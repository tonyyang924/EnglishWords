package tw.tonyyang.englishwords

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import jxl.Workbook
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.slf4j.LoggerFactory
import tw.tonyyang.englishwords.App.Companion.db
import tw.tonyyang.englishwords.database.Word
import tw.tonyyang.englishwords.databinding.FragmentDropboxchooserBinding
import tw.tonyyang.englishwords.util.PermissionManager
import tw.tonyyang.englishwords.util.PermissionManager.PermissionCallback
import tw.tonyyang.englishwords.util.UiUtils
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

class FileChooserFragment : Fragment() {

    private lateinit var binding: FragmentDropboxchooserBinding

    private var fileUrl: String? = null

    private val progress: AlertDialog? by lazy {
        UiUtils.getProgressDialog(activity as? Context, activity?.getString(R.string.loading_message))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDropboxchooserBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnSubmit.setOnClickListener {
            lifecycleScope.launch(Dispatchers.Main) {
                progress?.show()
                withContext(Dispatchers.IO) {
                    db?.userDao()?.deleteAll()
                    var data = ByteArray(0)
                    if (fileUrl?.contains("content://") == true || fileUrl?.contains("file:///") == true) {
                        data = readFile(fileUrl)
                    } else {
                        try {
                            val url = URL(fileUrl)
                            val arrayOutputStream = ByteArrayOutputStream()
                            val connection = url.openConnection() as? HttpURLConnection
                            connection?.connectTimeout = 10 * 1000
                            connection?.connect()
                            if (connection?.responseCode == 200) {
                                val inputStream = connection.inputStream
                                val buffer = ByteArray(10 * 1024)
                                while (true) {
                                    val len = inputStream.read(buffer)
                                    if (len == -1) {
                                        break
                                    }
                                    arrayOutputStream.write(buffer, 0, len)
                                }
                                arrayOutputStream.close()
                                inputStream.close()
                                data = arrayOutputStream.toByteArray()
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                    var fileOutputStream: FileOutputStream? = null
                    try {
                        fileOutputStream = activity?.openFileOutput(TMP_FILE_NAME, Context.MODE_PRIVATE)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    }
                    if (fileOutputStream != null) {
                        try {
                            fileOutputStream.write(data)
                            fileOutputStream.close()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                    }
                    readExcel(activity as? Context)
                }
                progress?.dismiss()
                Toast.makeText(context, activity?.getString(R.string.loading_complete), Toast.LENGTH_LONG).show()
                val realTimeUpdateEvent = RealTimeUpdateEvent(RealTimeUpdateEvent.Type.UPDATE_WORD_LIST)
                realTimeUpdateEvent.message = "更新列表資料"
                EventBus.getDefault().post(realTimeUpdateEvent)
            }
        }
        binding.btnSubmit.isEnabled = false
        binding.btnChooser.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 23) {
                activity?.let { it1 ->
                    PermissionManager.verifyStoragePermissions(it1, this@FileChooserFragment, object : PermissionCallback {
                        override fun onPermissionGranted() {
                            chooseFileFromLocal()
                        }

                        override fun onShowGuide() {
                            Toast.makeText(activity, "需要取得您的讀寫權限才能正常存取手機檔案，請至\"設定\"開啟讀寫權限。", Toast.LENGTH_LONG).show()
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", activity?.packageName, null)
                            intent.data = uri
                            activity?.startActivityForResult(intent, RequestCodeStore.REQUEST_EXTERNAL_STORAGE)
                        }
                    })
                }
            } else {
                chooseFileFromLocal()
            }
        }
    }

    private fun readFile(filePath: String?): ByteArray {
        val arrayOutputStream = ByteArrayOutputStream()
        val uri = Uri.parse(filePath)
        activity?.contentResolver?.openInputStream(uri)?.use {
            val buffer = ByteArray(10 * 1024)
            while (true) {
                val len = it.read(buffer)
                if (len == -1) {
                    break
                }
                arrayOutputStream.write(buffer, 0, len)
            }
            arrayOutputStream.close()
        }
        return arrayOutputStream.toByteArray()
    }

    private suspend fun readExcel(context: Context?) {
        if (context == null) {
            return
        }
        // TODO: fix this warning
        context.openFileInput(TMP_FILE_NAME).use {
            val book = Workbook.getWorkbook(it)
            book.numberOfSheets
            val sheet = book.getSheet(0)
            val rows = sheet.rows
            for (i in 0 until rows) {
                if (sheet.getCell(0, i).contents[0].toString() == "#") continue
                val word = Word(
                        word = sheet.getCell(0, i).contents,
                        wordMean = sheet.getCell(1, i).contents,
                        category = sheet.getCell(2, i).contents,
                        wordStar = sheet.getCell(3, i).contents,
                        wordSentence = sheet.getCell(4, i).contents
                )
                db?.userDao()?.insertAll(word)
            }
            book.close()
        }
    }

    private fun chooseFileFromLocal() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "application/vnd.ms-excel"
        }
        val destIntent = Intent.createChooser(intent, "檔案目錄選擇")
        if (activity == null || activity?.isFinishing == true || activity?.isDestroyed == true) {
            return
        }
        startActivityForResult(destIntent, RequestCodeStore.FILE_CHOOSER_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RequestCodeStore.FILE_CHOOSER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                logger.debug("file chooser")
                val uri = data?.data
                if (uri != null) {
                    fileUrl = uri.toString()
                    binding.tvFilename.text = fileUrl
                    binding.tvFileSize.text = ""
                    binding.btnSubmit.isEnabled = true
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RequestCodeStore.REQUEST_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                chooseFileFromLocal()
            } else {
                Toast.makeText(activity, "請取得內部檔案讀取權限，否則無法讀取本地端檔案。", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(FileChooserFragment::class.java)
        private const val TMP_FILE_NAME = "vocabulary.xls"
    }
}
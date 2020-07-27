package tw.tonyyang.englishwords

import android.app.Activity
import android.app.Fragment
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
import kotlinx.android.synthetic.main.fragment_dropboxchooser.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import tw.tonyyang.englishwords.App.Companion.db
import tw.tonyyang.englishwords.util.LoadTask
import tw.tonyyang.englishwords.util.PermissionManager
import tw.tonyyang.englishwords.util.PermissionManager.PermissionCallback
import tw.tonyyang.englishwords.util.Tool

class FileChooserFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_dropboxchooser, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_submit.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                withContext(Dispatchers.Default) {
                    db?.userDao()?.deleteAll()
                }.let {
                    val task = LoadTask(activity as Context)
                    task.setShowProgressView(true)
                    task.execute()
                }
            }
        }
        btn_submit.isEnabled = false
        btn_chooser.setOnClickListener {
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
                    Tool.fileUrl = uri.toString()
                    tv_filename.text = Tool.fileUrl
                    tv_file_size.text = ""
                    btn_submit.isEnabled = true
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
    }
}
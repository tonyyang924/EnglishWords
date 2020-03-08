package tw.tonyyang.englishwords.util

import android.content.Context
import android.net.Uri
import android.widget.Toast
import org.greenrobot.eventbus.EventBus
import tw.tonyyang.englishwords.R
import tw.tonyyang.englishwords.RealTimeUpdateEvent
import java.io.*
import java.net.HttpURLConnection
import java.net.URL

open class LoadTask(private val context: Context) : EggTask<Void?, Void?, Void?>(context) {
    override fun doInBackground(vararg params: Void?): Void? {
        var data = ByteArray(0)
        val fileUrl = Tool.fileUrl
        if (fileUrl != null && (fileUrl.contains("content://") || fileUrl.contains("file:///"))) {
            data = readFile(fileUrl)
        } else {
            try {
                val url = URL(Tool.fileUrl)
                val arrayOutputStream = ByteArrayOutputStream()
                val connection = url
                        .openConnection() as HttpURLConnection
                connection.connectTimeout = 10 * 1000
                connection.connect()
                if (connection.responseCode == 200) {
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
            fileOutputStream = context.openFileOutput(TMP_FILE_NAME, Context.MODE_PRIVATE)
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
        Tool.readExcel(context)
        return null
    }

    private fun readFile(filePath: String): ByteArray {
        val arrayOutputStream = ByteArrayOutputStream()
        val uri = Uri.parse(filePath)
        var inputStream: InputStream? = null
        try {
            inputStream = context.contentResolver.openInputStream(uri)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        if (inputStream != null) {
            val buffer = ByteArray(10 * 1024)
            try {
                while (true) {
                    val len = inputStream.read(buffer)
                    //publishProgress(len);
                    if (len == -1) {
                        break
                    }
                    arrayOutputStream.write(buffer, 0, len)
                }
                arrayOutputStream.close()
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        return arrayOutputStream.toByteArray()
    }

    override fun onPostExecute(result: Void?) {
        super.onPostExecute(result)
        Toast.makeText(context, context.getString(R.string.loading_complete), Toast.LENGTH_LONG).show()
        val realTimeUpdateEvent = RealTimeUpdateEvent(RealTimeUpdateEvent.Type.UPDATE_WORD_LIST)
        realTimeUpdateEvent.message = "更新列表資料"
        EventBus.getDefault().post(realTimeUpdateEvent)
    }

    override fun onCancelled() {
        super.onCancelled()
        // TODO: popup dialog ask user retry?
    }

    companion object {
        const val TMP_FILE_NAME = "vocabulary.xls"
    }

}
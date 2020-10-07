package tw.tonyyang.englishwords.util

import android.app.Activity
import android.content.Context
import android.net.Uri
import jxl.Workbook
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import tw.tonyyang.englishwords.App
import tw.tonyyang.englishwords.Logger
import tw.tonyyang.englishwords.database.Word
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL

class FileChooserUtils private constructor() {

    companion object {
        private val TAG = FileChooserUtils::class.java.simpleName
        private const val TMP_FILE_NAME = "vocabulary.xls"

        suspend fun importExcelDataToDb(activity: Activity?, fileUrl: String?) = coroutineScope {
            Logger.d(TAG, "[importExcelDataToDb] start")
            if (fileUrl == null) {
                Logger.d(TAG, "fileUrl is null.")
                return@coroutineScope
            }
            if (activity == null) {
                Logger.d(TAG, "activity is null.")
                return@coroutineScope
            }
            withContext(Dispatchers.IO) {
                val data = if (fileUrl.contains("content://") || fileUrl.contains("file:///")) {
                    readFile(activity, fileUrl)
                } else {
                    readFileFromInternet(fileUrl)
                }
                storeDataToTempFile(activity, data)
                getWorkbookFromTempFile(activity)?.let { book ->
                    if (book.sheets.isEmpty()) {
                        return@let
                    }
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
                        App.db?.userDao()?.insertAll(word)
                    }
                    book.close()
                }
            }
            Logger.d(TAG, "[importExcelDataToDb] end")
        }

        private fun readFile(activity: Activity, filePath: String): ByteArray {
            Logger.d(TAG, "[readFile] start")
            val arrayOutputStream = ByteArrayOutputStream()
            val uri = Uri.parse(filePath)
            activity.contentResolver.openInputStream(uri)?.use {
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
            Logger.d(TAG, "[readFile] end")
            return arrayOutputStream.toByteArray()
        }

        private fun readFileFromInternet(fileUrl: String): ByteArray {
            Logger.d(TAG, "[readFileFromInternet] start")
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
            }
            Logger.d(TAG, "[readFileFromInternet] end")
            return arrayOutputStream.toByteArray()
        }

        private fun storeDataToTempFile(activity: Activity, data: ByteArray) {
            activity.openFileOutput(TMP_FILE_NAME, Context.MODE_PRIVATE)?.use {
                it.write(data)
            }
        }

        private fun getWorkbookFromTempFile(activity: Activity): Workbook? {
            activity.openFileInput(TMP_FILE_NAME).use {
                return Workbook.getWorkbook(it)
            }
        }
    }
}
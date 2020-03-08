package tw.tonyyang.englishwords.util

import android.content.Context
import jxl.Workbook
import tw.tonyyang.englishwords.App
import tw.tonyyang.englishwords.database.Word
import java.io.FileInputStream
import java.io.IOException

object Tool {
    var fileUrl: String? = null

    fun readExcel(context: Context) {
        var fileInputStream: FileInputStream? = null
        try {
            fileInputStream = context.openFileInput(LoadTask.TMP_FILE_NAME)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (fileInputStream == null) {
            return
        }
        try {
            val book = Workbook.getWorkbook(fileInputStream)
            book.numberOfSheets
            //獲得工作表對象
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
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            fileInputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
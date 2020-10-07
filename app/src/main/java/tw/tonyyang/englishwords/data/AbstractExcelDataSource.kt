package tw.tonyyang.englishwords.data

import android.content.Context
import jxl.Workbook
import tw.tonyyang.englishwords.App

abstract class AbstractExcelDataSource : ExcelDataSource {
    override fun getData(url: String): Workbook {
        val bytes = getBytes(url)
        App.appContext.openFileOutput(TMP_FILE_NAME, Context.MODE_PRIVATE).use {
            it.write(bytes)
        }
        App.appContext.openFileInput(TMP_FILE_NAME).use {
            return Workbook.getWorkbook(it)
        }
    }

    abstract fun getBytes(url: String): ByteArray

    companion object {
        private const val TMP_FILE_NAME = "temp_vocabulary.xls"
    }
}
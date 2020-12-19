package tw.tonyyang.englishwords.data.excel.local

import android.net.Uri
import tw.tonyyang.englishwords.App
import tw.tonyyang.englishwords.data.excel.AbstractExcelDataSource
import java.io.IOException

class ExcelLocalDataSource : AbstractExcelDataSource() {
    override fun getBytes(url: String): ByteArray {
        val uri = Uri.parse(url)
        val bytes = App.appContext.contentResolver.openInputStream(uri)?.readBytes()
        if (bytes?.isEmpty() == true) {
            throw IOException("Local data is empty.")
        }
        return bytes ?: throw IOException("Cannot get data from local data source.")
    }
}
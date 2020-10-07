package tw.tonyyang.englishwords.data.remote

import okhttp3.OkHttpClient
import okhttp3.Request
import tw.tonyyang.englishwords.data.AbstractExcelDataSource
import java.io.IOException

class ExcelRemoteDataSource : AbstractExcelDataSource() {
    override fun getBytes(url: String): ByteArray {
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient()
        val response = client.newCall(request).execute()
        if (!response.isSuccessful) {
            throw IOException("Cannot get data from remote data source caused by: ${response.message()}")
        }
        val bytes = response.body()?.bytes()
        if (bytes?.isEmpty() == true) {
            throw IOException("Remote data is empty.")
        }
        return bytes
                ?: throw IOException("Cannot get data from remote data source since response body is null.")
    }
}
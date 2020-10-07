package tw.tonyyang.englishwords.data

import java.lang.Exception

sealed class ImporterResult<out T> {
    data class Success<T>(val data: T?) : ImporterResult<T>()
    data class Failure(val message: String) : ImporterResult<Nothing>()
    data class Error(val e: Exception) : ImporterResult<Nothing>()
}
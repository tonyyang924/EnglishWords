package tw.tonyyang.englishwords.data

sealed class ImporterResult<out T> {
    data class Success<T>(val data: T?) : ImporterResult<T>()
    data class Failure(val message: String) : ImporterResult<Nothing>()
    data class Error(val e: Throwable) : ImporterResult<Nothing>()
}
package tw.tonyyang.englishwords.state

// https://proandroiddev.com/implementing-an-android-app-with-jetpack-mvvm-ui-state-manage-and-some-other-interesting-stuff-e965b420f5a8
sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
    object InProgress : Result<Nothing>()

    val extractData: T?
        get() = when (this) {
            is Success -> data
            is Error -> null
            is InProgress -> null
        }
}
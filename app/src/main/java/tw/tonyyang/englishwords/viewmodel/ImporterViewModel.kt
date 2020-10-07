package tw.tonyyang.englishwords.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.launch
import tw.tonyyang.englishwords.App
import tw.tonyyang.englishwords.Logger
import tw.tonyyang.englishwords.R
import tw.tonyyang.englishwords.data.ImporterResult
import tw.tonyyang.englishwords.repository.ExcelRepository
import java.lang.Exception
import kotlin.system.measureTimeMillis

class ImporterViewModel(
        private val excelRepository: ExcelRepository = ExcelRepository()
) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _showResult = MutableLiveData<ImporterResult<String>>()
    val showResult: LiveData<ImporterResult<String>>
        get() = _showResult

    fun importWords(fileUrl: String?) {
        viewModelScope.launch {
            _isLoading.value = true
            val spendTime = measureTimeMillis {
                val wordList = try {
                    excelRepository.getWordList(fileUrl)
                } catch (e: Exception) {
                    _showResult.value = ImporterResult.Error(e)
                    return@measureTimeMillis
                }
                if (wordList.isEmpty()) {
                    _showResult.value = ImporterResult.Failure(App.appContext.getString(R.string.import_excel_failed_word_list_empty))
                    return@measureTimeMillis
                }
                App.db?.userDao()?.deleteAll()
                App.db?.userDao()?.insertAll(wordList)
                _showResult.value = ImporterResult.Success(App.appContext.getString(R.string.import_excel_complete))
            }
            Logger.d(TAG, "spendTime: $spendTime ms")
            _isLoading.value = false
        }
    }

    companion object {
        private val TAG = ImporterViewModel::class.java.simpleName
    }
}
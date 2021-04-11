package tw.tonyyang.englishwords.ui.importer

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import tw.tonyyang.englishwords.App
import tw.tonyyang.englishwords.Logger
import tw.tonyyang.englishwords.R
import tw.tonyyang.englishwords.repository.ExcelRepository
import tw.tonyyang.englishwords.state.Result
import java.lang.IllegalStateException
import kotlin.system.measureTimeMillis

class ImporterViewModel(
        private val excelRepository: ExcelRepository
) : ViewModel() {
    private val _showResult = MutableLiveData<Result<String>>()
    val showResult: LiveData<Result<String>>
        get() = _showResult

    fun importWords(fileUrl: String?) {
        viewModelScope.launch {
            _showResult.value = Result.InProgress
            val spendTime = measureTimeMillis {
                excelRepository.getWordList(fileUrl)
                        .flowOn(Dispatchers.IO)
                        .catch { e -> _showResult.value = Result.Error(e) }
                        .collect { wordList ->
                            if (wordList.isEmpty()) {
                                _showResult.value = Result.Error(IllegalStateException(App.appContext.getString(R.string.import_excel_failed_word_list_empty)))
                            }
                            App.db.wordDao().deleteAll()
                            val roomInsertedCount = App.db.wordDao().insertAll(wordList).size
                            _showResult.value = if (roomInsertedCount > 0) {
                                Result.Success(App.appContext.getString(R.string.import_excel_complete, roomInsertedCount))
                            } else {
                                Result.Error(IllegalStateException(App.appContext.getString(R.string.import_excel_insert_failed)))
                            }
                        }
            }
            Logger.d(TAG, "spendTime: $spendTime ms")
        }
    }

    companion object {
        private val TAG = ImporterViewModel::class.java.simpleName
    }
}
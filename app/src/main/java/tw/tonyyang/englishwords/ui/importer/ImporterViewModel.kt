package tw.tonyyang.englishwords.ui.importer

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import tw.tonyyang.englishwords.App
import tw.tonyyang.englishwords.util.Logger
import tw.tonyyang.englishwords.R
import tw.tonyyang.englishwords.database.entity.Word
import tw.tonyyang.englishwords.repository.ExcelRepository
import tw.tonyyang.englishwords.state.Result
import java.lang.IllegalStateException
import kotlin.system.measureTimeMillis

class ImporterViewModel(
        private val excelRepository: ExcelRepository
) : ViewModel() {

    private val _wordList = MutableLiveData<Result<List<Word>>>()
    val wordList: LiveData<Result<List<Word>>>
        get() = _wordList

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
                            // If list is empty, set `Result.Error` for MutableLivedata and return.
                            if (wordList.isEmpty()) {
                                IllegalStateException(App.appContext.getString(R.string.import_excel_failed_word_list_empty)).let {
                                    _showResult.value = Result.Error(it)
                                    _wordList.value = Result.Error(it)
                                }
                                return@collect
                            }
                            // Delete all vocabularies before doing insertion.
                            App.db.wordDao().deleteAll()
                            val roomInsertedCount = App.db.wordDao().insertAll(wordList).size
                            if (roomInsertedCount > 0) {
                                _showResult.value = Result.Success(App.appContext.getString(R.string.import_excel_complete, roomInsertedCount))
                                _wordList.value = Result.Success(wordList)
                            } else {
                                IllegalStateException(App.appContext.getString(R.string.import_excel_insert_failed)).let {
                                    _showResult.value = Result.Error(it)
                                    _wordList.value = Result.Error(it)
                                }
                            }
                        }
            }
            Logger.d(TAG, "spendTime: $spendTime ms")
        }
    }

    companion object {
        private const val TAG = "ImporterViewModel"
    }
}
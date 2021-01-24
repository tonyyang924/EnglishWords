package tw.tonyyang.englishwords.ui.exam

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import tw.tonyyang.englishwords.App
import tw.tonyyang.englishwords.Logger
import tw.tonyyang.englishwords.R
import tw.tonyyang.englishwords.database.entity.Word
import tw.tonyyang.englishwords.repository.ExamRepository
import tw.tonyyang.englishwords.state.Result
import java.lang.IllegalStateException
import kotlin.system.measureTimeMillis

class ExamViewModel(private val examRepository: ExamRepository) : ViewModel() {

    private val _randomWordList = MutableLiveData<Result<List<Word>>>()
    val randomWordList: LiveData<Result<List<Word>>>
        get() = _randomWordList

    fun requestRandomWords() {
        viewModelScope.launch {
            _randomWordList.value = Result.InProgress
            val spendTime = measureTimeMillis {
                examRepository.getRandomWords(RANDOM_WORDS_LIMIT_NUM)
                        .flowOn(Dispatchers.IO)
                        .catch { e ->
                            _randomWordList.value = Result.Error(e)
                        }
                        .collect { wordList ->
                            _randomWordList.value = if (wordList.isEmpty()) {
                                Result.Error(IllegalStateException(App.appContext.getString(R.string.failure_cannot_find_words)))
                            } else {
                                Result.Success(wordList)
                            }
                        }
            }
            Logger.d(TAG, "spendTime: $spendTime ms")
        }
    }

    companion object {
        private val TAG = ExamViewModel::class.java.simpleName
        private const val RANDOM_WORDS_LIMIT_NUM = 4
    }
}
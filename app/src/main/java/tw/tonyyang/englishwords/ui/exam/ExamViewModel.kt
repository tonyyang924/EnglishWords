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
import java.util.HashSet
import kotlin.system.measureTimeMillis

class ExamViewModel(private val examRepository: ExamRepository) : ViewModel() {

    private val _examData = MutableLiveData<Result<ExamData>>()
    val examData: LiveData<Result<ExamData>> = _examData

    fun requestExam() {
        viewModelScope.launch {
            _examData.value = Result.InProgress
            val spendTime = measureTimeMillis {
                examRepository.getRandomWords(RANDOM_WORDS_LIMIT_NUM)
                        .flowOn(Dispatchers.IO)
                        .catch { e ->
                            _examData.value = Result.Error(e)
                        }
                        .collect { wordList ->
                            _examData.value = if (wordList.isNotEmpty() && wordList.size == RANDOM_WORDS_LIMIT_NUM) {
                                val targetWord = wordList.first()
                                val randomArray = getRandomArray(wordList.size)
                                val answers = wordList.getAnswers(randomArray)
                                val examData = ExamData(targetWord.getWordAndIgnoreSymbol(), targetWord.wordMean, answers, randomArray.first())
                                Result.Success(examData)
                            } else {
                                Result.Error(IllegalStateException(App.appContext.getString(R.string.failure_cannot_find_words)))
                            }
                        }
            }
            Logger.d(TAG, "spendTime: $spendTime ms")
        }
    }

    private fun Word.getWordAndIgnoreSymbol(): String = word.replace(SYMBOL_STAR, "")

    private fun getRandomArray(size: Int): IntArray {
        var rnd: Int
        val random = IntArray(size)
        val rndSet: HashSet<Int> = HashSet<Int>(4)
        for (i in 0..3) {
            rnd = (4 * Math.random()).toInt()
            while (!rndSet.add(rnd)) rnd = (4 * Math.random()).toInt()
            random[i] = rnd
        }
        return random
    }

    private fun List<Word>.getAnswers(randomArray: IntArray): Array<String> {
        val answers = Array(randomArray.size) { "" }
        for (i in 0 until size) {
            answers[i] = this[randomArray[i]].getWordAndIgnoreSymbol()
        }
        return answers
    }

    companion object {
        private const val TAG = "ExamViewModel"
        private const val RANDOM_WORDS_LIMIT_NUM = 4
        private const val SYMBOL_STAR = "*"
    }
}
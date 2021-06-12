package tw.tonyyang.englishwords.ui.word.list

import androidx.lifecycle.*
import tw.tonyyang.englishwords.database.entity.Word
import tw.tonyyang.englishwords.repository.WordListRepository

class WordListViewModel(private val wordListRepository: WordListRepository) : ViewModel() {

    fun getWordList(category: String): LiveData<List<Word>> =
        wordListRepository.getWordList(category)
}
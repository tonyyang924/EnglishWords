package tw.tonyyang.englishwords.repository

import androidx.lifecycle.LiveData
import tw.tonyyang.englishwords.data.wordlist.local.WordListLocalDataSource
import tw.tonyyang.englishwords.database.entity.Word

interface WordListRepository {
    fun getWordList(category: String): LiveData<List<Word>>
}

class WordListRepositoryImpl(private val localDataSource: WordListLocalDataSource) :
    WordListRepository {

    override fun getWordList(category: String): LiveData<List<Word>> =
        localDataSource.getWordList(category)
}
package tw.tonyyang.englishwords.data.word.list.local

import androidx.lifecycle.LiveData
import tw.tonyyang.englishwords.App
import tw.tonyyang.englishwords.database.entity.Word

interface WordListLocalDataSource {
    fun getWordList(category: String): LiveData<List<Word>>
}

class WordListLocalDataSourceImpl : WordListLocalDataSource {

    override fun getWordList(category: String): LiveData<List<Word>> =
        App.db.wordDao().getCategoryWords(category)
}
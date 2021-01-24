package tw.tonyyang.englishwords.data.exam.local

import tw.tonyyang.englishwords.App
import tw.tonyyang.englishwords.database.entity.Word

interface ExamLocalDataSource {
    fun getRandomWords(limitNum: Int): List<Word>
}

class ExamLocalDataSourceImpl : ExamLocalDataSource {
    override fun getRandomWords(limitNum: Int): List<Word> = App.db.userDao().getRandomWords(limitNum)
}
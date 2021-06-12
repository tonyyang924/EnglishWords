package tw.tonyyang.englishwords.data.exam.local

import tw.tonyyang.englishwords.database.dao.WordDao
import tw.tonyyang.englishwords.database.entity.Word

interface ExamLocalDataSource {
    fun getRandomWords(limitNum: Int): List<Word>
}

class ExamLocalDataSourceImpl(private val wordDao: WordDao) : ExamLocalDataSource {
    override fun getRandomWords(limitNum: Int): List<Word> = wordDao.getRandomWords(limitNum)
}
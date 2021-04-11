package tw.tonyyang.englishwords.data.category.local

import tw.tonyyang.englishwords.database.dao.WordDao

interface CategoryLocalDataSource {
    fun getCategories(): List<String>
}

class CategoryLocalDataSourceImpl(private val wordDao: WordDao) : CategoryLocalDataSource {
    override fun getCategories(): List<String> = wordDao.allCategory
}
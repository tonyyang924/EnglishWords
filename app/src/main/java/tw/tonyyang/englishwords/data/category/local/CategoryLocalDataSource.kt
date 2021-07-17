package tw.tonyyang.englishwords.data.category.local

import androidx.lifecycle.LiveData
import tw.tonyyang.englishwords.database.dao.WordDao

interface CategoryLocalDataSource {
    fun getCategories(): LiveData<List<String>>
}

class CategoryLocalDataSourceImpl(private val wordDao: WordDao) : CategoryLocalDataSource {
    override fun getCategories(): LiveData<List<String>> = wordDao.allCategory
}
package tw.tonyyang.englishwords.repository

import androidx.lifecycle.LiveData
import tw.tonyyang.englishwords.data.category.local.CategoryLocalDataSource

interface CategoryRepository {
    fun getCategories(): LiveData<List<String>>
}

class CategoryRepositoryImpl(private val categoryLocalDataSource: CategoryLocalDataSource) : CategoryRepository {
    override fun getCategories(): LiveData<List<String>> = categoryLocalDataSource.getCategories()
}
package tw.tonyyang.englishwords.repository

import tw.tonyyang.englishwords.data.category.local.CategoryLocalDataSource

interface CategoryRepository {
    fun getCategories(): List<String>
}

class CategoryRepositoryImpl(private val categoryLocalDataSource: CategoryLocalDataSource) : CategoryRepository {
    override fun getCategories(): List<String> = categoryLocalDataSource.getCategories()
}
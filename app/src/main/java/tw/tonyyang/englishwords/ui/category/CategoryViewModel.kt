package tw.tonyyang.englishwords.ui.category

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tw.tonyyang.englishwords.repository.CategoryRepository

class CategoryViewModel(private val categoryRepository: CategoryRepository) : ViewModel() {
    fun getCategoryList(): LiveData<List<String>> = categoryRepository.getCategories()
}
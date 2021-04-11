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

    private val _categories = MutableLiveData<List<String>>()
    val categories: LiveData<List<String>>
        get() = _categories

    fun updateCategoryList() {
        viewModelScope.launch {
            withContext(Dispatchers.Default) {
                _categories.postValue(categoryRepository.getCategories())
            }
        }
    }
}
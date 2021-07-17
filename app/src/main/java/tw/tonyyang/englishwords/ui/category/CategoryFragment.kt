package tw.tonyyang.englishwords.ui.category

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import tw.tonyyang.englishwords.databinding.FragmentCategoryBinding
import tw.tonyyang.englishwords.state.Result
import tw.tonyyang.englishwords.ui.base.OnRecyclerViewListener
import tw.tonyyang.englishwords.ui.importer.ImporterViewModel
import tw.tonyyang.englishwords.ui.word.list.WordListActivity

class CategoryFragment : Fragment() {

    private val importerViewModel: ImporterViewModel by sharedViewModel()

    private val categoryViewModel: CategoryViewModel by viewModel()

    private val categoryAdapter: CategoryAdapter by lazy {
        CategoryAdapter(onRecyclerViewListener)
    }

    private lateinit var binding: FragmentCategoryBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = categoryAdapter
        }
        importerViewModel.wordList.observe(viewLifecycleOwner) {
            when (it) {
                is Result.InProgress -> {
                    // do nothing
                }
                is Result.Success -> {
                    categoryAdapter.categoryList = it.data.map { word ->
                        word.category
                    }.distinct()
                }
                is Result.Error -> {
                    // do nothing
                }
            }
        }
        categoryViewModel.categories.observe(viewLifecycleOwner) {
            categoryAdapter.categoryList = it
        }
        categoryViewModel.updateCategoryList()
    }

    private val onRecyclerViewListener: OnRecyclerViewListener = object : OnRecyclerViewListener {
        override fun onItemClick(v: View, position: Int) {
            val category = categoryAdapter.getItem(position)
            val intent = Intent(activity, WordListActivity::class.java)
            val bundle = Bundle()
            bundle.putString(WordListActivity.EXTRA_CATEGORY, category)
            intent.putExtras(bundle)
            startActivity(intent)
        }
    }

    companion object {
        fun newInstance() = CategoryFragment()
    }
}
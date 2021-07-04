package tw.tonyyang.englishwords.ui.category

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.koin.androidx.viewmodel.ext.android.viewModel
import tw.tonyyang.englishwords.util.Logger
import tw.tonyyang.englishwords.RealTimeUpdateEvent
import tw.tonyyang.englishwords.databinding.FragmentCategoryBinding
import tw.tonyyang.englishwords.ui.base.OnRecyclerViewListener
import tw.tonyyang.englishwords.ui.word.list.WordListActivity

class CategoryFragment : Fragment() {

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

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRealTimeUpdateEvent(event: RealTimeUpdateEvent) {
        val type = event.type
        if (type === RealTimeUpdateEvent.Type.UPDATE_WORD_LIST) {
            Logger.d(TAG, "UPDATE_WORD_LIST: " + event.message)
            categoryViewModel.updateCategoryList()
        }
    }

    companion object {
        private const val TAG = "CategoryFragment"

        fun newInstance() = CategoryFragment()
    }
}
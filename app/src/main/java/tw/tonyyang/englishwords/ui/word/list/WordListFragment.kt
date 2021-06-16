package tw.tonyyang.englishwords.ui.word.list

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import tw.tonyyang.englishwords.*
import tw.tonyyang.englishwords.databinding.FragmentWordListBinding
import tw.tonyyang.englishwords.ui.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import tw.tonyyang.englishwords.ui.word.detail.WordListDetailActivity

class WordListFragment : BaseFragment() {

    private lateinit var binding: FragmentWordListBinding

    private val viewModel: WordListViewModel by viewModel()

    private val category: String by lazy {
        arguments?.getString(WordListActivity.EXTRA_CATEGORY) ?: ""
    }

    private val wordListAdapter: WordListAdapter by lazy {
        WordListAdapter()
    }

    private val onRecyclerViewListener: WordListAdapter.OnRecyclerViewListener =
        object : WordListAdapter.OnRecyclerViewListener {
            override fun onItemClick(v: View?, position: Int) {
                Intent(activity, WordListDetailActivity::class.java).apply {
                    Bundle().apply {
                        putSerializable(
                            WordListDetailActivity.EXTRA_SELECTED_WORD,
                            wordListAdapter.getItem(position)
                        )
                    }.let {
                        putExtras(it)
                    }
                }.let {
                    startActivity(it)
                }
            }

            override fun onItemLongClick(v: View?, position: Int) {
                // do nothing
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWordListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.toolbar.toolbar.init()
        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(binding.toolbar.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = wordListAdapter
        }
        wordListAdapter.onRecyclerViewListener = onRecyclerViewListener
        viewModel.getWordList(category).observe(viewLifecycleOwner) {
            wordListAdapter.wordList = it
        }
    }

    companion object {

        fun newInstance(category: String) = WordListFragment().apply {
            val bundle = Bundle().apply {
                putString(WordListActivity.EXTRA_CATEGORY, category)
            }
            arguments = bundle
        }
    }
}
package tw.tonyyang.englishwords

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import tw.tonyyang.englishwords.App.Companion.db
import tw.tonyyang.englishwords.databinding.ActivityWordListBinding

class WordListM2Activity : AppCompatActivity() {

    private val binding by viewBinding(ActivityWordListBinding::inflate)

    private val category: String by lazy {
        intent.extras?.getString(EXTRA_CATEGORY) as String
    }

    private val wordListM2Adapter: WordListM2Adapter by lazy {
        WordListM2Adapter()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_list)
        binding.toolbar.toolbar.init()
        setSupportActionBar(binding.toolbar.toolbar)
        initActionBar()
        initRecyclerViews()
        updateWordList()
    }

    private fun initActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initRecyclerViews() {
        binding.recyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@WordListM2Activity)
            adapter = wordListM2Adapter
        }
        wordListM2Adapter.onRecyclerViewListener = onRecyclerViewListener
    }

    private fun updateWordList() = lifecycleScope.launch(Dispatchers.Main) {
        withContext(Dispatchers.Default) {
            db.wordDao().getCategoryWords(category)
        }.let {
            wordListM2Adapter.wordList = it
        }
    }

    private val onRecyclerViewListener: WordListM2Adapter.OnRecyclerViewListener = object : WordListM2Adapter.OnRecyclerViewListener {
        override fun onItemClick(v: View?, position: Int) {
            Intent(this@WordListM2Activity, WordListDetailActivity::class.java).apply {
                Bundle().apply {
                    putSerializable(WordListDetailActivity.EXTRA_SELECTED_WORDS, wordListM2Adapter.getItem(position))
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val EXTRA_CATEGORY = "extra_category"
    }
}
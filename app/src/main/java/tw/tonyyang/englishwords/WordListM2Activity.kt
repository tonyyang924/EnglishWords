package tw.tonyyang.englishwords

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tw.tonyyang.englishwords.App.Companion.db

class WordListM2Activity : BaseActivity() {
    private val category: String by lazy {
        intent.extras?.getString(EXTRA_CATEGORY) as String
    }
    private val wordListM2Adapter: WordListM2Adapter by lazy {
        WordListM2Adapter(db?.userDao()?.getCategoryWords(category) ?: listOf())
    }
    override val layoutResource: Int
        get() = R.layout.activity_word_list

    override fun onViewCreated() {
        initActionBar()
        initRecyclerViews()
    }

    private fun initActionBar() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initRecyclerViews() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        wordListM2Adapter.setOnRecyclerViewListener(onRecyclerViewListener)
        recyclerView.adapter = wordListM2Adapter
    }

    private val onRecyclerViewListener: WordListM2Adapter.OnRecyclerViewListener = object : WordListM2Adapter.OnRecyclerViewListener {
        override fun onItemClick(v: View?, position: Int) {
            val intent = Intent(this@WordListM2Activity, WordListDetailActivity::class.java)
            val bundle = Bundle()
            bundle.putSerializable(WordListDetailActivity.EXTRA_SELECTED_WORDS, wordListM2Adapter.getItem(position))
            intent.putExtras(bundle)
            startActivity(intent)
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
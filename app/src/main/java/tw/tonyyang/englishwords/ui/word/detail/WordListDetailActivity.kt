package tw.tonyyang.englishwords.ui.word.detail

import android.os.Bundle
import android.view.MenuItem
import tw.tonyyang.englishwords.R
import tw.tonyyang.englishwords.database.entity.Word
import tw.tonyyang.englishwords.databinding.ActivityWordListDetailBinding
import tw.tonyyang.englishwords.ui.base.BaseActivity
import tw.tonyyang.englishwords.ui.base.viewBinding

class WordListDetailActivity : BaseActivity() {

    private val binding by viewBinding(ActivityWordListDetailBinding::inflate)

    private val selectedWord: Word? by lazy {
        intent.extras?.getParcelable(EXTRA_SELECTED_WORD) as? Word
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, WordListDetailFragment.newInstance(selectedWord))
                .commitNow()
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
        const val EXTRA_SELECTED_WORD = "extra_selected_word"
    }
}
package tw.tonyyang.englishwords

import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_word_list_info.*
import tw.tonyyang.englishwords.database.Word

class WordListDetailActivity : BaseActivity() {
    private var selectedWords: Word? = null
    override val layoutResource: Int
        get() = R.layout.activity_word_list_info

    override fun onViewCreated() {
        initActionBar()
        initExtras()
        setViews()
    }

    private fun initActionBar() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initExtras() {
        intent.extras?.let {
            selectedWords = it.getSerializable(EXTRA_SELECTED_WORDS) as Word?
        }
    }

    private fun setViews() {
        selectedWords.let {
            tv_word.text = selectedWords?.word?.replace("*", "")
            tv_wordmean.text = selectedWords?.wordMean
            tv_word_sentence.text = selectedWords?.wordSentence
            tv_category.text = selectedWords?.category
            ratingbar.setStar(selectedWords?.wordStar?.toFloat() ?: 0F)
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
        const val EXTRA_SELECTED_WORDS = "extra_selected_words"
    }
}
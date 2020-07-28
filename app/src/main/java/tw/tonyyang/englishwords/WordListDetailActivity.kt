package tw.tonyyang.englishwords

import android.view.MenuItem
import android.widget.TextView
import com.hedgehog.ratingbar.RatingBar
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
            findViewById<TextView>(R.id.tv_word).text = selectedWords?.word?.replace("*", "")
            findViewById<TextView>(R.id.tv_wordmean).text = selectedWords?.wordMean
            findViewById<TextView>(R.id.tv_word_sentence).text = selectedWords?.wordSentence
            findViewById<TextView>(R.id.tv_category).text = selectedWords?.category
            findViewById<RatingBar>(R.id.ratingbar).setStar(selectedWords?.wordStar?.toFloat() ?: 0F)
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
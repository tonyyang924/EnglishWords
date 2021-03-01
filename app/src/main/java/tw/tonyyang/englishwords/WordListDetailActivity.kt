package tw.tonyyang.englishwords

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import tw.tonyyang.englishwords.database.entity.Word
import tw.tonyyang.englishwords.databinding.ActivityWordListInfoBinding

class WordListDetailActivity : AppCompatActivity() {
    private var selectedWords: Word? = null

    private val binding by viewBinding(ActivityWordListInfoBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_word_list_info)
        binding.toolbar.toolbar.init()
        setSupportActionBar(binding.toolbar.toolbar)
        initExtras()
        initActionBar()
        setViews()
    }

    private fun initActionBar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun initExtras() {
        intent.extras?.let {
            selectedWords = it.getSerializable(EXTRA_SELECTED_WORDS) as Word?
        }
    }

    private fun setViews() {
        selectedWords.let {
            binding.tvWord.text = selectedWords?.word?.replace("*", "")
            binding.tvWordmean.text = selectedWords?.wordMean
            binding.tvWordSentence.text = selectedWords?.wordSentence
            binding.tvCategory.text = selectedWords?.category
            binding.ratingbar.setStar(selectedWords?.wordStar?.toFloat() ?: 0F)
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
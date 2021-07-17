package tw.tonyyang.englishwords.ui.word.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import tw.tonyyang.englishwords.util.Logger
import tw.tonyyang.englishwords.database.entity.Word
import tw.tonyyang.englishwords.databinding.FragmentWordListDetailBinding
import tw.tonyyang.englishwords.ui.base.BaseFragment

class WordListDetailFragment : BaseFragment() {

    private lateinit var binding: FragmentWordListDetailBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentWordListDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initActionbar()
        setViews()
    }

    private fun initActionbar() {
        (activity as? AppCompatActivity)?.apply {
            setSupportActionBar(binding.toolbar.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setViews() {
        val word = arguments?.getParcelable(WordListDetailActivity.EXTRA_SELECTED_WORD) as? Word
        if (word != null) {
            Logger.d(TAG, "word: $word")
            binding.tvWord.text = word.word.replace("*", "")
            binding.tvWordmean.text = word.wordMean
            binding.tvWordSentence.text = word.wordSentence
            binding.tvCategory.text = word.category
            binding.ratingbar.setStar(word.wordStar.toFloat())
        } else {
            Logger.d(TAG, "word is null")
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
        private const val TAG = "WordListDetailFragment"

        fun newInstance(selectedWords: Word?) = WordListDetailFragment().apply {
            val bundle = Bundle().apply {
                putParcelable(WordListDetailActivity.EXTRA_SELECTED_WORD, selectedWords)
            }
            arguments = bundle
        }
    }
}
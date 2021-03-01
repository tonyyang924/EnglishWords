package tw.tonyyang.englishwords.ui.exam

import android.os.Bundle
import android.view.*
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import tw.tonyyang.englishwords.Logger
import tw.tonyyang.englishwords.R
import tw.tonyyang.englishwords.database.entity.Word
import tw.tonyyang.englishwords.databinding.FragmentExamBinding
import tw.tonyyang.englishwords.state.Result
import java.util.*

class ExamFragment : Fragment() {

    private val examViewModel: ExamViewModel by viewModel()

    private var trueWord: String? = null

    private val ansRadioBtnList: List<RadioButton> by lazy {
        listOf(binding.rbAns1, binding.rbAns2, binding.rbAns3, binding.rbAns4)
    }

    private lateinit var binding: FragmentExamBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentExamBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAnswer.setOnClickListener {
            var result = ""
            for (i in ansRadioBtnList.indices) {
                if (ansRadioBtnList[i].isChecked) {
                    result = ansRadioBtnList[i].text.toString()
                }
            }
            binding.tvResult.text = if (result == trueWord) "答對了！\n答案是：$trueWord" else "答錯了唷！\n答案是：$trueWord"
        }
        examViewModel.randomWordList.observe(viewLifecycleOwner) {
            when (it) {
                is Result.InProgress -> {
                    Logger.d(TAG, "Result.InProgress")
                }
                is Result.Success -> {
                    updateUI(it.data)
                }
                is Result.Error -> {
                    Toast.makeText(activity, it.exception.message, Toast.LENGTH_LONG).show()
                }
            }
        }
        examViewModel.requestRandomWords()
    }

    private fun updateUI(list: List<Word>) {
        if (list.isEmpty()) {
            binding.tvStatus.visibility = View.VISIBLE
            binding.tvChineseMean.text = ""
            for (i in 0 until binding.rgAns.childCount) {
                binding.rgAns.getChildAt(i).isEnabled = false
            }
            binding.btnAnswer.isEnabled = false
        } else {
            binding.tvStatus.visibility = View.GONE
            trueWord = list[0].word.replace("*", "")
            binding.tvChineseMean.text = list[0].wordMean
            var rnd: Int
            val random = IntArray(4)
            val rndSet: HashSet<Int> = HashSet<Int>(4)
            for (i in 0..3) {
                rnd = (4 * Math.random()).toInt()
                while (!rndSet.add(rnd)) rnd = (4 * Math.random()).toInt()
                random[i] = rnd
            }
            for (i in ansRadioBtnList.indices) {
                ansRadioBtnList[i].text = list[random[i]].word.replace("*", "")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.exam, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            activity?.onBackPressed()
            return true
        } else if (item.itemId == R.id.action_random) {
            examViewModel.requestRandomWords()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private val TAG = ExamFragment::class.java.simpleName
    }
}
package tw.tonyyang.englishwords.ui.exam

import android.os.Bundle
import android.view.*
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel
import tw.tonyyang.englishwords.util.Logger
import tw.tonyyang.englishwords.R
import tw.tonyyang.englishwords.databinding.FragmentExamBinding
import tw.tonyyang.englishwords.state.Result

class ExamFragment : Fragment() {

    private val examViewModel: ExamViewModel by viewModel()

    private var trueWord: String? = null

    private val ansRadioBtnList: List<RadioButton> by lazy {
        listOf(binding.rbAns1, binding.rbAns2, binding.rbAns3, binding.rbAns4)
    }

    private var menuVisible = false

    private var randomItem: MenuItem? = null

    private lateinit var binding: FragmentExamBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentExamBinding.inflate(inflater, container, false)
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
        examViewModel.examData.observe(viewLifecycleOwner) {
            when (it) {
                is Result.InProgress -> {
                    Logger.d(TAG, "Result.InProgress")
                }
                is Result.Success -> {
                    updateUI(it.data)
                }
                is Result.Error -> {
                    Toast.makeText(activity, it.exception.message, Toast.LENGTH_LONG).show()
                    handleError()
                }
            }
        }
        examViewModel.requestExam()
    }

    private fun updateUI(examData: ExamData) {
        binding.tvStatus.visibility = View.GONE
        trueWord = examData.trueWord
        binding.tvChineseMean.text = examData.wordMean
        for (i in ansRadioBtnList.indices) {
            ansRadioBtnList[i].text = examData.answers[i]
        }
    }

    private fun handleError() {
        binding.tvStatus.visibility = View.VISIBLE
        binding.tvChineseMean.text = ""
        for (i in 0 until binding.rgAns.childCount) {
            binding.rgAns.getChildAt(i).isEnabled = false
        }
        binding.btnAnswer.isEnabled = false
    }

    override fun onResume() {
        super.onResume()
        randomItem?.isVisible = menuVisible
    }

    override fun setMenuVisibility(menuVisible: Boolean) {
        super.setMenuVisibility(menuVisible)
        this.menuVisible = menuVisible
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.exam, menu)
        randomItem = menu.findItem(R.id.action_random)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_random) {
            examViewModel.requestExam()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        private const val TAG = "ExamFragment"
    }
}
package tw.tonyyang.englishwords

import android.app.Fragment
import android.os.Bundle
import android.view.*
import android.widget.RadioButton
import kotlinx.android.synthetic.main.fragment_exam.*
import kotlinx.coroutines.*
import tw.tonyyang.englishwords.App.Companion.db
import tw.tonyyang.englishwords.database.Word
import java.util.*

class ExamFragment : Fragment() {

    private var trueWord: String? = null

    private val ansRadioBtnList: List<RadioButton> by lazy {
        listOf(rb_ans1, rb_ans2, rb_ans3, rb_ans4)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_exam, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_answer.setOnClickListener {
            var result = ""
            for (i in ansRadioBtnList.indices) {
                if (ansRadioBtnList[i].isChecked) {
                    result = ansRadioBtnList[i].text.toString()
                }
            }
            tv_result.text = if (result == trueWord) "答對了！\n答案是：$trueWord" else "答錯了唷！\n答案是：$trueWord"
        }
        updateUI(getRandomWordList())
    }

    private fun updateUI(list: List<Word>) {
        if (getWordCount() == 0 || list.isEmpty()) {
            tv_status.visibility = View.VISIBLE
            tv_chinese_mean.text = ""
            for (i in 0 until rg_ans.childCount) {
                rg_ans.getChildAt(i).isEnabled = false
            }
            btn_answer.isEnabled = false
        } else {
            tv_status.visibility = View.GONE
            trueWord = list[0].word.replace("*", "")
            tv_chinese_mean.text = list[0].wordMean
            var rnd: Int
            val random = IntArray(4)
            val rndSet: HashSet<Int> = HashSet<Int>(4)
            for (i in 0..3) {
                rnd = (4 * Math.random()).toInt()
                while (!rndSet.add(rnd)) rnd = (4 * Math.random()).toInt()
                random[i] = rnd
            }
            for (i in ansRadioBtnList.indices) {
                ansRadioBtnList[i].text = list[random[i]]?.word?.replace("*", "")
            }
        }
    }

    private fun getRandomWordList(): List<Word> = runBlocking {
        withContext(Dispatchers.Default) {
            db?.userDao()?.getRandomWords(4)
        } ?: mutableListOf()
    }

    private fun getWordCount(): Int = runBlocking {
        withContext(Dispatchers.Default) {
            db?.userDao()?.count
        } ?: 0
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
            updateUI(getRandomWordList())
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
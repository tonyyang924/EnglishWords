package tw.tonyyang.englishwords

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_word_list.*
import kotlinx.coroutines.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.slf4j.LoggerFactory
import tw.tonyyang.englishwords.App.Companion.db

class WordListM1Fragment : Fragment() {

    private val wordListM1Adapter: WordListM1Adapter by lazy {
        WordListM1Adapter()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.fragment_word_list, container, false)
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wordListM1Adapter.onRecyclerViewListener = onRecyclerViewListener
        recyclerview.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = wordListM1Adapter
        }
        updateCategoryList()
    }

    private fun updateCategoryList() = GlobalScope.launch(Dispatchers.Main) {
        withContext(Dispatchers.Default) {
            db?.userDao()?.allCategory ?: listOf()
        }.let {
            wordListM1Adapter.categoryList = it
        }
    }

    private val onRecyclerViewListener: WordListM1Adapter.OnRecyclerViewListener = object : WordListM1Adapter.OnRecyclerViewListener {
        override fun onItemClick(v: View?, position: Int) {
            val category = wordListM1Adapter.getItem(position)
            val intent = Intent(activity, WordListM2Activity::class.java)
            val bundle = Bundle()
            bundle.putString(WordListM2Activity.EXTRA_CATEGORY, category)
            intent.putExtras(bundle)
            startActivity(intent)
        }

        override fun onItemLongClick(v: View?, position: Int) {
            // do nothing
        }
    }

    override fun onStart() {
        super.onStart()
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRealTimeUpdateEvent(event: RealTimeUpdateEvent) {
        val type = event.type
        if (type === RealTimeUpdateEvent.Type.UPDATE_WORD_LIST) {
            logger.debug("UPDATE_WORD_LIST: " + event.message)
            updateCategoryList()
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(WordListM1Fragment::class.java)
    }
}
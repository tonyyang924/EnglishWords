package tw.tonyyang.englishwords;

import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@EFragment(R.layout.fragment_word_list)
public class WordListM1Fragment extends Fragment {
    private static final Logger logger = LoggerFactory.getLogger(WordListM1Fragment.class);

    @ViewById(R.id.recyclerview_word_list)
    RecyclerView recyclerView;

    private WordListM1Adapter wordListM1Adapter;

    @AfterViews
    protected void initViews() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        wordListM1Adapter = new WordListM1Adapter(getAllCategory());
        wordListM1Adapter.setOnRecyclerViewListener(onRecyclerViewListener);
        recyclerView.setAdapter(wordListM1Adapter);
    }

    private List<String> getAllCategory() {
        return App_.getDb().userDao().getAllCategory();
    }

    private WordListM1Adapter.OnRecyclerViewListener onRecyclerViewListener = new WordListM1Adapter.OnRecyclerViewListener() {

        @Override
        public void onItemClick(View v, int position) {
            String category = wordListM1Adapter.getItem(position);
            WordListM2Activity_.intent(getActivity())
                    .category(category)
                    .start();
        }

        @Override
        public void onItemLongClick(View v, int position) {

        }
    };

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onRealTimeUpdateEvent(RealTimeUpdateEvent event) {
        RealTimeUpdateEvent.Type type = event.getType();
        if (type == RealTimeUpdateEvent.Type.UPDATE_WORD_LIST) {
            logger.debug("UPDATE_WORD_LIST: " + event.getMessage());
            wordListM1Adapter.setWordList(getAllCategory());
            wordListM1Adapter.notifyDataSetChanged();
        }
    }
}

package tw.tonyyang.englishwords;

import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;

import tw.tonyyang.englishwords.db.Words;
import tw.tonyyang.englishwords.db.WordsDao;


@EFragment(R.layout.fragment_wordslistmain)
public class WordsListM1Fragment extends Fragment {
    private static final Logger logger = LoggerFactory.getLogger(WordsListM1Fragment.class);

    @Bean
    WordsDao wordsDao;

    @ViewById(R.id.wordslistmain_recyclerview)
    RecyclerView recyclerView;

    private WordsListM1Adapter wordsListM1Adapter;

    @AfterViews
    protected void initViews() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        wordsListM1Adapter = new WordsListM1Adapter(getData());
        wordsListM1Adapter.setOnRecyclerViewListener(onRecyclerViewListener);
        recyclerView.setAdapter(wordsListM1Adapter);
    }

    private List<Words> getData() {
        try {
            return wordsDao.getRawDao().queryBuilder().distinct().selectColumns(Words.CATEGORY).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private WordsListM1Adapter.OnRecyclerViewListener onRecyclerViewListener = new WordsListM1Adapter.OnRecyclerViewListener() {

        @Override
        public void onItemClick(View v, int position) {
            Words words = wordsListM1Adapter.getItem(position);
            WordsListM2Activity_.intent(getActivity())
                    .day(words.getCategory())
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
        switch (type) {
            case UPDATE_WORD_LIST:
                logger.debug("UPDATE_WORD_LIST: " + event.getMessage());
                wordsListM1Adapter.setWordsList(getData());
                wordsListM1Adapter.notifyDataSetChanged();
                break;
        }
    }

}

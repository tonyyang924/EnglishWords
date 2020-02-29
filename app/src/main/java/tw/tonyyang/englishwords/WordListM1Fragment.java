package tw.tonyyang.englishwords;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


public class WordListM1Fragment extends Fragment {
    private static final Logger logger = LoggerFactory.getLogger(WordListM1Fragment.class);

    private WordListM1Adapter wordListM1Adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_word_list, container, false);
        wordListM1Adapter = new WordListM1Adapter(getAllCategory());
        wordListM1Adapter.setOnRecyclerViewListener(onRecyclerViewListener);
        final RecyclerView recyclerView = view.findViewById(R.id.recyclerview_word_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(wordListM1Adapter);
        return view;
    }

    private List<String> getAllCategory() {
        return App.getDb().userDao().getAllCategory();
    }

    private WordListM1Adapter.OnRecyclerViewListener onRecyclerViewListener = new WordListM1Adapter.OnRecyclerViewListener() {

        @Override
        public void onItemClick(View v, int position) {
            final String category = wordListM1Adapter.getItem(position);
            final Intent intent = new Intent(getActivity(), WordListM2Activity.class);
            final Bundle bundle = new Bundle();
            bundle.putString(WordListM2Activity.EXTRA_CATEGORY, category);
            intent.putExtras(bundle);
            startActivity(intent);
        }

        @Override
        public void onItemLongClick(View v, int position) {
            // do nothing
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
        final RealTimeUpdateEvent.Type type = event.getType();
        if (type == RealTimeUpdateEvent.Type.UPDATE_WORD_LIST) {
            logger.debug("UPDATE_WORD_LIST: " + event.getMessage());
            wordListM1Adapter.setWordList(getAllCategory());
            wordListM1Adapter.notifyDataSetChanged();
        }
    }
}

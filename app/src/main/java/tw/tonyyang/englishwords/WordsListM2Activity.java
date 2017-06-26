package tw.tonyyang.englishwords;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

import java.sql.SQLException;
import java.util.List;

import tw.tonyyang.englishwords.db.Words;
import tw.tonyyang.englishwords.db.WordsDao;

@EActivity(R.layout.activity_wordslist)
public class WordsListM2Activity extends BaseActivity {

    @Extra
    String day;

    @Bean
    WordsDao wordsDao;

    @ViewById(R.id.words_recyclerView)
    RecyclerView recyclerView;

    private WordsListM2Adapter wordsListM2Adapter;

    @AfterViews
    protected void initViews() {
        super.initViews();
        initActionBar();
        setRecyclerViews();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setRecyclerViews() {
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        List<Words> wordsList = getSQLiteData(day);
        wordsListM2Adapter = new WordsListM2Adapter(wordsList);
        wordsListM2Adapter.setOnRecyclerViewListener(onRecyclerViewListener);
        recyclerView.setAdapter(wordsListM2Adapter);
    }

    private WordsListM2Adapter.OnRecyclerViewListener onRecyclerViewListener = new WordsListM2Adapter.OnRecyclerViewListener() {
        @Override
        public void onItemClick(View v, int position) {
            WordsListDetailActivity_.intent(WordsListM2Activity.this)
                    .selectedWords(wordsListM2Adapter.getItem(position))
                    .start();
        }

        @Override
        public void onItemLongClick(View v, int position) {

        }
    };

    private List<Words> getSQLiteData(String theDay) {
        try {
            return wordsDao.getRawDao().queryBuilder().where().eq(Words.CATEGORY, theDay).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

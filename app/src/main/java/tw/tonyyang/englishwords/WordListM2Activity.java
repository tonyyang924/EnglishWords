package tw.tonyyang.englishwords;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_word_list)
public class WordListM2Activity extends BaseActivity {

    @Extra
    String category;

    @ViewById(R.id.words_recyclerView)
    RecyclerView recyclerView;

    private WordListM2Adapter wordListM2Adapter;

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
        wordListM2Adapter = new WordListM2Adapter(App_.getDb().userDao().getCategoryWords(category));
        wordListM2Adapter.setOnRecyclerViewListener(onRecyclerViewListener);
        recyclerView.setAdapter(wordListM2Adapter);
    }

    private WordListM2Adapter.OnRecyclerViewListener onRecyclerViewListener = new WordListM2Adapter.OnRecyclerViewListener() {
        @Override
        public void onItemClick(View v, int position) {
            WordListDetailActivity_.intent(WordListM2Activity.this)
                    .selectedWords(wordListM2Adapter.getItem(position))
                    .start();
        }

        @Override
        public void onItemLongClick(View v, int position) {

        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

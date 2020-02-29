package tw.tonyyang.englishwords;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

public class WordListM2Activity extends BaseActivity {

    public static final String EXTRA_CATEGORY = "extra_category";

    private String category;

    private WordListM2Adapter wordListM2Adapter;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_word_list;
    }

    @Override
    protected void onViewCreated() {
        initActionBar();
        initExtras();
        initRecyclerViews();
    }

    private void initActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initExtras() {
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            category = bundle.getString(EXTRA_CATEGORY);
        }
    }

    private void initRecyclerViews() {
        RecyclerView recyclerView = findViewById(R.id.words_recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        wordListM2Adapter = new WordListM2Adapter(App.getDb().userDao().getCategoryWords(category));
        wordListM2Adapter.setOnRecyclerViewListener(onRecyclerViewListener);
        recyclerView.setAdapter(wordListM2Adapter);
    }

    private WordListM2Adapter.OnRecyclerViewListener onRecyclerViewListener = new WordListM2Adapter.OnRecyclerViewListener() {
        @Override
        public void onItemClick(View v, int position) {
            final Intent intent = new Intent(WordListM2Activity.this, WordListDetailActivity.class);
            final Bundle bundle = new Bundle();
            bundle.putSerializable(WordListDetailActivity.EXTRA_SELECTED_WORDS, wordListM2Adapter.getItem(position));
            intent.putExtras(bundle);
            startActivity(intent);
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

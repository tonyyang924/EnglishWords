package tw.tonyyang.englishwords;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;
import android.widget.TextView;

import com.hedgehog.ratingbar.RatingBar;

import tw.tonyyang.englishwords.database.Word;


public class WordListDetailActivity extends BaseActivity {

    public static final String EXTRA_SELECTED_WORDS = "extra_selected_words";

    private Word selectedWords;

    @Override
    protected int getLayoutResource() {
        return R.layout.activity_word_list_info;
    }

    @Override
    protected void onViewCreated() {
        initActionBar();
        initExtras();
        setViews();
    }

    private void initActionBar() {
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    private void initExtras() {
        final Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            selectedWords = (Word) bundle.getSerializable(EXTRA_SELECTED_WORDS);
        }
    }

    private void setViews() {
        final TextView wordTV = findViewById(R.id.wordTV);
        final TextView wordMeanTV = findViewById(R.id.wordmeanTV);
        final TextView wordSentenceTV = findViewById(R.id.wordsentenceTV);
        final TextView categoryTV = findViewById(R.id.categoryTV);
        final RatingBar ratingbar = findViewById(R.id.ratingbar);
        if (selectedWords != null) {
            wordTV.setText(selectedWords.getWord().replace("*", ""));
            wordMeanTV.setText(selectedWords.getWordMean());
            wordSentenceTV.setText(selectedWords.getWordSentence());
            categoryTV.setText(selectedWords.getCategory());
            ratingbar.setStar(Float.parseFloat(selectedWords.getWordStar()));
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

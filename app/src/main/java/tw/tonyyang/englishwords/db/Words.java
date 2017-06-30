package tw.tonyyang.englishwords.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by tonyyang on 2017/6/1.
 */

@DatabaseTable(tableName = DatabaseHelper.TABLE_NAME)
public class Words implements Serializable {

    public static final String ID = "_id";
    public static final String CATEGORY = "category";
    public static final String WORD = "word";
    public static final String WORD_STAR = "word_star";
    public static final String WORD_MEAN = "word_mean";
    public static final String WORD_SENTENCE = "word_sentence";

    @DatabaseField(columnName = ID, id = true)
    private String id;

    @DatabaseField(columnName = CATEGORY)
    private String category;

    @DatabaseField(columnName = WORD)
    private String word;

    @DatabaseField(columnName = WORD_STAR)
    private String wordStar;

    @DatabaseField(columnName = WORD_MEAN)
    private String wordMean;

    @DatabaseField(columnName = WORD_SENTENCE)
    private String wordSentence;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWordStar() {
        return wordStar;
    }

    public void setWordStar(String wordStar) {
        this.wordStar = wordStar;
    }

    public String getWordMean() {
        return wordMean;
    }

    public void setWordMean(String wordMean) {
        this.wordMean = wordMean;
    }

    public String getWordSentence() {
        return wordSentence;
    }

    public void setWordSentence(String wordSentence) {
        this.wordSentence = wordSentence;
    }
}

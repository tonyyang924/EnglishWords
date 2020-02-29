package tw.tonyyang.englishwords.database;

import androidx.annotation.NonNull;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Word implements Serializable {
    private static final String ID = "_id";
    private static final String CATEGORY = "category";
    private static final String WORD = "word";
    private static final String WORD_STAR = "word_star";
    private static final String WORD_MEAN = "word_mean";
    private static final String WORD_SENTENCE = "word_sentence";

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = CATEGORY)
    private String category;

    @ColumnInfo(name = WORD)
    private String word;

    @ColumnInfo(name = WORD_STAR)
    private String wordStar;

    @ColumnInfo(name = WORD_MEAN)
    private String wordMean;

    @ColumnInfo(name = WORD_SENTENCE)
    private String wordSentence;

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    @NonNull
    @Override
    public String toString() {
        return "Words{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", word='" + word + '\'' +
                ", wordStar='" + wordStar + '\'' +
                ", wordMean='" + wordMean + '\'' +
                ", wordSentence='" + wordSentence + '\'' +
                '}';
    }
}

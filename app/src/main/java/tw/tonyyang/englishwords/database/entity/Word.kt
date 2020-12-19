package tw.tonyyang.englishwords.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Word(@PrimaryKey(autoGenerate = true) val id: Int = 0,
           @ColumnInfo(name = "category") val category: String,
           @ColumnInfo(name = "word") val word: String,
           @ColumnInfo(name = "word_star") val wordStar: String,
           @ColumnInfo(name = "word_mean") val wordMean: String,
           @ColumnInfo(name = "word_sentence") val wordSentence: String
) : Serializable {
    override fun toString(): String {
        return "Words{" +
                "id='" + id + '\'' +
                ", category='" + category + '\'' +
                ", word='" + word + '\'' +
                ", wordStar='" + wordStar + '\'' +
                ", wordMean='" + wordMean + '\'' +
                ", wordSentence='" + wordSentence + '\'' +
                '}'
    }
}
package tw.tonyyang.englishwords.database.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Word(
        @PrimaryKey(autoGenerate = true) val id: Int = 0,
        @ColumnInfo(name = "category") val category: String,
        @ColumnInfo(name = "word") val word: String,
        @ColumnInfo(name = "word_star") val wordStar: String,
        @ColumnInfo(name = "word_mean") val wordMean: String,
        @ColumnInfo(name = "word_sentence") val wordSentence: String
) : Parcelable
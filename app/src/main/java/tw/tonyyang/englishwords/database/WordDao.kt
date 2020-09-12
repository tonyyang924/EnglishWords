package tw.tonyyang.englishwords.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WordDao {
    @get:Query("SELECT count(*) FROM word")
    val count: Int

    @Query("SELECT * FROM word ORDER BY RANDOM() LIMIT (:limit)")
    fun getRandomWords(limit: Int): List<Word>

    @get:Query("SELECT DISTINCT category FROM word")
    val allCategory: List<String>

    @Query("SELECT * FROM word WHERE category = (:category)")
    fun getCategoryWords(category: String): List<Word>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg words: Word)

    @Query("DELETE FROM word")
    suspend fun deleteAll()
}
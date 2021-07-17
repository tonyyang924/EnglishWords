package tw.tonyyang.englishwords.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import tw.tonyyang.englishwords.database.entity.Word

@Dao
interface WordDao {
    @get:Query("SELECT count(*) FROM word")
    val count: Int

    @Query("SELECT * FROM word ORDER BY RANDOM() LIMIT (:limit)")
    fun getRandomWords(limit: Int): List<Word>

    @get:Query("SELECT DISTINCT category FROM word")
    val allCategory: LiveData<List<String>>

    @Query("SELECT * FROM word WHERE category = (:category)")
    fun getCategoryWords(category: String): LiveData<List<Word>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(words: List<Word>): LongArray

    @Query("DELETE FROM word")
    suspend fun deleteAll()
}
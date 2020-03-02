package tw.tonyyang.englishwords.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WordDao {

    @Query("SELECT count(*) FROM word")
    int getCount();

    @Query("SELECT * FROM word ORDER BY RANDOM() LIMIT (:limit)")
    List<Word> getRandomWords(int limit);

    @Query("DELETE FROM word")
    void deleteAll();

    @Query("SELECT DISTINCT category FROM word")
    List<String> getAllCategory();

    @Query("SELECT * FROM word WHERE category = (:category)")
    List<Word> getCategoryWords(String category);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Word... words);
}

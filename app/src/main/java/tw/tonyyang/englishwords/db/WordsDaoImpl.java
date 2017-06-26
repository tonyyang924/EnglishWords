package tw.tonyyang.englishwords.db;

import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

/**
 * Created by tonyyang on 2017/6/2.
 */

public class WordsDaoImpl extends BaseDaoImpl<Words, String> {
    public WordsDaoImpl(ConnectionSource connectionSource) throws SQLException {
        super(connectionSource, Words.class);
    }
}

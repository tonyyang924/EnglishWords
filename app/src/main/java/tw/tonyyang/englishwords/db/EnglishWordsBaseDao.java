package tw.tonyyang.englishwords.db;

import android.content.Context;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;

/**
 * Created by tonyyang on 2017/6/2.
 */

public abstract class EnglishWordsBaseDao<Entity, ID> extends BaseDao<Entity, ID> {

    public EnglishWordsBaseDao(Context ctx) {
        super(ctx);
    }

    @Override
    protected Class<? extends OrmLiteSqliteOpenHelper> getHelperClass() {
        return DatabaseHelper.class;
    }
}

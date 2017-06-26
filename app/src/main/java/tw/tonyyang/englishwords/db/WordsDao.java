package tw.tonyyang.englishwords.db;

import android.content.Context;

import org.androidannotations.annotations.EBean;

/**
 * Created by tonyyang on 2017/6/2.
 */

@EBean(scope = EBean.Scope.Singleton)
public class WordsDao extends EnglishWordsBaseDao<Words, String> {

    public WordsDao(Context ctx) {
        super(ctx);
    }

    @Override
    protected String getEntityId(Words words) {
        return words.getId();
    }
}

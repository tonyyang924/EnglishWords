package tw.tonyyang.englishwords.db;

import android.content.Context;
import android.database.Cursor;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.misc.TransactionManager;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;

import org.androidannotations.annotations.EBean;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * Created by gary on 9/1/15.
 */
@EBean
public abstract class BaseDao<Entity, ID> {

    protected Context context;

    private Class<Entity> entityClass;

    private Class<ID> idClass;

    private final Class<? extends OrmLiteSqliteOpenHelper> helperClass;

    public BaseDao(Context ctx) {
        context = ctx;
        findTypeArguments(getClass());
        helperClass = getHelperClass();
    }

    private void findTypeArguments(Type t) {
        if (t instanceof ParameterizedType) {
            Type[] typeArgs = ((ParameterizedType) t).getActualTypeArguments();
            entityClass = (Class<Entity>) typeArgs[0];
            idClass = (Class<ID>) typeArgs[1];
        } else {
            Class c = (Class) t;
            findTypeArguments(c.getGenericSuperclass());
        }
    }

    protected abstract Class<? extends OrmLiteSqliteOpenHelper> getHelperClass();

    protected Context getContext() {
        return context;
    }

    public Class<Entity> getEntityClass() {
        return entityClass;
    }

    public Class<ID> getIdClass() {
        return idClass;
    }

    public Dao<Entity, ID> getRawDao() {
        OrmLiteSqliteOpenHelper helper = OpenHelperManager.getHelper(context, helperClass);
        try {
            return helper.getDao(entityClass);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Entity queryForId(ID id) throws SQLException {
        return getRawDao().queryForId(id);
    }

    public List<Entity> queryForAll() throws SQLException {
        return getRawDao().queryForAll();
    }

    public Entity create(Entity e) throws SQLException {
        getRawDao().create(e);
        return e;
    }

    public Entity createIfNotExists(Entity e) throws SQLException {
        getRawDao().createIfNotExists(e);
        return e;
    }

    public int update(Entity e) throws SQLException {
        return getRawDao().update(e);
    }

    public Entity createOrUpdate(Entity e) throws SQLException {
        getRawDao().createOrUpdate(e);
        return e;
    }

    public void delete(Entity gcmNotification) throws SQLException {
        getRawDao().delete(gcmNotification);
    }

    public int deleteById(ID id) throws SQLException {
        return getRawDao().deleteById(id);
    }

    public boolean idExists(ID id) throws SQLException {
        return getRawDao().idExists(id);
    }

    protected Entity mapCursorToEntity(Cursor cursor) throws SQLException {
        try {
            if (cursor.moveToFirst()) {
                return getRawDao().mapSelectStarRow(new AndroidDatabaseResults(cursor, null));
            } else {
                return null;
            }
        } finally {
            cursor.close();
        }
    }

    protected abstract ID getEntityId(Entity entity);

    protected <T> T transaction(Callable<T> callable) throws SQLException {
        Dao<Entity, ID> rawDao = getRawDao();
        ConnectionSource conn = rawDao.getConnectionSource();
        return TransactionManager.callInTransaction(conn, callable);
    }

    public GenericRawResults<Entity> queryRaw(String stmt, RawRowMapper<Entity> rawRowMapper) throws SQLException {
        return getRawDao().queryRaw(stmt, rawRowMapper);
    }

    public GenericRawResults<String[]> queryRaw(String stmt, String... params) throws SQLException {
        return getRawDao().queryRaw(stmt, params);
    }

    public List<Entity> subquery(QueryBuilder<Entity, ID> mainBuilder, QueryBuilder<Entity, ID> subBuilder) throws SQLException {
        String mainStmt = mainBuilder.prepareStatementString();

        Dao<Entity, ID> dao = getRawDao();
        String tableName = ((BaseDaoImpl) dao).getTableInfo().getTableName();

        mainStmt = mainStmt.replace("`" + tableName + "`", "(" + subBuilder.prepareStatementString() + ")");

        GenericRawResults<Entity> result = dao.queryRaw(mainStmt, dao.getRawRowMapper());
        return result.getResults();
    }

    public long count() throws SQLException {
        return getRawDao().countOf();
    }

    public void deleteAll() throws SQLException {
        getRawDao().deleteBuilder().delete();
    }

    public boolean needMigration() throws SQLException {
        return needMigration(false);
    }

    public boolean needMigration(boolean updateZeroCheckingState) throws SQLException {
        return false;
    }

    public void batchCreate(final List<Entity> chList) throws SQLException {
        transaction(new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                for (Entity e : chList) {
                    create(e);
                }
                return null;
            }
        });
    }
}

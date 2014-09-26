package cn.core.mobile.library.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.core.mobile.library.database.annotation.Table;
import cn.core.mobile.library.database.exception.DbException;
import cn.core.mobile.library.database.sqlite.CursorUtils;
import cn.core.mobile.library.database.sqlite.SqlBuilder;
import cn.core.mobile.library.database.sqlite.SqlInfo;
import cn.core.mobile.library.database.table.TableInfo;
import cn.core.mobile.library.database.utils.ClassUtils;
import cn.core.mobile.library.remote.response.Entity;

/**
 * 一个轻量级的ORM工具
 * Created by dallas on 14-4-10.
 */
public class DBHelper {
    private static final String TAG = DBHelper.class.getName();

    private SQLiteDatabase mDatabase;
    private DBConfig mConfig;
    private volatile static DBHelper sInstance;
    private DbUpgradeListener mUpgradeListener;

    private static final String ERROR_NOT_INIT = "DBHelper must be init with config before using";
    private static final String ERROR_INIT_CONFIG_WITH_NULL = "DBHelper config can not be initialized with null";

    public static DBHelper getInstance() {
        if (sInstance == null) {
            synchronized (DBHelper.class) {
                if (sInstance == null) {
                    sInstance = new DBHelper();
                }
            }
        }
        return sInstance;
    }

    protected DBHelper() {
    }

    public synchronized void init(DBConfig config) {
        if (config == null) {
            throw new DbException(ERROR_INIT_CONFIG_WITH_NULL);
        }
        if (this.mConfig == null) {
            this.mConfig = config;

            if (!TextUtils.isEmpty(mConfig.dbPath)) {
                mDatabase = createDbFileOnSDCard(mConfig.dbPath, mConfig.dbName);
            } else {
                mDatabase = new SQLiteDbHelper(mConfig.context, mConfig.dbName, null, mConfig.dbVersion).getWritableDatabase();
            }
        } else {

        }
    }

    public boolean isInited() {
        return this.mConfig != null;
    }

    public void setUpgradeListener(DbUpgradeListener listener) {
        this.mUpgradeListener = listener;
    }

    private void checkConfiguration() {
        if (this.mConfig == null) {
            throw new DbException(ERROR_NOT_INIT);
        }
    }

    /**
     * 在SDCard上创建数据库文件
     *
     * @param sdcardPath 数据库文件创建路径
     * @param dbfilename 数据库名称
     * @return 成功返回SQLiteDatabase对象，否则返回<code>null<code/>
     */
    private SQLiteDatabase createDbFileOnSDCard(String sdcardPath, String dbfilename) {
        File dbf = new File(sdcardPath, dbfilename);
        if (!dbf.exists()) {
            try {
                if (dbf.createNewFile()) {
                    return SQLiteDatabase.openOrCreateDatabase(dbf, null);
                }
            } catch (IOException ioex) {
                Log.e(TAG, "数据库文件创建失败, Error msg:" + ioex.getMessage());
            }
        } else {
            return SQLiteDatabase.openOrCreateDatabase(dbf, null);
        }

        return null;
    }

    public SQLiteDatabase getDatabase() {
        return mDatabase;
    }

    /**
     * 插入数据
     *
     * @param entity 数据对象
     */
    public void insert(Object entity) {
        checkConfiguration();
        TableInfo tableInfo = TableInfo.get(entity.getClass());
        checkTableExist(tableInfo);
        exeSqlInfo(SqlBuilder.buildInsertSql(tableInfo, entity, null));
    }

    /**
     * 删除数据
     *
     * @param clazz       Class 对象
     * @param whereClause the optional WHERE clause to apply when deleting.
     *                    Passing null will delete all rows.
     * @param whereArgs   You may include ?s in the where clause, which
     *                    will be replaced by the values from whereArgs. The values
     *                    will be bound as Strings.
     */
    public void delete(Class<?> clazz, String whereClause, String[] whereArgs) {
        checkConfiguration();
        TableInfo tableInfo = TableInfo.get(clazz);
        checkTableExist(tableInfo);
        exeSqlInfo(SqlBuilder.buildDeleteSql(tableInfo, whereClause, whereArgs));
    }

    /**
     * 删除表中所有数据
     *
     * @param clazz Class 对象
     */
    public void delete(Class<?> clazz) {
        delete(clazz, null, null);
    }

    /**
     * 更新数据
     */
    public void update(Class<?> clazz, ContentValues values, String whereClause, String[] whereArgs) {
        checkConfiguration();
        TableInfo tableInfo = TableInfo.get(clazz);
        checkTableExist(tableInfo);
        mDatabase.update(tableInfo.getTableName(), values, whereClause, whereArgs);
    }

    /**
     * 更新数据
     */
    public void update(Object entity, String whereClause, String[] whereArgs) {
        checkConfiguration();
        TableInfo tableInfo = TableInfo.get(entity.getClass());
        checkTableExist(tableInfo);
        exeSqlInfo(SqlBuilder.buildUpdateSql(tableInfo, entity, whereClause, whereArgs));
    }

    /**
     * 查询数据
     */
    public <T> List<T> query(Class<T> clazz) {
        return query(clazz, null, null);
    }

    /**
     * 查询数据
     */
    public <T> List<T> query(Class<T> clazz, String where, String[] whereArgs) {
        return query(clazz, where, whereArgs, null);
    }

    /**
     * 查询数据
     */
    public <T> List<T> query(Class<T> clazz, String where, String[] whereArgs, String orderBy) {
        return query(clazz, false, where, whereArgs, null, null, orderBy, null);
    }

    /**
     * 查询数据
     */
    public <T> List<T> query(Class<T> clazz, boolean distinct, String where, String[] whereArgs,
                             String groupBy, String having, String orderBy, String limit) {
        checkConfiguration();
        TableInfo tableInfo = TableInfo.get(clazz);
        checkTableExist(tableInfo);
        SqlInfo sqlInfo = SqlBuilder.buildQuerySql(tableInfo, distinct, null, where, whereArgs, groupBy, having, orderBy, limit);
        Cursor cursor = mDatabase.rawQuery(sqlInfo.getSql(), (String[]) sqlInfo.getBindArgs());
        List<T> list = new ArrayList<T>();

        if (cursor == null) {
            return list;
        }
        try {
            while (cursor.moveToNext()) {
                T entity = CursorUtils.getEntity(cursor, tableInfo);
                list.add(entity);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return list;
    }

    /**
     * 删除指定的表
     *
     * @param clazz Class 对象
     */
    public void dropTable(Class<?> clazz) {
        checkConfiguration();
        TableInfo tableInfo = TableInfo.get(clazz);
        exeSqlInfo(SqlBuilder.buildDropTableSql(tableInfo));
    }

    /**
     * 删除所有数据库表
     */
    public void dropAllTables() {
        checkConfiguration();
        Cursor cursor = mDatabase.rawQuery(
                "SELECT name FROM sqlite_master WHERE type ='table' AND name != 'sqlite_sequence'", null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                mDatabase.execSQL("DROP TABLE " + cursor.getString(0));
            }
        }
        if (cursor != null) {
            cursor.close();
        }
    }

    /**
     * 设置指定数据库表的数据更新日期
     *
     * @param clazz      数据库表 clazz
     * @param no         no
     * @param updateTime 更新日期
     */
    public void setUpdateTime(Class<?> clazz, String no, long updateTime) {
        String source = ClassUtils.getTableName(clazz);

        UpdateInfo updateInfo = new UpdateInfo();
        updateInfo.setUpdateTime(updateTime);
        updateInfo.setSource(source);
        updateInfo.setId(no);

        List<String> selectionArgs = new ArrayList<String>();
        StringBuilder selection = new StringBuilder();
        selection.append("source");
        selection.append(" = ? ");
        selectionArgs.add(source);

        if (no != null && !no.equals("")) {
            selection.append(" and ");
            selection.append("id");
            selection.append(" = ? ");
            selectionArgs.add(no);
        }

        List<UpdateInfo> result = query(UpdateInfo.class, selection.toString(), selectionArgs.toArray(new String[]{}));
        if (result != null && result.size() > 0) {
            updateInfo.setUpdateTime(result.get(0).getUpdateTime());
            update(updateInfo, selection.toString(), selectionArgs.toArray(new String[]{}));
        } else {
            insert(updateInfo);
        }
    }

    /**
     * 获取指定数据库表的数据最后更新日期
     *
     * @param clazz 数据库表clazz
     * @param no    no
     * @return 最后更新时间
     */
    public long getUpdateTime(Class<?> clazz, String no) {
        String source = ClassUtils.getTableName(clazz);

        List<String> selectionArgs = new ArrayList<String>();
        StringBuilder selection = new StringBuilder();
        selection.append("source");
        selection.append(" = ? ");
        selectionArgs.add(source);

        if (no != null && !no.equals("")) {
            selection.append(" and ");
            selection.append("id");
            selection.append(" = ? ");
            selectionArgs.add(no);
        }
        List<UpdateInfo> result = query(UpdateInfo.class, selection.toString(), selectionArgs.toArray(new String[]{}));
        if (result != null && result.size() > 0) {
            result.get(0).getUpdateTime();
        }
        return 0;
    }

    public void beginTransaction() {
        checkConfiguration();
        mDatabase.beginTransaction();
    }

    public void setTransactionSuccessful() {
        checkConfiguration();
        mDatabase.setTransactionSuccessful();
    }

    public void endTransaction() {
        checkConfiguration();
        mDatabase.endTransaction();
    }

    /**
     * 执行SQL语句。
     *
     * @param sqlInfo SQL语句信息
     */
    private void exeSqlInfo(SqlInfo sqlInfo) {
        if (sqlInfo == null) {
            Log.e(TAG, "sqlInfo is null");
            return;
        }
        debugSql(sqlInfo.getSql(), sqlInfo.getBindArgs());
        if (sqlInfo.getBindArgs() != null) {
            mDatabase.execSQL(sqlInfo.getSql(), sqlInfo.getBindArgs());
        } else {
            mDatabase.execSQL(sqlInfo.getSql());
        }
    }

    /**
     * 检查实体类对应的数据表是否创建,如没有创建则创建。
     *
     * @param tableInfo TableInfo对象
     */
    private void checkTableExist(TableInfo tableInfo) {
        if (!isTableExist(tableInfo)) {
            exeSqlInfo(SqlBuilder.buildCreateTableSql(tableInfo));
        }
    }

    /**
     * 根据TableInfo对象判断数据库表是否已经创建
     *
     * @param tableInfo TableInfo对象
     * @return 已经创建返回true，否则返回false
     */
    private boolean isTableExist(TableInfo tableInfo) {
        if (tableInfo.isTableExist()) {
            return true;
        }
        Cursor cursor = null;
        try {
            String sql = "SELECT COUNT(*) AS c FROM sqlite_master WHERE type ='table' AND name ='"
                    + tableInfo.getTableName() + "'";
            debugSql(sql, null);
            cursor = mDatabase.rawQuery(sql, null);
            if (cursor != null && cursor.moveToNext()) {
                int count = cursor.getInt(0);
                if (count > 0) {
                    tableInfo.setTableExist(true);
                    return true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return false;
    }

    private void debugSql(String sql, Object[] bindArgs) {
        if (mConfig.debug) {
            Log.d("Debug SQL", ">>>>>>  " + sql + (bindArgs == null ? "" : " bindArgs:" + Arrays.toString(bindArgs)));
        }
    }


    class SQLiteDbHelper extends SQLiteOpenHelper {
        public SQLiteDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (mUpgradeListener != null) {
                mUpgradeListener.onUpgrade(db, oldVersion, newVersion);
            } else {
                //删除所有数据库表
                Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type ='table' AND name != 'sqlite_sequence'", null);
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        db.execSQL("DROP TABLE " + cursor.getString(0));
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

    }

    /**
     * 数据库升级监听器
     * Created by dallas on 14-4-10.
     */
    public interface DbUpgradeListener {
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);
    }

    /**
     * 记录数据更新时间
     *
     * @author Will.Wu </br> Create at 2014/5/26
     * @version 1.0
     */
    @Table(name = "t_update")
    class UpdateInfo extends Entity {
        private static final long serialVersionUID = -1186367388916429319L;
        private String source;
        private long updateTime;

        public String getSource() {
            return source;
        }

        public void setSource(String source) {
            this.source = source;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }
    }
}

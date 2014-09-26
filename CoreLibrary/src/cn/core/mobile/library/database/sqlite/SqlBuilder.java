package cn.core.mobile.library.database.sqlite;

import android.database.sqlite.SQLiteQueryBuilder;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cn.core.mobile.library.database.table.KeyValue;
import cn.core.mobile.library.database.table.Property;
import cn.core.mobile.library.database.table.TableInfo;

/**
 * Created by dallas on 14-4-20.
 */
public class SqlBuilder {

    /**
     * 构建创建表的sql语句。
     *
     * @param tableInfo 数据库表信息TableInfo对象
     * @return sql语句SqlInfo对象
     */
    public static SqlInfo buildCreateTableSql(TableInfo tableInfo) {
        if (tableInfo == null) {
            return null;
        }

        Property id = tableInfo.getId();

        StringBuilder strSQL = new StringBuilder();
        strSQL.append("CREATE TABLE IF NOT EXISTS ");
        strSQL.append(tableInfo.getTableName());
        strSQL.append(" ( ");

        Class<?> primaryClazz = id.getDataType();
        if (primaryClazz == int.class || primaryClazz == Integer.class
                || primaryClazz == long.class || primaryClazz == Long.class) {
            strSQL.append(id.getColumn()).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");
        } else {
            strSQL.append(id.getColumn()).append(" TEXT PRIMARY KEY,");
        }

        Collection<Property> properties = tableInfo.propertyMap.values();
        for (Property property : properties) {
            strSQL.append(property.getColumn());
            Class<?> dataType = property.getDataType();
            if (dataType == int.class || dataType == Integer.class
                    || dataType == long.class || dataType == Long.class) {
                strSQL.append(" INTEGER");
            } else if (dataType == float.class || dataType == Float.class
                    || dataType == double.class || dataType == Double.class) {
                strSQL.append(" REAL");
            } else if (dataType == boolean.class || dataType == Boolean.class) {
                strSQL.append(" NUMERIC");
            }
            strSQL.append(",");
        }

        strSQL.deleteCharAt(strSQL.length() - 1);
        strSQL.append(" )");

        SqlInfo info = new SqlInfo();
        info.setSql(strSQL.toString());
        return info;

    }

    /**
     * 构建删除表的sql语句。
     *
     * @param tableInfo 数据库表信息TableInfo对象
     * @return sql语句SqlInfo对象
     */
    public static SqlInfo buildDropTableSql(TableInfo tableInfo) {
        if (tableInfo == null) {
            return null;
        }
        String sql = "DROP TABLE IF EXISTS " + tableInfo.getTableName();

        SqlInfo info = new SqlInfo();
        info.setSql(sql);
        return info;
    }


    /**
     * 构建插入数据的sql语句。
     *
     * @param tableInfo      数据库表信息TableInfo对象
     * @param entity         类对象
     * @param nullColumnHack optional; may be <code>null</code>.
     * @return 插入数据sql语句SqlInfo对象
     */
    public static SqlInfo buildInsertSql(TableInfo tableInfo, Object entity, String nullColumnHack) {
        if (entity == null || tableInfo == null) {
            return null;
        }
        List<KeyValue> keyValueList = getContentValues(tableInfo, entity);
        StringBuilder sql = new StringBuilder();
        sql.append("INSERT");
        sql.append(" INTO ");
        sql.append(tableInfo.getTableName());
        sql.append('(');

        Object[] bindArgs = null;
        int size = (keyValueList != null && keyValueList.size() > 0)
                ? keyValueList.size() : 0;
        if (size > 0) {
            bindArgs = new Object[size];
            int i = 0;
            for (KeyValue keyValue : keyValueList) {
                String colName = keyValue.getKey();
                Object arg = keyValue.getValue();
                sql.append((i > 0) ? "," : "");
                sql.append(colName);
                bindArgs[i++] = arg;
            }
            sql.append(')');
            sql.append(" VALUES (");
            for (i = 0; i < size; i++) {
                sql.append((i > 0) ? ",?" : "?");
            }
        } else {
            sql.append(nullColumnHack + ") VALUES (NULL");
        }
        sql.append(')');

        SqlInfo info = new SqlInfo();
        info.setSql(sql.toString());
        info.setBindArgs(bindArgs);
        return info;
    }

    /**
     * 创建删除数据的sql语句
     *
     * @param tableInfo   数据库表信息TableInfo对象
     * @param whereClause WHERE语句。如果传null表示删除表中所有数据
     * @param whereArgs   WHERE语句如果有？占位符，则需要该参数
     * @return 删除数据sql语句SqlInfo对象
     */
    public static SqlInfo buildDeleteSql(TableInfo tableInfo, String whereClause, String[] whereArgs) {
        if (tableInfo == null) {
            return null;
        }
        String sql = "DELETE FROM " + tableInfo.getTableName() + ((whereClause != null && whereClause.length() != 0) ? " WHERE " + whereClause : "");
        SqlInfo info = new SqlInfo();
        info.setSql(sql);
        info.setBindArgs(whereArgs);
        return info;
    }

    /**
     * 创建更新数据的sql语句
     *
     * @param tableInfo   数据库表信息TableInfo对象
     * @param entity      类对象
     * @param whereClause WHERE语句。如果传null表示更新表中所有数据
     * @param whereArgs   WHERE语句如果有？占位符，则需要该参数
     * @return 更新数据sql语句SqlInfo对象
     */
    public static SqlInfo buildUpdateSql(TableInfo tableInfo, Object entity, String whereClause, String[] whereArgs) {
        if (entity == null || tableInfo == null) {
            return null;
        }
        List<KeyValue> keyValueList = getContentValues(tableInfo, entity);

        StringBuilder sql = new StringBuilder(120);
        sql.append("UPDATE ");
        sql.append(tableInfo.getTableName());
        sql.append(" SET ");

        int setValuesSize = keyValueList.size();
        int bindArgsSize = (whereArgs == null) ? setValuesSize : (setValuesSize + whereArgs.length);
        Object[] bindArgs = new Object[bindArgsSize];

        int i = 0;
        for (KeyValue keyValue : keyValueList) {
            String colName = keyValue.getKey();
            Object arg = keyValue.getValue();

            sql.append((i > 0) ? "," : "");
            sql.append(colName);
            sql.append("=?");
            bindArgs[i++] = arg;
        }
        if (whereArgs != null) {
            for (i = setValuesSize; i < bindArgsSize; i++) {
                bindArgs[i] = whereArgs[i - setValuesSize];
            }
        }
        if (!TextUtils.isEmpty(whereClause)) {
            sql.append(" WHERE ");
            sql.append(whereClause);
        }

        SqlInfo info = new SqlInfo();
        info.setSql(sql.toString());
        info.setBindArgs(bindArgs);
        return info;
    }

    /**
     * 构建查询sql语句
     */
    public static SqlInfo buildQuerySql(
            TableInfo tableInfo, boolean distinct, String[] columns, String where, String[] whereArgs,
            String groupBy, String having, String orderBy, String limit) {
        String sql = SQLiteQueryBuilder.buildQueryString(distinct, tableInfo.getTableName(), columns, where, groupBy, having, orderBy, limit);

        SqlInfo info = new SqlInfo();
        info.setSql(sql);
        info.setBindArgs(whereArgs);
        return info;
    }

    private static List<KeyValue> getContentValues(TableInfo tableInfo, Object entity) {
        if (tableInfo == null) {
            return null;
        }
        String colName;
        Object values;
        Object defValues;
        List<KeyValue> keyValueList = new ArrayList<KeyValue>();

        Property id = tableInfo.getId();
        Class<?> type = id.getDataType();
        //用了非自增长,添加id , 采用自增长就不需要添加id了
        if (!(type.equals(int.class) || type.equals(Integer.class)
                || type.equals(long.class) || type.equals(Long.class))) {
            colName = id.getColumn();
            values = id.getValue(entity);
            defValues = id.getDefaultVal();
            if (colName != null && colName.trim().length() != 0) {
                KeyValue keyValue = new KeyValue();
                keyValue.setKey(colName);
                if (values == null && defValues != null) {
                    keyValue.setValue(defValues);
                } else {
                    keyValue.setValue(values);
                }
                keyValueList.add(keyValue);
            }
        }

        //添加属性
        Collection<Property> properties = tableInfo.propertyMap.values();
        for (Property property : properties) {
            colName = property.getColumn();
            values = property.getValue(entity);
            defValues = property.getDefaultVal();
            if (colName != null && colName.trim().length() != 0) {
                KeyValue keyValue = new KeyValue();
                keyValue.setKey(colName);
                if (values == null && defValues != null) {
                    keyValue.setValue(defValues);
                } else {
                    keyValue.setValue(values);
                }
                keyValueList.add(keyValue);
            }

        }
        return keyValueList;
    }

}

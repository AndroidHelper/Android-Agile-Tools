package cn.core.mobile.library.database.sqlite;

/**
 * Created by dallas on 14-4-20.
 */
public class SqlInfo {
    private String sql;
    private Object[] bindArgs;


    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getBindArgs() {
        return bindArgs;
    }

    public void setBindArgs(Object[] bindArgs) {
        this.bindArgs = bindArgs;
    }
}

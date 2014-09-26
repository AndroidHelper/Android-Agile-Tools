package cn.core.mobile.library.database;

import android.content.Context;

/**
 * 数据库配置类
 * Created by dallas on 14-4-10.
 */
public class DBConfig {
    final boolean debug;
    final Context context;
    final String dbName;
    final String dbPath;
    final int dbVersion;

    private DBConfig(final Builder builder) {
        this.debug = builder.debug;
        this.context = builder.context;
        this.dbName = builder.dbName;
        this.dbPath = builder.dbPath;
        this.dbVersion = builder.dbVersion;
    }

    public static DBConfig createDefault(Context context) {
        return new Builder(context).build();
    }

    public static class Builder {
        /**
         * 调试模式,增删改查的时候显示SQL语句
         */
        private boolean debug = false;

        /**
         * android设备上下文
         */
        private Context context;
        /**
         * 数据库名字
         */
        private String dbName = "dbhelper.db";
        /**
         * 数据库版本
         */
        private int dbVersion = 1;
        /**
         * 创建数据库文件的路径
         */
        private String dbPath;

        public Builder(Context context) {
            this.context = context.getApplicationContext();
        }

        public Builder dbName(String name) {
            this.dbName = name;
            return this;
        }

        public Builder dbVersion(int version) {
            this.dbVersion = version;
            return this;
        }

        public Builder dbPath(String path) {
            this.dbPath = path;
            return this;
        }

        public Builder debug(boolean isDebug) {
            this.debug = isDebug;
            return this;
        }

        public DBConfig build() {
            return new DBConfig(this);
        }
    }
}

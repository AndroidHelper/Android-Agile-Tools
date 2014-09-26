package cn.core.mobile.library.database.exception;

/**
 * Created by dallas on 14-4-10.
 */
public class DbException extends RuntimeException {

    public DbException() {
        super();
    }

    public DbException(String detailMessage) {
        super(detailMessage);
    }

    public DbException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
    }

    public DbException(Throwable throwable) {
        super(throwable);
    }
}

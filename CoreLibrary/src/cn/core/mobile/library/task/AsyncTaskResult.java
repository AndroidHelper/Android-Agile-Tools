
package cn.core.mobile.library.task;

import java.io.Serializable;

/**
 * @author Dallas
 * @version 1.0
 */
public final class AsyncTaskResult implements Serializable {
    public static final int   CODE_ERROR       = -1;
    public static final int   CODE_SUCCESS     = 0;
    public static final int   CODE_FAILED      = 1;
    public static final int   CODE_CANCELED    = 2;

    private static final long serialVersionUID = 4195237447592568873L;
    public final int          code;
    public final String       message;
    public final Object       content;

    public AsyncTaskResult(final int code) {
        this(code, null, null);
    }

    public AsyncTaskResult(final int code, final String message) {
        this(code, message, null);
    }

    public AsyncTaskResult(final int code, final String message, final Object content) {
        this.code = code;
        this.message = message;
        this.content = content;
    }

}

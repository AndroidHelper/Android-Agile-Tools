
package cn.core.mobile.library.exception;

/**
 * 数据库异常
 */
public class DaoException extends BaseException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4590004583236497337L;

    /**
     * @param s
     */
    public DaoException(String s) {
        super(s);
    }

    /**
     * Creates a new JsonConversionException object.
     * 
     * @param message
     * @param cause
     */
    public DaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public DaoException(int exceptionCode, String detailMessage, Throwable throwable) {
        super(exceptionCode, detailMessage, throwable);
        // TODO Auto-generated constructor stub
    }

    public DaoException(int exceptionCode, String detailMessage) {
        super(exceptionCode, detailMessage);
        // TODO Auto-generated constructor stub
    }

}

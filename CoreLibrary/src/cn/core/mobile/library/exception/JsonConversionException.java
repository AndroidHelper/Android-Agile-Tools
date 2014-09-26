
package cn.core.mobile.library.exception;

/**
 * Json转化异常
 */
public class JsonConversionException extends BaseException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4590004583236497337L;

    /**
     * @param s
     */
    public JsonConversionException(String s) {
        super(s);
    }

    /**
     * Creates a new JsonConversionException object.
     * 
     * @param message
     * @param cause
     */
    public JsonConversionException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonConversionException(int exceptionCode, String detailMessage, Throwable throwable) {
        super(exceptionCode, detailMessage, throwable);
        // TODO Auto-generated constructor stub
    }

    public JsonConversionException(int exceptionCode, String detailMessage) {
        super(exceptionCode, detailMessage);
        // TODO Auto-generated constructor stub
    }

}

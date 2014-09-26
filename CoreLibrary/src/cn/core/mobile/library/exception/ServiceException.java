/**
 *
 */

package cn.core.mobile.library.exception;

/**
 * 服务异常
 *
 * @author Richard.Ma
 */
public class ServiceException extends BaseException {

    /**
     *
     */
    private static final long serialVersionUID = -1442439299450146708L;

    /**
     * Creates a new SelectorException object.
     *
     * @param s
     */
    public ServiceException(String s) {
        super(s);
    }

    /**
     * Creates a new SelectorException object.
     *
     * @param s
     * @param throwable
     */
    public ServiceException(String s, Throwable throwable) {
        super(s, throwable);
    }

    /**
     * Creates a new SelectorException object.
     *
     * @param throwable
     */
    public ServiceException(Throwable throwable) {
        super(throwable);
    }

    public ServiceException(int exceptionCode, String detailMessage, Throwable throwable) {
        super(exceptionCode, detailMessage, throwable);
        // TODO Auto-generated constructor stub
    }

    public ServiceException(int exceptionCode, String detailMessage) {
        super(exceptionCode, detailMessage);
        // TODO Auto-generated constructor stub
    }

}

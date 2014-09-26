
package cn.core.mobile.library.exception;

/**
 * 远程调用失败异常类
 *
 * @author Richard.Ma
 */
public class ClientException extends BaseException {

    /**
     *
     */
    private static final long serialVersionUID = 4590004583236497337L;

    /**
     * @param s
     */
    public ClientException(String s) {
        super(s);
    }

    /**
     * Creates a new RpcFailureException object.
     *
     * @param message
     * @param cause
     */
    public ClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public ClientException(int exceptionCode, String detailMessage, Throwable throwable) {
        super(exceptionCode, detailMessage, throwable);
        // TODO Auto-generated constructor stub
    }

    public ClientException(int exceptionCode, String detailMessage) {
        super(exceptionCode, detailMessage);
        // TODO Auto-generated constructor stub
    }

}

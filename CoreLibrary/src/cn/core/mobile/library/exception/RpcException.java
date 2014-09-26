
package cn.core.mobile.library.exception;

/**
 * 远程调用失败异常类
 * 
 * @author Richard.Ma
 */
public class RpcException extends BaseException {

    /**
	 * 
	 */
    private static final long serialVersionUID = 4590004583236497337L;

    /**
     * @param s
     */
    public RpcException(String s) {
        super(s);
    }

    /**
     * Creates a new RpcFailureException object.
     * 
     * @param message
     * @param cause
     */
    public RpcException(String message, Throwable cause) {
        super(message, cause);
    }

    public RpcException(int exceptionCode, String detailMessage, Throwable throwable) {
        super(exceptionCode, detailMessage, throwable);
        // TODO Auto-generated constructor stub
    }

    public RpcException(int exceptionCode, String detailMessage) {
        super(exceptionCode, detailMessage);
        // TODO Auto-generated constructor stub
    }

}

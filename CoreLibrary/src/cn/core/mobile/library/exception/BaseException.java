
package cn.core.mobile.library.exception;

/**
 * @author Will.Wu
 * @ClassName: BaseException
 * @Description: TODO
 * @date 2013年12月13日 下午2:51:57
 */
public class BaseException extends Exception {
    /**
     *
     */
    private static final long serialVersionUID = -5538307166866038382L;
    private int mExceptionCode = 0;

    public BaseException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        // TODO Auto-generated constructor stub
    }

    public BaseException(int exceptionCode, String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        // TODO Auto-generated constructor stub
        mExceptionCode = exceptionCode;
    }

    public BaseException(String detailMessage) {
        super(detailMessage);
        // TODO Auto-generated constructor stub
    }

    public BaseException(int exceptionCode, String detailMessage) {
        super(detailMessage);
        // TODO Auto-generated constructor stub
        mExceptionCode = exceptionCode;
    }

    public BaseException(Throwable throwable) {
        super(throwable);
        // TODO Auto-generated constructor stub
    }

    public int getCode() {
        return mExceptionCode;
    }
}

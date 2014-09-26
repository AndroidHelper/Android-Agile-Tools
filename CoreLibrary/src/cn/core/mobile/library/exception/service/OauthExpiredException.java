/**
 * 
 */
package cn.core.mobile.library.exception.service;

import cn.core.mobile.library.exception.ServiceException;

/**
 * SNS 授权失效异常
 * 
 * @author Richard.Ma
 * 
 */
public class OauthExpiredException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1442439299450146708L;


	/**
	 * Creates a new SelectorException object.
	 * 
	 * @param s
	 */
	public OauthExpiredException(String s) {
		super(s);
	}

	/**
	 * Creates a new SelectorException object.
	 * 
	 * @param s
	 * @param throwable
	 */
	public OauthExpiredException(String s, Throwable throwable) {
		super(s, throwable);
	}

	/**
	 * Creates a new SelectorException object.
	 * 
	 * @param throwable
	 */
	public OauthExpiredException(Throwable throwable) {
		super(throwable);
	}
}

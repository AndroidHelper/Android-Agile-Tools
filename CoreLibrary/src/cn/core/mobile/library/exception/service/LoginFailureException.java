/**
 * 
 */
package cn.core.mobile.library.exception.service;

import cn.core.mobile.library.exception.ServiceException;

/**
 * 登录失效异常
 * 
 * @author Richard.Ma
 * 
 */
public class LoginFailureException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1442439299450146708L;


	/**
	 * Creates a new SelectorException object.
	 * 
	 * @param s
	 */
	public LoginFailureException(String s) {
		super(s);
	}

	/**
	 * Creates a new SelectorException object.
	 * 
	 * @param s
	 * @param throwable
	 */
	public LoginFailureException(String s, Throwable throwable) {
		super(s, throwable);
	}

	/**
	 * Creates a new SelectorException object.
	 * 
	 * @param throwable
	 */
	public LoginFailureException(Throwable throwable) {
		super(throwable);
	}
}

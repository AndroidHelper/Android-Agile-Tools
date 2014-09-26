/**
 * 
 */
package cn.core.mobile.library.exception.service;

import cn.core.mobile.library.exception.ServiceException;

/**
 * 访问拒绝异常
 * 
 * @author Richard.Ma
 * 
 */
public class AccessDniedException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1442439299450146708L;


	/**
	 * Creates a new SelectorException object.
	 * 
	 * @param s
	 */
	public AccessDniedException(String s) {
		super(s);
	}

	/**
	 * Creates a new SelectorException object.
	 * 
	 * @param s
	 * @param throwable
	 */
	public AccessDniedException(String s, Throwable throwable) {
		super(s, throwable);
	}

	/**
	 * Creates a new SelectorException object.
	 * 
	 * @param throwable
	 */
	public AccessDniedException(Throwable throwable) {
		super(throwable);
	}
}

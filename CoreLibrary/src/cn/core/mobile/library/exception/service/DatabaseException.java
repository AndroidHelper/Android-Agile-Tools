/**
 * 
 */
package cn.core.mobile.library.exception.service;

import cn.core.mobile.library.exception.ServiceException;

/**
 * 数据库访问操作异常
 * 
 * @author Richard.Ma
 * 
 */
public class DatabaseException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1442439299450146708L;


	/**
	 * Creates a new SelectorException object.
	 * 
	 * @param s
	 */
	public DatabaseException(String s) {
		super(s);
	}

	/**
	 * Creates a new SelectorException object.
	 * 
	 * @param s
	 * @param throwable
	 */
	public DatabaseException(String s, Throwable throwable) {
		super(s, throwable);
	}

	/**
	 * Creates a new SelectorException object.
	 * 
	 * @param throwable
	 */
	public DatabaseException(Throwable throwable) {
		super(throwable);
	}
}

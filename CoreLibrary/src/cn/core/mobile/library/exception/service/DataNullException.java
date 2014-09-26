/**
 * 
 */
package cn.core.mobile.library.exception.service;

import cn.core.mobile.library.exception.ServiceException;

/**
 * 必须数据不能为空异常
 * 
 * @author Richard.Ma
 * 
 */
public class DataNullException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1442439299450146708L;


	/**
	 * Creates a new SelectorException object.
	 * 
	 * @param s
	 */
	public DataNullException(String s) {
		super(s);
	}

	/**
	 * Creates a new SelectorException object.
	 * 
	 * @param s
	 * @param throwable
	 */
	public DataNullException(String s, Throwable throwable) {
		super(s, throwable);
	}

	/**
	 * Creates a new SelectorException object.
	 * 
	 * @param throwable
	 */
	public DataNullException(Throwable throwable) {
		super(throwable);
	}
}

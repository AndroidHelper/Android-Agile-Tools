package cn.core.mobile.library.remote.response;

import java.io.Serializable;

/**
 * @author Will.Wu </br> Create at 2014年3月25日 下午4:20:38
 * @version 1.0
 */
public class Response implements Serializable {

	private static final long serialVersionUID = -4916688580556445457L;
	/** 错误代码 */
	private String code = null;

	private String msg = null;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}

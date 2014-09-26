
package cn.core.mobile.library.remote.response;

/**
 * Json对象
 * 
 * @author Richard.Ma
 */
public class EntityResponse<T> extends Response {

    private static final long serialVersionUID = -7203786896447223056L;

    private T                 result;

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

    
}

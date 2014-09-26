
package cn.core.mobile.library.remote.response;

import java.util.List;

/**
 * 列表Json对象
 * 
 * @author Richard.Ma
 */
public class ListResponse<T> extends Response {

    private static final long serialVersionUID = -7203786896447223056L;

    private List<T>           items;

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
    }
}

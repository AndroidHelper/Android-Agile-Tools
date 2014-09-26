
package cn.core.mobile.library.remote.response;

import java.io.Serializable;

public class Entity implements Serializable {

    private static final long serialVersionUID = -1704800382569106413L;
    private long _id;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public long get_id() {
        return _id;
    }

    public void set_id(long _id) {
        this._id = _id;
    }
}

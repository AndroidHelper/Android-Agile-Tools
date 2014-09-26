/**
 * 项目名称: CoreLibrary
 * 文件名称: ListEntityResponseCallback.java
 * 创建时间: 2014年3月26日
 * Copyright: 2014 www.fantasee.cn Inc. All rights reserved. 
 */

package cn.core.mobile.library.http.callback;

import cn.core.mobile.library.remote.response.ListResponse;

/**
 * @author Will.Wu </br> Create at 2014年3月26日 上午10:23:39
 * @version 1.0
 */
public interface ListResponseCallback<T> {
	//void onCompleted(Throwable e, long updateTime, ListResponse<T> result);
    void onCompleted(Throwable e,ListResponse<T> result);
}

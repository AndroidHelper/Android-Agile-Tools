/**
 * 项目名称: CoreLibrary
 * 文件名称: PageEntityResponseCallback.java
 * 创建时间: 2014年4月2日
 * Copyright: 2014 www.fantasee.cn Inc. All rights reserved. 
 */

package cn.core.mobile.library.http.callback;

import cn.core.mobile.library.remote.response.PageResponse;

/**
 * @author Will.Wu </br> Create at 2014年4月2日 上午11:05:11
 * @version 1.0
 */
public interface PageEntityResponseCallback<T> {
	// void onCompleted(Throwable e, long updateTime, PageResponse<T> result);
    void onCompleted(Throwable e,PageResponse<T> result);
}

/**
 * 项目名称: CoreLibrary
 * 文件名称: EntityResponseCallback.java
 * 创建时间: 2014年3月26日
 * Copyright: 2014 www.fantasee.cn Inc. All rights reserved. 
 */

package cn.core.mobile.library.http.callback;

import cn.core.mobile.library.remote.response.EntityResponse;

/**
 * @author Will.Wu </br> Create at 2014年3月26日 上午10:23:11 \
 * @version 1.0
 */
public interface EntityResponseCallback<T> {
// void onCompleted(Throwable e, long updateTime, EntityResponse<T> result);
    void onCompleted(Throwable e, EntityResponse<T> result);
}

/**
 * 项目名称: CoreLibrary
 * 文件名称: BaseResponseCallback.java
 * 创建时间: 2014/4/17
 * Copyright: 2014 www.fantasee.cn Inc. All rights reserved.
 */
package cn.core.mobile.library.http.callback;


/**
 * @author Wu.Will </br> Create at 2014年4月17日 上午11:23:11 \
 * @version 1.0
 */
public interface BaseResponseCallback<T> {
    void onCompleted(Throwable e, T result);
}

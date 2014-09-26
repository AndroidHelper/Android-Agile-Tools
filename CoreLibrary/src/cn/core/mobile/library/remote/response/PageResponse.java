/**
 * 项目名称: CoreLibrary
 * 文件名称: PageResponse.java
 * 创建时间: 2014年4月2日
 * Copyright: 2014 www.fantasee.cn Inc. All rights reserved. 
 */

package cn.core.mobile.library.remote.response;

/**
 * 分页json对象
 * 
 * @author Will.Wu </br> Create at 2014年4月2日 上午9:59:42
 * @version 1.0
 */
public class PageResponse<T> extends Response {

    /**
     * 
     */
    private static final long serialVersionUID = -6946364507434105403L;

    private PageEntity<T>     page;

    public PageEntity<T> getPage() {
        return page;
    }

    public void setPage(PageEntity<T> page) {
        this.page = page;
    }

}

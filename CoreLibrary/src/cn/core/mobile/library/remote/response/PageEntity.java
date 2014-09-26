/**
 * 项目名称: CoreLibrary
 * 文件名称: PageEntity.java
 * 创建时间: 2014年4月2日
 * Copyright: 2014 www.fantasee.cn Inc. All rights reserved. 
 */

package cn.core.mobile.library.remote.response;

import java.util.List;

/**
 * @author Will.Wu </br> Create at 2014年4月2日 上午10:05:09
 * @version 1.0
 */
public class PageEntity<T> extends Entity {

    /**
     * 
     */
    private static final long serialVersionUID = 3314116356683700522L;
    private int               totalCount;
    private int               pageSize;
    private int               pageNo;
    private List<T>           list;
    private int               firstResult;
    private int               nextPage;
    private boolean           lastPage;
    private boolean           firstPage;
    private int               totalPage;
    private int               prePage;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getFirstResult() {
        return firstResult;
    }

    public void setFirstResult(int firstResult) {
        this.firstResult = firstResult;
    }

    public int getNextPage() {
        return nextPage;
    }

    public void setNextPage(int nextPage) {
        this.nextPage = nextPage;
    }

    public boolean isLastPage() {
        return lastPage;
    }

    public void setLastPage(boolean lastPage) {
        this.lastPage = lastPage;
    }

    public boolean isFirstPage() {
        return firstPage;
    }

    public void setFirstPage(boolean firstPage) {
        this.firstPage = firstPage;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getPrePage() {
        return prePage;
    }

    public void setPrePage(int prePage) {
        this.prePage = prePage;
    }

}

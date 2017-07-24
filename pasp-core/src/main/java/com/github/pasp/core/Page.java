package com.github.pasp.core;

import java.io.Serializable;
import java.util.List;

public class Page<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final int DEFAULT_PAGE_SIZE = 20;
    public static final int DEFAULT_PAGE_INDEX = 1;
    // 当前页
    private int pageIndex = DEFAULT_PAGE_INDEX;
    // 每页的数量
    private int pageSize = DEFAULT_PAGE_SIZE;
    // 当前页的数量
    private int size;

    private int total;
    // 总页数
    private int pages;
    // 结果集
    private List<T> list;

    public Page() {

    }

    public Page(List<T> list, int pageIndex, int pageSize, int totalCounts) {
        this.list = list;
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.size = list == null ? 0 : list.size();
        this.total = totalCounts;
        this.total = totalCounts / pageSize;
        if ((totalCounts % pageSize) > 0) {
            this.total += 1;
        }
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getSize() {
        return size;
    }

    public long getTotal() {
        return total;
    }

    public long getPages() {
        return pages;
    }

    public List<T> getList() {
        return list;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Page{");
        sb.append("pageIndex=").append(pageIndex);
        sb.append(", pageSize=").append(pageSize);
        sb.append(", size=").append(size);
        sb.append(", total=").append(total);
        sb.append(", pages=").append(pages);
        sb.append(", list=").append(list);
        sb.append('}');
        return sb.toString();
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public void setPages(int totalPages) {
        this.pages = totalPages;
    }

    public void setTotal(int totalCounts) {
        this.total = totalCounts;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

}

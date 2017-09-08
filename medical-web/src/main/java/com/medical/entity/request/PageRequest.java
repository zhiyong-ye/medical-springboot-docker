package com.medical.entity.request;

import java.io.Serializable;

/**
 * PageRequest 分页请求参数
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年8月14日 上午10:18:36
 * @version 1.0
 */
public class PageRequest implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -8513250100509044437L;

    /**
     * 当前页
     */
    private Integer pageNum = 0;
    
    /**
     * 每页大小
     */
    private Integer pageSize = 20;

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "PageRequest [pageNum=" + pageNum + ", pageSize=" + pageSize + "]";
    }
    
}

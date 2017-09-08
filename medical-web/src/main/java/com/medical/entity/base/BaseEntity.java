package com.medical.entity.base;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

/**
 * BaseEntity 数据库映射实体父类
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年8月11日 下午6:00:10
 * @version 1.0
 */
public class BaseEntity implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 864977845455742430L;

    /**
     * 主键
     */
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 分页插件:参数方式需要设置supportMethodsArguments，params,非数据库字段，需加@Transient注解
     * 当前页
     */
    @Transient
    private Integer pageNum;
    
    /**
     * 每页大小
     */
    @Transient
    private Integer pageSize;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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
    
}

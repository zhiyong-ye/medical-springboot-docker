package com.medical.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.medical.entity.base.BaseEntity;

/**
 * User 用户表映射实体
 *
 * @author ZHIYONG.YE
 * @email 773276516@qq.com
 * @date 2017年7月28日 上午10:09:58
 * @version 1.0
 */
public class User extends BaseEntity implements Serializable {
    
    /**
     * 
     */
    private static final long serialVersionUID = -9202714206013174639L;


    
    @Column(name = "USER_NAME")
    private String username;
    
    @Column(name = "PASSWORD")
    private String password;
    
    /**
     * 不是表中字段的属性必须加 @Transient 注解 
     */
    @Transient
    private Integer page = 1;
    
    @Transient
    private Integer rows = 20;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }
    
}

package com.zjrb.sjzsw.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Thinkpad on 2017/11/3.
 */
@Entity
public class GreenDaoTest2 {
    @Id
    private Long id;
    private String name;
    private Long pid;
    @Generated(hash = 1737271187)
    public GreenDaoTest2(Long id, String name, Long pid) {
        this.id = id;
        this.name = name;
        this.pid = pid;
    }
    @Generated(hash = 801573289)
    public GreenDaoTest2() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Long getPid() {
        return this.pid;
    }
    public void setPid(Long pid) {
        this.pid = pid;
    }
}

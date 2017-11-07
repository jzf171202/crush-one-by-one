package com.zjrb.sjzsw.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by Thinkpad on 2017/11/3.
 */
@Entity
public class greenDaoUpdateTest {
    @Id
    private Long id;

    @Generated(hash = 1949956620)
    public greenDaoUpdateTest(Long id) {
        this.id = id;
    }

    @Generated(hash = 769340350)
    public greenDaoUpdateTest() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

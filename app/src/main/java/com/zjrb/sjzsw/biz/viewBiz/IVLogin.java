package com.zjrb.sjzsw.biz.viewBiz;

import com.zjrb.sjzsw.entity.LoginEntity;

/**
 * Created by jinzifu on 2018/4/12.
 */

public interface IVLogin extends IVBase {

    /**
     * 展示个人信息
     *
     * @param loginEntity
     */
    void showInfo(LoginEntity loginEntity);
}

package com.zjrb.sjzsw.entity;

import android.util.Log;

import com.zjrb.sjzsw.utils.ListUtil;

import java.util.List;

/**
 * 类描述：测试对象锁的类
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/12/8 1512
 */

public class SycnListEntity {
    private final String TAG = "SycnListEntity";
    private List<Integer> list;

    public SycnListEntity(List<Integer> list) {
        this.list = list;
    }

    public boolean removeItem() {
        if (!ListUtil.isListEmpty(list)) {
            list.remove(list.size() - 1);
            Log.d(TAG, "删除后的list.size = " + list.size());
            return true;
        }
        return false;
    }

    public  boolean addItem(int i) {
        if (list != null) {
            list.add(i);
            Log.d(TAG, "增加后的list.size = " + list.size());
            return true;
        }
        return false;
    }
}

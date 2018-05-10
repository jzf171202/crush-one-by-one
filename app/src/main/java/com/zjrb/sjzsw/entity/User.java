package com.zjrb.sjzsw.entity;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

/**
 * Created by jinzifu on 2018/4/29.
 */

public class User extends BaseObservable{
    private String account;
    private String password;

    public User(String account, String password) {
        this.account = account;
        this.password = password;
    }

    @Bindable
    public String getAccount() {
        return this.account;
    }


    public void setAccount(String account) {
        this.account = account;
        notifyPropertyChanged(BR.account);
    }

    @Bindable
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }
}

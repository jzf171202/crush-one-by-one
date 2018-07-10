package com.zjrb.sjzsw.widget.dialog;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * 类描述：
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/11/20 1102
 */

public abstract class BaseDialogFrament<T extends ViewDataBinding> extends DialogFragment {
    private Context context;
    protected T t;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        t = DataBindingUtil.inflate(inflater, getLayoutId(), null, false);
        initView(t.getRoot());
        return t.getRoot();
    }

    protected abstract void initView(View root);

    protected abstract int getLayoutId();
}

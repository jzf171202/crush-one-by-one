package com.jzf.net.ui;

import android.app.Dialog;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.jzf.net.R;

import io.reactivex.annotations.NonNull;


/**
 * Created by ASUS on 2017/11/16.
 */

public class LoadingDialog extends Dialog {


    ImageView ivLoading;
    private Animation rotateAnimation;

    public LoadingDialog(@NonNull Context context) {
        this(context, R.style.TransparentDialog);
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    private void initView(Context context) {
        setCancelable(false);
        setContentView(R.layout.dialog_loading);
        ivLoading = findViewById(R.id.iv_loading);
        rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_always);
        rotateAnimation.setInterpolator(new LinearInterpolator());
    }

    @Override
    public void show() {
        super.show();
        ivLoading.startAnimation(rotateAnimation);
    }

    @Override
    public void dismiss() {
        ivLoading.clearAnimation();
        super.dismiss();
    }
}

package com.zjrb.sjzsw.widget.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import com.zjrb.sjzsw.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 类描述：
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/11/20 1102
 */

public class CustomDialogFrament extends DialogFragment {
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    Unbinder unbinder;
    private Animation rotateAnimation;
    private Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_dialog, null);
        rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_always);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        ivLoading.startAnimation(rotateAnimation);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void dismiss() {
        if (null != ivLoading) {
            ivLoading.clearAnimation();
        }
        super.dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

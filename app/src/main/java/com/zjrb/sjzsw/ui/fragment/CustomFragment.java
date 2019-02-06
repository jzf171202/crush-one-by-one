package com.zjrb.sjzsw.ui.fragment;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Toast;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.databinding.FrCustomLayoutBinding;

public class CustomFragment extends BaseFragment<FrCustomLayoutBinding> {
    @Override
    protected int getLayoutId() {
        return R.layout.fr_custom_layout;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void initView(View root) {
//        ImageUtil.decodeBitmapFromResource(
//                getResources(),
//                R.drawable.icon_simple,
//                100,
//                100);

        t.test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1.采用scrollTo/scrollBy实现滑动
                // t.myView.smoothScrollTo(-500,-500);

                //2.采用动画实现滑动
                ObjectAnimator.ofFloat(t.ok, "translationX", 0, 500)
                        .setDuration(2000)
                        .start();

                //3.设置布局参数实现滑动
//                ViewGroup.MarginLayoutParams params =
//                        (ViewGroup.MarginLayoutParams) t.ok.getLayoutParams();
//                params.leftMargin += 5;
//                t.ok.requestLayout();
//                t.ok.setLayoutParams(params);
            }
        });
        t.ok.setOnClickListener(v -> Toast.makeText(getContext(),"哈哈",Toast.LENGTH_SHORT).show());
    }
}

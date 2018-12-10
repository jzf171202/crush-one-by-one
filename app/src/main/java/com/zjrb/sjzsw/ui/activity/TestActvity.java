package com.zjrb.sjzsw.ui.activity;

import android.os.Bundle;
import android.view.View;

import com.yuyh.library.EasyGuide;
import com.yuyh.library.support.HShape;
import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.databinding.AcTestBinding;

public class TestActvity extends BaseActivity<AcTestBinding> implements View.OnClickListener {
    private EasyGuide guide;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_test;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        t.test.setOnClickListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            showGuide();
        }
    }

    private void showGuide() {
        int[] loccation = new int[2];
        t.test.getLocationOnScreen(loccation);
        if (guide != null && guide.isShowing()) {
            guide.dismiss();
        }
        guide = new EasyGuide.Builder(this)
                .addHightArea(t.test, HShape.CIRCLE)
                .addIndicator(R.drawable.guide_line, loccation[0]+t.test.getWidth(), loccation[1] + t.test.getHeight())
                .dismissAnyWhere(false)
                .setPositiveButton("知道了", 15, this)
                .build();
        guide.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test:
                showGuide();
                break;
            default:
                guide.dismiss();
                break;
        }
    }
}

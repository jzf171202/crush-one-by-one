package com.zjrb.sjzsw.widget.animation;

import android.animation.TimeInterpolator;
import android.util.Log;

public class PathAnimInterpolator implements TimeInterpolator {
    @Override
    public float getInterpolation(float input) {
        //动画开始时，input值 = 0；动画结束时input = 1

        //实现先减速后加速的效果
        float result;
        if (input <= 0.5) {
            result = (float) (Math.sin(Math.PI * input)) / 2;
        } else {
            result = (float) (2 - Math.sin(Math.PI * input)) / 2;
        }

        Log.d("TestActvity", "result=" + result);
        return result;
    }
}

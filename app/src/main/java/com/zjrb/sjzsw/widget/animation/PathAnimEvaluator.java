package com.zjrb.sjzsw.widget.animation;

import android.animation.TypeEvaluator;

import com.zjrb.sjzsw.model.PointModel;

/**
 * 路径估值器
 */
public class PathAnimEvaluator implements TypeEvaluator<PointModel> {
    PointModel pointModel = new PointModel(0f, 0f);

    @Override
    public PointModel evaluate(float fraction, PointModel startValue, PointModel endValue) {
        float x = startValue.getX() + fraction * (endValue.getX() - startValue.getX());
        float y = startValue.getY() + fraction * (endValue.getY() - startValue.getY());

        pointModel.setX(x);
        pointModel.setY(y);
        return pointModel;//避免OOM
    }
}

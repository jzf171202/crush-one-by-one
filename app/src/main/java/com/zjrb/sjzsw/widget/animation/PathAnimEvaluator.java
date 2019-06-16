package com.zjrb.sjzsw.widget.animation;

import android.animation.TypeEvaluator;

import com.zjrb.sjzsw.model.PointModel;

/**
 * 路径估值器
 */
public class PathAnimEvaluator implements TypeEvaluator<PointModel> {
    PointModel pointModel = null;

    public PathAnimEvaluator(PointModel pointModel) {
        this.pointModel = pointModel;
    }

    /**
     * 动画执行算法
     *
     * @param fraction   表示动画完成度，属性改变的百分比
     * @param startValue 动画初始值
     * @param endValue   动画结束值
     * @return 当前动画过渡值
     */
    @Override
    public PointModel evaluate(float fraction, PointModel startValue, PointModel endValue) {

        float x = startValue.getX() + fraction * (endValue.getX() - startValue.getX());
        float y = startValue.getY() + fraction * (endValue.getY() - startValue.getY());
//        Log.d("TestActvity", "fraction=" + fraction);//fraction < 1

        pointModel.setX(x);
        pointModel.setY(y);
        return pointModel;//避免造成非常多的内存碎片
    }
}

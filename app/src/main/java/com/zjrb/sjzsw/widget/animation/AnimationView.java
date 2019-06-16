package com.zjrb.sjzsw.widget.animation;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;

import com.zjrb.sjzsw.model.PointModel;

public class AnimationView extends View {
    private final float RADIUS = 100f;

    //属性名
    private PointModel pointModel;
    private Paint paint;

    public AnimationView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public AnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public AnimationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        paint = new Paint();
        paint.setColor(Color.RED);
    }

    public PointModel getPointModel() {
        Log.d("TestActvity", "getPointModel");
        return pointModel;
    }

    public void setPointModel(PointModel pointModel) {
        this.pointModel = pointModel;
        invalidate();
        Log.d("TestActvity", "setPointModel="+pointModel.getY());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (pointModel == null) {
            pointModel = new PointModel(RADIUS, RADIUS);
            canvas.drawCircle(pointModel.getX(), pointModel.getY(), RADIUS, paint);
            startAnimation();
        } else {
            canvas.drawCircle(pointModel.getX(), pointModel.getY(), RADIUS, paint);
        }
    }

    private void startAnimation() {
        PointModel startPoint = new PointModel(RADIUS, RADIUS);
        ObjectAnimator translate = ObjectAnimator.ofObject(this,
                "pointModel",
                new PathAnimEvaluator(startPoint),
                startPoint, new PointModel(RADIUS, getHeight() - RADIUS));

        translate.setDuration(10000);
        translate.setInterpolator(new BounceInterpolator());
        translate.start();
    }
}

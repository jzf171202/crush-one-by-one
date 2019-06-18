package com.zjrb.sjzsw.widget.animation;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
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
        return pointModel;
    }

    public void setPointModel(PointModel pointModel) {
        this.pointModel = pointModel;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (pointModel == null) {
            pointModel = new PointModel(getWidth() / 2, RADIUS);
            startAnimation();
        }
        canvas.drawCircle(pointModel.getX(), pointModel.getY(), RADIUS, paint);
    }

    public void startAnimation() {
        if (pointModel != null){
            pointModel.setY(RADIUS);
            pointModel.setX(getWidth()/2);
        }
        ObjectAnimator translate = ObjectAnimator.ofObject(this,
                "pointModel",
                new PathAnimEvaluator(),
                new PointModel(getWidth() / 2, RADIUS),
                new PointModel(getWidth() / 2, getHeight() - RADIUS));

        translate.setDuration(5000);
        translate.setInterpolator(new BounceInterpolator());
        translate.start();
    }
}

package com.zjrb.sjzsw.widget;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * 测试自定义view
 */
public class MyView extends LinearLayout {
    private int touchSlop;
    private VelocityTracker velocityTracker;
    private GestureDetector gestureDetector;
    private Scroller scroller;

    public MyView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MyView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    /**
     * 自定义view初始配置
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        velocityTracker = VelocityTracker.obtain();
        scroller = new Scroller(context);
        gestureDetector = new GestureDetector(context,
                new GestureDetector.SimpleOnGestureListener() {
                    @Override
                    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                        return super.onFling(e1, e2, velocityX, velocityY);
                    }
                });
    }

    /**
     * 弹性滑动到指定位置
     *
     * @param x
     * @param y
     */
    public void smoothScrollTo(int x, int y) {
        //根据提供的初始位置、滑动距离和持续时间弹性滑动
        scroller.startScroll(getScrollX(), getScrollY(),
                x - getScrollX(), y - getScrollY(), 5000);
        invalidate();//使得view重绘
    }

    /**
     * Scroller类滑动原理(针对view内容的滑动)
     * 1.invalidate()会促使view的重绘；
     * 2.view的重绘会在draw方法中调用computeScroll();
     * 3.外部重写computeScroll()，通过scrollTo将view内容滑动到相应位置，
     * 并调用postInvalidate触发重绘，123循环执行；
     */
    @Override
    public void computeScroll() {
        super.computeScroll();
        //类似插值器，true表示滑动未结束，继续滑动
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            Log.d("test","scroller.getCurrX() = "+scroller.getCurrX()+"; scroller.getCurrY()="+scroller.getCurrY());
            postInvalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        velocityTracker.addMovement(event);//速度追踪
        gestureDetector.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                //获取1000ms内的平均速度(从右向左滑动时，水平方向速度为负值)
                velocityTracker.computeCurrentVelocity(1000);
                int xVelocity = (int) velocityTracker.getXVelocity();
                Log.d("test", "xVelocity=" + xVelocity);
                break;
        }
        velocityTracker.clear();
        //velocityTracker.recycle();
        return super.onTouchEvent(event);
    }
}

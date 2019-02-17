package com.zjrb.sjzsw.widget;


import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.zjrb.sjzsw.utils.ScreenUtil;

/**
 * 测试自定义view
 * 假设item宽高是相等的
 */
public class MyView extends ViewGroup {
    Scroller scroller;
    int childWidth;
    VelocityTracker velocityTracker;
    int lastTouchX;
    int nearlyChildIndex;//偏移对应的最近item索引
    int lastInterceptX, lastInterceptY;
    int touchSlop;

    public MyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        scroller = new Scroller(context);
        velocityTracker = VelocityTracker.obtain();
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int parentWidth = 0, parentHeight = 0;

        int childrenNum = getChildCount();
        for (int i = 0; i < childrenNum; i++) {
            final View child = getChildAt(i);
            if (child == null || child.getVisibility() == GONE) continue;
            //测量子元素（含子元素内外边距）
            measureChildWithMargins(child, widthMeasureSpec, parentWidth,
                    heightMeasureSpec, 0);
            //根据子元素计算出父容器宽高期望值
            parentWidth += child.getMeasuredWidth();
            parentHeight = Math.max(child.getMeasuredHeight(), parentHeight);
        }
        //resloveSize()是api内部对不同测量模式下的测量值的获取方式优化并封装
        setMeasuredDimension(resolveSize(parentWidth, widthMeasureSpec),
                resolveSize(parentHeight, heightMeasureSpec));
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childrenNum = getChildCount();
        int resultLeft = getPaddingLeft();
        for (int j = 0; j < childrenNum; j++) {
            if (j == 0) {
                childWidth = getChildAt(0).getMeasuredWidth();
            }
            final View child = getChildAt(j);
            if (child == null || child.getVisibility() == GONE) continue;

            MarginLayoutParams params = (MarginLayoutParams) child.getLayoutParams();
            left = resultLeft + params.leftMargin;//横向列表 left为累加item宽度和左右间距
            top = params.topMargin + getPaddingTop();
            right = left + child.getMeasuredWidth();
            bottom = top + child.getMeasuredHeight();

            child.layout(left, top, right, bottom);
            resultLeft += params.leftMargin + child.getMeasuredWidth() + params.rightMargin;
        }
    }

    /**
     * 自定义view初始化时执行的是带AttributeSet的构造方法，
     * 所以支持Magin的generateLayoutParams方法需要是带AttributeSet。
     *
     * @param attrs
     * @return
     */
    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean isIntercepted = false;
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isIntercepted = false;
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                    isIntercepted = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = x - lastInterceptX;
                int offsetY = y - lastInterceptY;
                //横向滑动大于纵向滑动时 拦截事件
                if (Math.abs(offsetX) > Math.abs(offsetY) && Math.abs(offsetX) > touchSlop) {
                    isIntercepted = true;
                    //记录事件拦截时坐标
                    lastTouchX = x;
                } else {
                    isIntercepted = false;
                }
                break;
            case MotionEvent.ACTION_UP:
                isIntercepted = false;
                break;
        }
        lastInterceptX = x;
        lastInterceptY = y;
        return isIntercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        velocityTracker.addMovement(event);
        int touchX = (int) event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //ViewGroup的ACTION_DOWN事件默认不拦截，不在此捕获事件坐标，
                // 正确获取时机在ViewGroup开始拦截事件时。
                if (!scroller.isFinished()) {
                    scroller.abortAnimation();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int offsetX = touchX - lastTouchX;
                scrollBy(-offsetX, 0);  //滑动时偏移
                //滑动时同步校准临近child索引
                nearlyChildIndex = getScrollX() / childWidth;
                break;
            case MotionEvent.ACTION_UP:
                velocityTracker.computeCurrentVelocity(1000);
                int velocityX = (int) velocityTracker.getXVelocity(); //左负右正

                //粗调：滑动抬起时，找到最近的item的索引
                if (Math.abs(velocityX) >= childWidth / 2) {
                    nearlyChildIndex = velocityX > 0 ? nearlyChildIndex - 1 : nearlyChildIndex + 1;
                } else {
                    //计算出累计偏移量折算成item宽度个数(余数部分超过半个item宽度则+1，未超过为0)
                    nearlyChildIndex = (getScrollX() + childWidth / 2) / childWidth;
                }
                //微优化nearliestchildIndex取值
                nearlyChildIndex = Math.max(0, Math.min(nearlyChildIndex, getChildCount() - 1));
                //微调：滑动抬起时，偏移策略——1.临近item置左；2.最右item置右

                int scrollX;
                //当最右边的item完全可见时，最左边的item索引
                int resultIndex = getChildCount() - 1 - ScreenUtil.getScreenWidth() / childWidth;
                if (nearlyChildIndex >= resultIndex) {//左滑过头时
                    // 左滑过头时，确保最右边的item可见，强制为偏移在最左边的item索引
                    nearlyChildIndex = resultIndex;
                    //左边最近item置左后，确保最右边的item置右，需再偏移的量
                    int result = childWidth - ScreenUtil.getScreenWidth() % childWidth;
                    scrollX = nearlyChildIndex * childWidth - getScrollX() + result;
                } else {
                    //微调到最近item，并置左
                    scrollX = nearlyChildIndex * childWidth - getScrollX();
                }
                //偏移微调，左正右负
                smoothScrollBy(scrollX, 0);
                break;
        }
        velocityTracker.clear();
        lastTouchX = touchX;
        return super.onTouchEvent(event);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        velocityTracker.recycle();
    }

    public void smoothScrollBy(int x, int y) {
        scroller.startScroll(getScrollX(), getScrollY(), x, y, 500);
        invalidate();
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (scroller.computeScrollOffset()) {
            scrollTo(scroller.getCurrX(), scroller.getCurrY());
            postInvalidate();
        }
    }
}

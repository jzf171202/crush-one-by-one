package com.zjrb.sjzsw.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.SeekBar;

import com.zjrb.sjzsw.R;

/**
 * 类描述：
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/12/12 1626
 */

@SuppressLint("AppCompatCustomView")
public class IndicatorSeekBar extends SeekBar {
    /**
     * SeekBar数值文字颜色
     */
    private int mTextColor;
    /**
     * SeekBar数值文字大小
     */
    private float mTextSize;
    /**
     * SeekBar数值文字内容
     */
    private String mText;
    /**
     * 画笔
     */
    private Paint mPaint;
    /**
     * SeekBar数值文字方向
     */
    private int mTextOrientation;
    /**
     * SeekBar数值文字宽度
     */
    private float mTextWidth;
    /**
     * SeekBar数值文字基线Y坐标
     */
    private float mTextBaseLineY;

    /**
     * SeekBar数值文字背景宽高
     */
    private float mBgHeight = 31;
    private float mBgWidth = 91;
    //文字方向
    private static final int ORIENTATION_TOP = 1;
    private static final int ORIENTATION_BOTTOM = 2;

    public IndicatorSeekBar(Context context) {
        this(context, null);
    }

    public IndicatorSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.IndicatorSeekBar, defStyleAttr, 0);
        if (typedArray == null) {
            return;
        }
        int count = typedArray.getIndexCount();
        for (int i = 0; i < count; i++) {
            int index = typedArray.getIndex(i);
            switch (index) {
                case R.styleable.IndicatorSeekBar_textColor:
                    mTextColor = typedArray.getColor(index, Color.WHITE);
                    break;
                case R.styleable.IndicatorSeekBar_textSize:
                    mTextSize = typedArray.getDimension(index, 15f);
                    break;
                case R.styleable.IndicatorSeekBar_textOrientation:
                    mTextOrientation = typedArray.getInt(index, ORIENTATION_TOP);
                    break;
                default:
                    break;
            }
        }
        typedArray.recycle();

        //设置画笔及文字显示方向
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(mTextColor);
        mPaint.setTextSize(mTextSize);
        if (mTextOrientation == ORIENTATION_TOP) {
            setPadding((int) Math.ceil(mBgWidth) / 2, (int) Math.ceil(mBgHeight) + 5, (int) Math.ceil(mBgWidth) / 2, 0);
        } else {
            setPadding((int) Math.ceil(mBgWidth) / 2, 0, (int) Math.ceil(mBgWidth) / 2, (int) Math.ceil(mBgHeight) + 5);
        }
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        setBaseLine();

        Rect bgRect = getProgressDrawable().getBounds();
        //计算数值背景X坐标
        float bgX = bgRect.width() * getProgress() / getMax();
        //计算数值背景Y坐标
        float bgY = 0;
        if (mTextOrientation == ORIENTATION_BOTTOM) {
            bgY = mBgHeight + 10;
        }

        //计算数值文字X,Y坐标
        float textX = bgX + (mBgWidth - mTextWidth) / 2;
        float textY = (float) (mTextBaseLineY + bgY + (0.16 * mBgHeight / 2) - 10);
        canvas.drawText(mText, textX, textY, mPaint);
    }

    /**
     * 设置文字的显示位置
     */
    private void setBaseLine() {
        Paint.FontMetrics fm = mPaint.getFontMetrics();
        mText = "" + getProgress();
        //测量文字宽度
        mTextWidth = mPaint.measureText(mText);
        //计算文字基线Y坐标
        mTextBaseLineY = mBgHeight / 2 - fm.descent + (fm.descent - fm.ascent) / 2;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        invalidate();
        return super.onTouchEvent(event);
    }
}

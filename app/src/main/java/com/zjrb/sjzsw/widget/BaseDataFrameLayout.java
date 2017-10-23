package com.zjrb.sjzsw.widget;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 可赋值的自定义布局
 * tips：只可在自定义布局里写视图逻辑，不可写业务逻辑。
 *
 * @param <T>
 */
public abstract class BaseDataFrameLayout<T> extends BaseFrameLayout {
    public BaseDataFrameLayout(Context context) {
        super(context);
    }

    public BaseDataFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public abstract void update(T data);
}
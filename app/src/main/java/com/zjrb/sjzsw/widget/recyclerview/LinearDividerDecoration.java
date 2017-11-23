package com.zjrb.sjzsw.widget.recyclerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 类描述：纵向列表的分割线
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/11/22 1053
 */

public class LinearDividerDecoration extends RecyclerView.ItemDecoration {
    private int dividerHeight;
    private ColorDrawable divider;

    public LinearDividerDecoration(Context context, int height, int color) {
        divider = new ColorDrawable(context.getResources().getColor(color));
        this.dividerHeight = context.getResources().getDimensionPixelSize(height);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int lastPosition = state.getItemCount() - 1;
        int current = parent.getChildLayoutPosition(view);
        if (current == -1) {
            return;
        }
        //最后一行下面的分割线不显示
        if (current == lastPosition) {
            outRect.set(0, 0, 0, 0);
        } else {
            outRect.set(0, 0, 0, dividerHeight);
        }
    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        super.onDraw(c, parent, state);
        int childCount = parent.getChildCount();
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();
        for (int i = 0; i < childCount; i++) {
            View view = parent.getChildAt(i);
            if (view == null) {
                continue;
            }
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            int top = view.getBottom() + layoutParams.bottomMargin;
            int bottom = view.getBottom() + dividerHeight;
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}

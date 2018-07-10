package com.zjrb.sjzsw.widget.recyclerview.base;


/**
 * Created by zhy on 16/6/22.
 */
public interface ItemViewDelegate<T> {

    int getItemViewLayoutId();

    boolean isForViewType(T item, int position);

    void convert(MyViewHolder holder, T t, int position);

}

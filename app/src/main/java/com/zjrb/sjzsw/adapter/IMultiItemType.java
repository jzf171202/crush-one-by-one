package com.zjrb.sjzsw.adapter;

public interface IMultiItemType<T> {
    int getLayoutId(int itemType);

    int getItemViewType(int position, T t);
}

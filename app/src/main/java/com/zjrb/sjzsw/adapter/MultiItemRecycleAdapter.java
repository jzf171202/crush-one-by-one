package com.zjrb.sjzsw.adapter;

import android.content.Context;
import android.view.ViewGroup;

import java.util.List;

/**
 * 分类type的adapter
 *
 * @param <T>
 */
public abstract class MultiItemRecycleAdapter<T> extends BaseRecycleAdapter<T> {
    protected IMultiItemType<T> mIMultiItemType;

    public MultiItemRecycleAdapter(Context context, List<T> datas, IMultiItemType<T> IMultiItemType) {
        super(context, -1, datas);
        mIMultiItemType = IMultiItemType;
    }

    @Override
    public int getItemViewType(int position) {
        if (mIMultiItemType == null) return super.getItemViewType(position);
        return mIMultiItemType.getItemViewType(position, mDatas.get(position));
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mIMultiItemType == null) return super.onCreateViewHolder(parent, viewType);
        return RecycleViewHolder.get(mContext, parent, mIMultiItemType.getLayoutId(viewType));
    }
}
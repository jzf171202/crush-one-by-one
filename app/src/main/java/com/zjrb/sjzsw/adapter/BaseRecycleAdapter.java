package com.zjrb.sjzsw.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

public abstract class BaseRecycleAdapter<T> extends RecyclerView.Adapter<RecycleViewHolder> {

    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public BaseRecycleAdapter(Context context, int layoutId, List<T> datas) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;
    }

    @Override
    public RecycleViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        RecycleViewHolder recycleViewHolder = RecycleViewHolder.get(mContext, parent, mLayoutId);
        return recycleViewHolder;
    }

    @Override
    public void onBindViewHolder(RecycleViewHolder recycleViewHolder, int position) {
        convert(recycleViewHolder, mDatas.get(position));
    }

    public abstract void convert(RecycleViewHolder holder, T t);

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
}

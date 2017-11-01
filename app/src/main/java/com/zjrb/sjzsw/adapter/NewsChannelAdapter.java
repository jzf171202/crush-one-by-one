/*
 * Copyright (c) 2016 咖枯 <kaku201313@163.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.zjrb.sjzsw.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.entity.NewsChannel;
import com.zjrb.sjzsw.listener.OnItemClickListener;
import com.zjrb.sjzsw.utils.AppUtil;

import java.util.List;


/**
 * @author 咖枯
 * @version 1.0 2016/6/30
 */
public class NewsChannelAdapter extends BaseRecyclerViewAdapter<NewsChannel>
         {
    private static final int TYPE_CHANNEL_FIXED = 0;
    private static final int TYPE_CHANNEL_NO_FIXED = 1;


    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }


    public NewsChannelAdapter(List<NewsChannel> newsChannelTableList) {
        super(newsChannelTableList);
    }

    public List<NewsChannel> getData() {
        return mList;
    }

    @Override
    public NewsChannelViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news_channel, parent, false);
        final NewsChannelViewHolder newsChannelViewHolder = new NewsChannelViewHolder(view);
        handleLongPress(newsChannelViewHolder);
        handleOnClick(newsChannelViewHolder);
        return newsChannelViewHolder;
    }

    private void handleLongPress(final NewsChannelViewHolder newsChannelViewHolder) {
//        if (mItemDragHelperCallback != null) {
//            newsChannelViewHolder.itemView.setOnTouchListener(new View.OnTouchListener() {
//                @Override
//                public boolean onTouch(View v, MotionEvent event) {
//                    NewsChannelTable newsChannel = mList.get(newsChannelViewHolder.getLayoutPosition());
//                    boolean isChannelFixed = newsChannel.getNewsChannelFixed();
//                    if (isChannelFixed) {
//                        mItemDragHelperCallback.setLongPressEnabled(false);
//                    } else {
//                        mItemDragHelperCallback.setLongPressEnabled(true);
//                    }
//                    return false;
//                }
//            });
//        }
    }

    private void handleOnClick(final NewsChannelViewHolder newsChannelViewHolder) {
        if (mOnItemClickListener != null) {
            newsChannelViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!AppUtil.isFastDoubleClick()) {
                        mOnItemClickListener.onItemClick(v, newsChannelViewHolder.getLayoutPosition());
                    }
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final String newsChannel = mList.get(position).getNewsChannelName();
        String newsChannelName = newsChannel;
        NewsChannelViewHolder viewHolder = (NewsChannelViewHolder) holder;
        viewHolder.mNewsChannelTv.setText(newsChannelName);

//        if (position == 0) {
//            viewHolder.mNewsChannelTv.setTextColor(ContextCompat
//                    .getColor(App.getAppContext(), getColorId()));
//        }
    }

    private int getColorId() {
        int colorId;
//        if (MyUtils.isNightMode()) {
//            colorId = R.color.alpha_40_white;
//        } else {
            colorId = R.color.color_6b6b6b;
//        }
        return colorId;
    }

    @Override
    public int getItemViewType(int position) {
//        Boolean isFixed = mList.get(position).getNewsChannelFixed();
//        if (isFixed) {
//            return TYPE_CHANNEL_FIXED;
//        } else {
            return TYPE_CHANNEL_NO_FIXED;
//        }
    }




    class NewsChannelViewHolder extends RecyclerView.ViewHolder {
        TextView mNewsChannelTv;

        public NewsChannelViewHolder(View view) {
            super(view);
            mNewsChannelTv = view.findViewById(R.id.news_channel_tv);
        }
    }
}

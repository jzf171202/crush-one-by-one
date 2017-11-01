package com.zjrb.sjzsw.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.zjrb.sjzsw.App;
import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.adapter.NewsChannelAdapter;
import com.zjrb.sjzsw.entity.NewsChannel;
import com.zjrb.sjzsw.greendao.NewsChannelDao;
import com.zjrb.sjzsw.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thinkpad on 2017/10/27.
 */

public class NewsChannelActivity extends BaseActivity implements View.OnClickListener{

    ImageView back;
    RecyclerView mNewsChannelMineRv, mNewsChannelMoreRv;

    List<NewsChannel> newsChannels_mine = new ArrayList<>();
    List<NewsChannel> newsChannels_more = new ArrayList<>();

    private NewsChannelAdapter mNewsChannelAdapterMine;
    private NewsChannelAdapter mNewsChannelAdapterMore;

    private NewsChannelDao newsChannelDao;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_news_channel;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        newsChannelDao = App.getAppContext().getDaoSession().getNewsChannelDao();
        initView();
    }


    private void initView() {
        newsChannels_mine = newsChannelDao.loadAll();
        for(int i=0 ; i< 10 ; i++)
        {
            NewsChannel channel = new NewsChannel();
            channel.setNewsChannelId("channel_no" + i);
            channel.setNewsChannelName("未标" + i);
            channel.setNewsChannelType("2");
            newsChannels_more.add(channel);
        }
        back = (ImageView) findViewById(R.id.back);
        back.setOnClickListener(this);
        mNewsChannelMineRv = (RecyclerView) findViewById(R.id.news_channel_mine_rv);
        mNewsChannelMoreRv = (RecyclerView) findViewById(R.id.news_channel_more_rv);
        initRecycleViews(newsChannels_mine, newsChannels_more);
    }

    private void initRecycleViews(List<NewsChannel> newsChannels_mine, List<NewsChannel> newsChannels_more) {
        initRecycleView(mNewsChannelMineRv, newsChannels_mine, true);
        initRecycleView(mNewsChannelMoreRv, newsChannels_more, false);
    }

    private void initRecycleView(RecyclerView recyclerView, List<NewsChannel> newsChannels, boolean isChannelMine) {
        // !!!加上这句将不能动态增加列表大小。。。
//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        if(isChannelMine)
        {
            mNewsChannelAdapterMine = new NewsChannelAdapter(newsChannels_mine);
            recyclerView.setAdapter(mNewsChannelAdapterMine);
            setChannelMineOnItemClick();

        }
        else
        {
            mNewsChannelAdapterMore = new NewsChannelAdapter(newsChannels_more);
            recyclerView.setAdapter(mNewsChannelAdapterMore);
            setChannelMoreOnItemClick();

        }
    }

    private void setChannelMineOnItemClick() {
        mNewsChannelAdapterMine.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NewsChannel newsChannel = mNewsChannelAdapterMine.getData().get(position);
                newsChannels_more.add(newsChannel);
                mNewsChannelAdapterMore.notifyDataSetChanged();
                newsChannels_mine.remove(position);
                newsChannelDao.delete(newsChannel);
                mNewsChannelAdapterMine.notifyDataSetChanged();



            }
        });
    }

    private void setChannelMoreOnItemClick() {

        mNewsChannelAdapterMore.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                NewsChannel newsChannel = mNewsChannelAdapterMore.getData().get(position);
                newsChannelDao.insert(newsChannel);
                newsChannels_mine.add(newsChannel);
                mNewsChannelAdapterMine.notifyDataSetChanged();
                newsChannels_more.remove(position);
                mNewsChannelAdapterMore.notifyDataSetChanged();
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.back:
                Intent intent = new Intent();
                intent.putExtra("type", "change");
                setResult(RESULT_OK, intent);
                finish();
                break;
            default:
                break;
        }
    }
}

package com.zjrb.sjzsw.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.zjrb.sjzsw.App;
import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.adapter.pageadapter.NewsFragmentPagerAdapter;
import com.zjrb.sjzsw.common.Constant;
import com.zjrb.sjzsw.entity.NewsChannel;
import com.zjrb.sjzsw.greendao.NewsChannelDao;
import com.zjrb.sjzsw.ui.fragment.NewsFragment;
import com.zjrb.sjzsw.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thinkpad on 2017/10/27.
 */

public class NewsActivity extends AppCompatActivity implements View.OnClickListener, Toolbar.OnMenuItemClickListener{

    Toolbar toolbar;
    TabLayout tabs;
    ImageView add_channel_iv;
    ViewPager view_pager;
    FloatingActionButton fab;

    private String mCurrentViewPagerName;
    private List<String> mChannelNames;
    private List<Fragment> mNewsFragmentList = new ArrayList<>();
    List<NewsChannel> newsChannels = new ArrayList<>();
    List<String> newsChannelnames = new ArrayList<>();

    private NewsChannelDao newsChannelDao;

//    @Override
//    protected int getLayoutId() {
//        return R.layout.activity_news;
//    }

//    @Override
//    protected void init(@Nullable Bundle savedInstanceState) {

//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        newsChannelDao = App.getAppContext().getDaoSession().getNewsChannelDao();
        initView();

        initViewPager(newsChannels);
    }

    private void initView() {

        newsChannels = newsChannelDao.loadAll();
        if(newsChannels.size() == 0) {
            for (int i = 0; i < 3; i++) {
                NewsChannel channel = new NewsChannel();
                channel.setNewsChannelId("channel" + i);
                channel.setNewsChannelName("标签" + i);
                channel.setNewsChannelType("1");
                newsChannels.add(channel);
                newsChannelDao.insert(channel);
            }
        }
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.setOnMenuItemClickListener(this);
        tabs = (TabLayout) findViewById(R.id.tabs);
        add_channel_iv = (ImageView) findViewById(R.id.add_channel_iv);
        add_channel_iv.setOnClickListener(this);
        view_pager = (ViewPager) findViewById(R.id.view_pager);
        fab = (FloatingActionButton) findViewById(R.id.fab);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.fab:
                //滑动到顶部
                break;
            case R.id.add_channel_iv:
                //跳到标签编辑页面
                Intent intent = new Intent(this, NewsChannelActivity.class);
                startActivityForResult(intent, 0x001);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_OK) {
            return;
        }
        if(requestCode == 0x001)
        {
            //刷新操作
            newsChannels.clear();
            newsChannels = newsChannelDao.loadAll();
            initViewPager(newsChannels);
        }
    }

    private void initViewPager(List<NewsChannel> newsChannels) {
        if (newsChannels != null) {
            setNewsList(newsChannels, newsChannelnames);
            setViewPager(newsChannelnames);
        }
    }

    private void setNewsList(List<NewsChannel> newsChannels, List<String> newsChannelnames) {
        mNewsFragmentList.clear();
        newsChannelnames.clear();
        for(NewsChannel newsChannel : newsChannels)
        {
            NewsFragment newsFragment = creatFragment(newsChannel);
            mNewsFragmentList.add(newsFragment);
            newsChannelnames.add(newsChannel.getNewsChannelName());

        }
    }

    private NewsFragment creatFragment(NewsChannel newsChannel) {
        NewsFragment fragment = new NewsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Constant.NEWS_ID, newsChannel.getNewsChannelId());
        bundle.putString(Constant.NEWS_TYPE, newsChannel.getNewsChannelType());
        bundle.putInt(Constant.CHANNEL_POSITION, newsChannel.getNewsChannelIndex());
        fragment.setArguments(bundle);
        return fragment;
    }

    private void setViewPager(List<String> newsChannelnames) {

        NewsFragmentPagerAdapter adapter = new NewsFragmentPagerAdapter(
                getSupportFragmentManager(), newsChannelnames, mNewsFragmentList);
        view_pager.setAdapter(adapter);
        tabs.setupWithViewPager(view_pager);
        Utils.dynamicSetTabLayoutMode(tabs);
        setPageChangeListener();
        mChannelNames = newsChannelnames;

        int currentViewPagerPosition = getCurrentViewPagerPosition();
        view_pager.setCurrentItem(currentViewPagerPosition, false);
    }

    private int getCurrentViewPagerPosition() {
        int position = 0;
        if (mCurrentViewPagerName != null) {
            for (int i = 0; i < mChannelNames.size(); i++) {
                if (mCurrentViewPagerName.equals(mChannelNames.get(i))) {
                    position = i;
                }
            }
        }
        return position;
    }

    private void setPageChangeListener() {

        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentViewPagerName = mChannelNames.get(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.day:
                App.getAppContext().setThemes(this, false);
                break;

            case R.id.night:
                App.getAppContext().setThemes(this, true);
                break;
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getAppContext().refreshResources(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

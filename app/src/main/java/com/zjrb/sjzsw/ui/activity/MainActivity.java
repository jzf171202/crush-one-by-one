package com.zjrb.sjzsw.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jzf.net.exception.ApiException;
import com.jzf.net.listener.OnResultCallBack;
import com.jzf.net.observer.BaseObserver;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.controller.MainController;
import com.zjrb.sjzsw.entity.TopicBean;
import com.zjrb.sjzsw.utils.ActivityUtil;
import com.zjrb.sjzsw.utils.ListUtil;
import com.zjrb.sjzsw.utils.Utils;
import com.zjrb.sjzsw.widget.baselayout.LoadLayout;
import com.zjrb.sjzsw.widget.recyclerview.CommonAdapter;
import com.zjrb.sjzsw.widget.recyclerview.base.MyViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {
    @BindView(R.id.leftImage)
    ImageButton leftImage;
    @BindView(R.id.titleText)
    TextView titleText;
    @BindView(R.id.rightImage)
    ImageButton rightImage;
    @BindView(R.id.container)
    RelativeLayout container;
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.load_layout)
    LoadLayout loadLayout;

    private MainController mainController;
    private List<TopicBean.DataBean.TopicListsBean> beanList = new ArrayList<>();
    private CommonAdapter commonAdapter = null;
    //分页加载页号
    private int pageNo = 1;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        registerController(mainController = new MainController(this));
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        leftImage.setImageResource(R.drawable.homepage_set_up);
        rightImage.setVisibility(View.VISIBLE);
        rightImage.setImageResource(R.drawable.homepage_establish_btn);
        titleText.setText("移动直播");
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        //初始化SmartRefreshLayout
        refreshLayout.autoRefresh();
        refreshLayout.autoLoadmore();
        // TODO: 2017/11/8 设置刷新控件进度条样式。
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                pageNo = pageNo + 1;
                getProgramList();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                pageNo = 1;
                getProgramList();
            }
        });


        final View.OnClickListener myClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.item_layout:
                        showToast("item_layout");
                        break;
                    case R.id.status_bt:
                        showToast("status_bt");
                        break;
                    case R.id.preview_bt:
                        ActivityUtil.nextWeb(MainActivity.this, "http://baidu.com");
                        break;
                    case R.id.modify_bt:
                        showToast("modify_bt");
                        break;
                    default:
                        break;
                }
            }
        };

        recycleView.setAdapter(commonAdapter = new CommonAdapter<TopicBean.DataBean.TopicListsBean>(this, R.layout.item_main_list, beanList) {
            @Override
            protected void convert(MyViewHolder holder, TopicBean.DataBean.TopicListsBean topicListsBean, int position) {
                RelativeLayout itemLayout = holder.getView(R.id.item_layout);
                TextView itemTitle = holder.getView(R.id.item_title);
                TextView statusText = holder.getView(R.id.status_text);
                TextView statusBt = holder.getView(R.id.status_bt);
                TextView timeStart = holder.getView(R.id.time_start);
                TextView timeEnd = holder.getView(R.id.time_end);
                TextView previewBt = holder.getView(R.id.preview_bt);
                TextView modifyBt = holder.getView(R.id.modify_bt);
                RelativeLayout bottomLayout = holder.getView(R.id.item_bottom_layout);

                itemTitle.setText(topicListsBean.getTitle());
                timeStart.setText("开始：" + Utils.getNowDate("MM月dd日 HH:mm", topicListsBean.getBeginTime()));
                timeEnd.setText("结束：" + Utils.getNowDate("MM月dd日 HH:mm", topicListsBean.getEndTime()));

                itemLayout.setOnClickListener(myClickListener);
                statusBt.setOnClickListener(myClickListener);
                previewBt.setOnClickListener(myClickListener);
                modifyBt.setOnClickListener(myClickListener);

                switch (topicListsBean.getStatus()) {
                    case 0:
                        statusText.setText("· 待直播");
                        statusText.setTextColor(getResources().getColor(R.color.color_ff912e));
                        statusBt.setText("开始直播");
                        statusBt.setTextColor(getResources().getColor(R.color.white));
                        statusBt.setBackgroundResource(R.drawable.bg_round_light_red);
                        break;
                    case 1:
                        statusText.setText("· 直播中");
                        statusText.setTextColor(getResources().getColor(R.color.color_ff4e4b));
                        statusBt.setText("开始直播");
                        statusBt.setTextColor(getResources().getColor(R.color.white));
                        statusBt.setBackgroundResource(R.drawable.bg_round_light_red);
                        break;
                    case 2:
                        // TODO: 2017/11/8 遍历本地文件，根据文件的回放状态设置item样式。
                        statusText.setText("已结束");
                        statusText.setTextColor(getResources().getColor(R.color.color_b2b2b2));
                        statusBt.setText("无回放");
                        statusBt.setBackgroundResource(R.drawable.bg_round_grey);
                        statusBt.setTextColor(getResources().getColor(R.color.color_b2b2b2));

                        bottomLayout.setVisibility(View.GONE);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    /**
     * 获取节目列表
     */
    private void getProgramList() {
        loadLayout.setVisibility(View.GONE);
        mainController.getProgramList(pageNo, new BaseObserver(new OnResultCallBack<TopicBean>() {
            @Override
            public void onSuccess(TopicBean topicBean) {
                TopicBean.DataBean dataBean = topicBean.getData();
                List<TopicBean.DataBean.TopicListsBean> topicLists = dataBean.getTopicLists();
                if (!ListUtil.isListEmpty(topicLists)) {
                    beanList.addAll(topicLists);
                    commonAdapter.notifyDataSetChanged();
                } else if (pageNo == 1) {
                    loadLayout.showEmptyView();
                }
            }

            @Override
            public void onComplete() {
                refreshLayout.finishRefresh();
                refreshLayout.finishLoadmore();
            }

            @Override
            public void onError(ApiException.ResponeThrowable e) {
                Log.e("onError", "" + e.getMessage());
                if (pageNo == 1) {
                    loadLayout.showErrorView();
                    loadLayout.getErrorLayout().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getProgramList();
                        }
                    });
                }
            }
        }));
    }


    @OnClick({R.id.leftImage, R.id.rightImage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.leftImage:
                break;
            case R.id.rightImage:
                break;
            default:
                break;
        }
    }
}
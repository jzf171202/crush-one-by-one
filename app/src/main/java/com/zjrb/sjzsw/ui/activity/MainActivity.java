package com.zjrb.sjzsw.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadmoreListener;
import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.controller.MainController;
import com.zjrb.sjzsw.entity.HomeBean;
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
    @BindView(R.id.empty_layout)
    LinearLayout emptyLayout;
    @BindView(R.id.error_layout)
    LinearLayout errorLayout;
    @BindView(R.id.load_layout)
    FrameLayout loadLayout;
    @BindView(R.id.recycle_view)
    RecyclerView recycleView;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;

    private MainController mainController;
    private List<HomeBean> beanList = new ArrayList<>();
    private CommonAdapter commonAdapter = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        registerController(mainController = new MainController(this));
        initData();
        initView();
    }

    private void initData() {
        beanList.add(new HomeBean("UC 创始人何小鹏退役现场", "开始直播", "开始：8月21日 14：30", "结束：8月21日 16：30"));
        beanList.add(new HomeBean("UC 创始人何小鹏退役现场", "", "开始：8月21日 14：30", "结束：8月21日 16：30"));
        beanList.add(new HomeBean("UC 创始人何小鹏退役现场", "", "开始：8月21日 14：30", "结束：8月21日 16：30"));
        beanList.add(new HomeBean("UC 创始人何小鹏退役现场", "开始直播", "开始：8月21日 14：30", "结束：8月21日 16：30"));
        beanList.add(new HomeBean("UC 创始人何小鹏退役现场", "", "开始：8月21日 14：30", "结束：8月21日 16：30"));
        beanList.add(new HomeBean("UC 创始人何小鹏退役现场", "开始直播", "开始：8月21日 14：30", "结束：8月21日 16：30"));
        beanList.add(new HomeBean("UC 创始人何小鹏退役现场", "", "开始：8月21日 14：30", "结束：8月21日 16：30"));
        beanList.add(new HomeBean("UC 创始人何小鹏退役现场", "开始直播", "开始：8月21日 14：30", "结束：8月21日 16：30"));
        beanList.add(new HomeBean("UC 创始人何小鹏退役现场", "", "开始：8月21日 14：30", "结束：8月21日 16：30"));
        beanList.add(new HomeBean("UC 创始人何小鹏退役现场", "开始直播", "开始：8月21日 14：30", "结束：8月21日 16：30"));
        beanList.add(new HomeBean("UC 创始人何小鹏退役现场", "", "开始：8月21日 14：30", "结束：8月21日 16：30"));
        beanList.add(new HomeBean("UC 创始人何小鹏退役现场", "开始直播", "开始：8月21日 14：30", "结束：8月21日 16：30"));
        beanList.add(new HomeBean("UC 创始人何小鹏退役现场", "", "开始：8月21日 14：30", "结束：8月21日 16：30"));
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
        recycleView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
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
                        showToast("preview_bt");
                        break;
                    case R.id.modify_bt:
                        showToast("modify_bt");
                        break;
                    default:
                        break;
                }
            }
        };
        recycleView.setAdapter(commonAdapter = new CommonAdapter<HomeBean>(this, R.layout.item_main_list, beanList) {
            @Override
            protected void convert(MyViewHolder holder, HomeBean homeBean, int position) {
                RelativeLayout itemLayout = holder.getView(R.id.item_layout);
                TextView itemTitle = holder.getView(R.id.item_title);
                TextView statusText = holder.getView(R.id.status_text);
                TextView statusBt = holder.getView(R.id.status_bt);
                TextView timeStart = holder.getView(R.id.time_start);
                TextView timeEnd = holder.getView(R.id.time_end);
                TextView previewBt = holder.getView(R.id.preview_bt);
                TextView modifyBt = holder.getView(R.id.modify_bt);
                RelativeLayout bottomLayout = holder.getView(R.id.item_bottom_layout);

                itemTitle.setText(homeBean.getTitle());
                statusText.setText(homeBean.getStatus());
                timeStart.setText(homeBean.getStarttime());
                timeEnd.setText(homeBean.getEndtime());
                itemLayout.setOnClickListener(myClickListener);
                statusBt.setOnClickListener(myClickListener);
                previewBt.setOnClickListener(myClickListener);
                modifyBt.setOnClickListener(myClickListener);
                if (!"开始直播".equals(homeBean.getStatus())) {
                    bottomLayout.setVisibility(View.GONE);
                }
            }
        });


        //初始化SmartRefreshLayout
//        refreshLayout.autoRefresh();
//        refreshLayout.autoLoadmore();
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
                refreshLayout.finishLoadmore();
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                refreshLayout.finishRefresh();
                //加载反馈页面
//                loadLayout.setVisibility(View.VISIBLE);
//                emptyLayout.setVisibility(View.VISIBLE);
//                emptyLayout.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        showToast("没有数据");
//                    }
//                });
            }
        });
    }


    @OnClick({R.id.leftImage, R.id.rightImage})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.leftImage:
                finish();
                break;
            case R.id.rightImage:
                break;
        }
    }
}
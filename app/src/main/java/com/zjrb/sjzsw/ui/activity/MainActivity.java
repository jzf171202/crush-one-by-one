package com.zjrb.sjzsw.ui.activity;

import android.os.Bundle;
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
import com.zjrb.sjzsw.entity.GirlList;
import com.zjrb.sjzsw.widget.recyclerview.CommonAdapter;
import com.zjrb.sjzsw.widget.recyclerview.DividerGridItemDecoration;
import com.zjrb.sjzsw.widget.recyclerview.MultiItemTypeAdapter;
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
    private List<GirlList.NewslistBean> beanList = new ArrayList<>();
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
        initView();
    }

    /**
     * 初始化view
     */
    private void initView() {
        recycleView.setLayoutManager(new LinearLayoutManager(this));
        recycleView.addItemDecoration(new DividerGridItemDecoration(this));
        recycleView.setAdapter(commonAdapter = new CommonAdapter<GirlList.NewslistBean>(this, R.layout.item_main_list, beanList) {
            @Override
            protected void convert(MyViewHolder holder, GirlList.NewslistBean newslistBean, int position) {
//                TextView tv = holder.getView(R.id.item_title);
//                ImageView itemImg = holder.getView(R.id.item_img);
//
//                tv.setText(newslistBean.getTitle());
//                Glide.with(context).load(newslistBean.getPicUrl()).centerCrop().placeholder(R.mipmap.img_defult).into(itemImg);
            }
        });
        commonAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        //初始化SmartRefreshLayout
        refreshLayout.autoRefresh();
        refreshLayout.autoLoadmore();
        refreshLayout.setRefreshHeader(new ClassicsHeader(this));
        refreshLayout.setRefreshFooter(new ClassicsFooter(this));
        refreshLayout.setOnRefreshLoadmoreListener(new OnRefreshLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {
            }

            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

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
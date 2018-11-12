package com.zjrb.sjzsw.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.andview.refreshview.XRefreshView;
import com.bumptech.glide.Glide;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.loader.ImageLoader;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;
import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.adapter.HomeTestAdapter;
import com.zjrb.sjzsw.databinding.FrRecycleBinding;
import com.zjrb.sjzsw.model.GirlsItemModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecycleFragment extends BaseFragment<FrRecycleBinding> {
    private String url = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTOlt6QXr6EtRYnCyKBUasvHhGj_-xnseBtgeDKj6Rjl-7uP3M3";
    private final static int SPAN_COUNT = 2;
    private List<GirlsItemModel> itemModels = new ArrayList<>(Arrays.asList(
            new GirlsItemModel(1, "1", url, 0),
            new GirlsItemModel(2, "2", url, 1),
            new GirlsItemModel(3, "3", url, 0),
            new GirlsItemModel(4, "4", url, 1)
    ));
    private List<GirlsItemModel> list = new ArrayList<>();
    private HomeTestAdapter homeTestAdapter;


    @Override
    protected int getLayoutId() {
        return R.layout.fr_recycle;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
    }

    @Override
    protected void initView(View root) {
        list.addAll(itemModels);
        initXrefreshView();
        // setNormalAdapter();
        setHeadAdapter();
    }

    private void setHeadAdapter() {
        t.recycleview.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        // t.recycleview.addItemDecoration(new DividerGridItemDecoration(getActivity(), R.drawable.recycle_divider));

        HeaderAndFooterWrapper headerAndFooterWrapper = new HeaderAndFooterWrapper(
                homeTestAdapter = new HomeTestAdapter(getActivity(), R.layout.item_homelist, list));
        View head = LayoutInflater.from(context).inflate(R.layout.head_recycleview, null, false);
        initBanner(head);
        headerAndFooterWrapper.addHeaderView(head);
        t.recycleview.setAdapter(headerAndFooterWrapper);
        headerAndFooterWrapper.notifyDataSetChanged();
    }

    private void setNormalAdapter() {
        //线性纵向列表
//        t.recycleview.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
//        t.recycleview.addItemDecoration(new DividerListItemDecoration(getActivity(), DividerListItemDecoration.VERTICAL_LIST, R.drawable.recycle_divider));
        //网格列表
        t.recycleview.setLayoutManager(new GridLayoutManager(getActivity(), SPAN_COUNT));
        //t.recycleview.addItemDecoration(new DividerGridItemDecoration(getActivity(), R.drawable.recycle_divider));
        t.recycleview.setAdapter(homeTestAdapter = new HomeTestAdapter(getActivity(), R.layout.item_homelist, list));
    }

    private void initXrefreshView() {
        t.xrefreshview.setAutoRefresh(true);
        t.xrefreshview.setPullLoadEnable(true);
        t.xrefreshview.setPullRefreshEnable(true);
        t.xrefreshview.setAutoLoadMore(true);
        t.xrefreshview.setMoveForHorizontal(true);
        t.xrefreshview.setXRefreshViewListener(new XRefreshView.SimpleXRefreshListener() {
            @Override
            public void onRefresh(boolean isPullDown) {
                super.onRefresh(isPullDown);
                t.xrefreshview.postDelayed(() -> t.xrefreshview.stopRefresh(), 1000);
            }

            @Override
            public void onLoadMore(boolean isSilence) {
                super.onLoadMore(isSilence);
                list.addAll(itemModels);
                if (homeTestAdapter != null) {
                    // TODO: 2018/11/12  数据刷新无效
                    homeTestAdapter.notifyDataSetChanged();
                }
                t.xrefreshview.postDelayed(() -> t.xrefreshview.stopLoadMore(), 1000);
            }
        });
    }

    private void initBanner(View head) {
        Banner banner = head.findViewById(R.id.banner);
        TextView title = head.findViewById(R.id.mid_text);
        title.setOnClickListener(v -> showToast("你点击了head的标题"));
        banner.setImages(itemModels);
        banner.isAutoPlay(true);
        banner.setDelayTime(3000);
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object object, ImageView imageView) {
                GirlsItemModel girlsItemModel = (GirlsItemModel) object;
                if (context != null) {
                    Glide.with(context).load(girlsItemModel.getUrl()).placeholder(R.drawable.icon_simple).into(imageView);
                }
            }
        });
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        banner.start();
    }
}

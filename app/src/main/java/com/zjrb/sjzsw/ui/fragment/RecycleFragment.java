package com.zjrb.sjzsw.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.adapter.BaseListAdapter;
import com.zjrb.sjzsw.adapter.BaseRecycleAdapter;
import com.zjrb.sjzsw.adapter.DividerItemDecoration;
import com.zjrb.sjzsw.adapter.RecycleViewHolder;
import com.zjrb.sjzsw.databinding.FrRecycleBinding;
import com.zjrb.sjzsw.model.GirlsItemModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecycleFragment extends BaseFragment<FrRecycleBinding> {
    private String url = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTOlt6QXr6EtRYnCyKBUasvHhGj_-xnseBtgeDKj6Rjl-7uP3M3";

    private List<GirlsItemModel> girlsItemModels = new ArrayList<>(Arrays.asList(
            new GirlsItemModel(1, "1", url),
            new GirlsItemModel(2, "2", url),
            new GirlsItemModel(3, "3", url),
            new GirlsItemModel(4, "4", url),
            new GirlsItemModel(5, "5", url),
            new GirlsItemModel(6, "6", url),
            new GirlsItemModel(7, "7", url),
            new GirlsItemModel(8, "8", url),
            new GirlsItemModel(9, "9", url),
            new GirlsItemModel(10, "10", url),
            new GirlsItemModel(10, "14", url),
            new GirlsItemModel(10, "15", url),
            new GirlsItemModel(10, "16", url),
            new GirlsItemModel(10, "17", url),
            new GirlsItemModel(10, "18", url),
            new GirlsItemModel(10, "19", url),
            new GirlsItemModel(10, "20", url),
            new GirlsItemModel(10, "21", url),
            new GirlsItemModel(10, "22", url),
            new GirlsItemModel(11, "23", url),
            new GirlsItemModel(12, "24", url),
            new GirlsItemModel(13, "13", url)
    ));

    private BaseListAdapter listAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fr_recycle;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
    }

//    private void setListAdapter() {
//        t.listview.setAdapter(listAdapter = new BaseListAdapter<GirlsItemModel>(
//                getActivity(), girlsItemModels, R.layout.item_homelist) {
//            @Override
//            public void convert(ListViewHolder viewHolder, GirlsItemModel listItemEntity) {
//                TextView name = viewHolder.getView(R.id.name);
//                ImageView img = viewHolder.getView(R.id.img);
//                name.setText("" + listItemEntity.getName());
//                Glide.with(context).load(listItemEntity.getUrl()).placeholder(R.drawable.icon_simple).into(img);
//            }
//        });
//    }

    @Override
    protected void initView(View root) {
        initRecycleView();
    }

    private void initRecycleView() {
        t.recycleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        t.recycleview.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
        t.recycleview.setAdapter(new BaseRecycleAdapter<GirlsItemModel>(getActivity(), R.layout.item_homelist, girlsItemModels) {
            @Override
            public void convert(RecycleViewHolder holder, GirlsItemModel girlsItemModel, int position) {
                TextView name = holder.getView(R.id.name);
                name.setText("" + girlsItemModel.getName());
            }
        });
    }
}

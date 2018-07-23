package com.zjrb.sjzsw.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.adapter.BaseListAdapter;
import com.zjrb.sjzsw.adapter.ListViewHolder;
import com.zjrb.sjzsw.databinding.FrRecycleBinding;
import com.zjrb.sjzsw.entity.ListItemEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecycleFragment extends BaseFragment<FrRecycleBinding> {
    private String url = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSN9b2J56II7OCCTls-Za5B3eBXJ4WMDVJ96FFK0ql49cyptteN";
    private List<ListItemEntity> entityList = new ArrayList<>(Arrays.asList(
            new ListItemEntity(1, "1", url),
            new ListItemEntity(2, "2", url),
            new ListItemEntity(3, "3", url),
            new ListItemEntity(4, "4", url),
            new ListItemEntity(5, "5", url),
            new ListItemEntity(6, "6", url),
            new ListItemEntity(7, "7", url),
            new ListItemEntity(8, "8", url),
            new ListItemEntity(9, "9", url),
            new ListItemEntity(10, "10", url),
            new ListItemEntity(11, "11", url),
            new ListItemEntity(12, "12", url),
            new ListItemEntity(13, "13", url)
    ));

    private BaseListAdapter listAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fr_recycle;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        // TODO: 2018/7/23 对照鸿翔封装的recycleadapter拓展
    }

//    private void setListAdapter() {
//        t.listview.setAdapter(listAdapter = new BaseListAdapter<ListItemEntity>(
//                getActivity(), entityList, R.layout.item_homelist) {
//            @Override
//            public void convert(ListViewHolder viewHolder, ListItemEntity listItemEntity) {
//                TextView name = viewHolder.getView(R.id.name);
//                ImageView img = viewHolder.getView(R.id.img);
//                name.setText("" + listItemEntity.getName());
//                Glide.with(context).load(listItemEntity.getUrl()).placeholder(R.drawable.icon_simple).into(img);
//            }
//        });
//    }

    @Override
    protected void initView(View root) {

    }
}

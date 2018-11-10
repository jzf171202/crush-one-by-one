package com.zjrb.sjzsw.adapter;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.model.GirlsItemModel;
import com.zjrb.sjzsw.utils.ScreenUtil;

import java.util.List;

public class HomeImgAdapter extends BaseRecycleAdapter<GirlsItemModel> {
    public HomeImgAdapter(Context context, int layoutId, List<GirlsItemModel> datas) {
        super(context, layoutId, datas);
    }

    @Override
    public void convert(RecycleViewHolder holder, GirlsItemModel girlsItemModel, int position) {
        TextView name = holder.getView(R.id.name);
        ImageView img = holder.getView(R.id.img);
        name.setText("" + girlsItemModel.getName());

        ViewGroup.LayoutParams imgParams = img.getLayoutParams();
        imgParams.width = ScreenUtil.getScreenWidth(mContext);
        imgParams.height = (int) (imgParams.width * 0.545f);
        img.setLayoutParams(imgParams);

        ViewGroup.LayoutParams textParams = img.getLayoutParams();
        textParams.width = ScreenUtil.getScreenWidth(mContext);
        name.setLayoutParams(textParams);

        if (mContext != null) {
            Glide.with(mContext).load(girlsItemModel.getUrl()).placeholder(R.drawable.icon_simple).into(img);
        }
    }
}

package com.zjrb.sjzsw.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.zjrb.sjzsw.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by jinzifu on 2017/9/17.
 */

public class ImageFragment extends BaseFragment {
    @BindView(R.id.image_show)
    ImageView imageShow;
    Unbinder unbinder;
    private String url = "http://a3.topitme.com/8/2d/b7/1128528404720b72d8o.jpg";

    @Override
    protected int getLayoutId() {
        return R.layout.fr_imageload;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        ImageLoader.getInstance().displayImage(url, imageShow);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

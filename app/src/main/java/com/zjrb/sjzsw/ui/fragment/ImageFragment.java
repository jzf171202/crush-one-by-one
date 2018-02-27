package com.zjrb.sjzsw.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.blankj.utilcode.util.AppUtils;
import com.jzf.image.cache.disc.impl.ext.libcore.io.DiskLruCache;
import com.jzf.image.core.ImageLoader;
import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.manager.DownloadManager;
import com.zjrb.sjzsw.manager.ThreadPoolManager;
import com.zjrb.sjzsw.utils.FileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
    private DiskLruCache diskLruCache;
    private String key;

    @Override
    protected int getLayoutId() {
        return R.layout.fr_imageload;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {
        try {
            key = FileUtil.hashKeyForDisk(url);
            diskLruCache = DiskLruCache.open(FileUtil.getDiskCacheDir("image"),
                    AppUtils.getAppVersionCode(), 1, 10 * 1024 * 1024, 20);
        } catch (IOException e) {
            e.printStackTrace();
        }
        cacheFile();
        getCache();
    }

    /**
     * 获取 DiskLruCache缓存
     */
    private void getCache() {
        try {
            if (null == diskLruCache) {
                diskLruCache = DiskLruCache.open(FileUtil.getDiskCacheDir("image"),
                        AppUtils.getAppVersionCode(), 1, 10 * 1024 * 1024, 20);
            }
            DiskLruCache.Snapshot snapshot = diskLruCache.get(key);
            if (snapshot != null) {
                InputStream inputStream = snapshot.getInputStream(0);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageShow.setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 写入 DiskLruCache缓存
     */
    private void cacheFile() {
        ImageLoader.getInstance().displayImage(url, imageShow);
        ThreadPoolManager.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    if (null == diskLruCache) {
                        diskLruCache = DiskLruCache.open(FileUtil.getDiskCacheDir("image"),
                                AppUtils.getAppVersionCode(), 1, 10 * 1024 * 1024, 20);
                    }
                    DiskLruCache.Editor edit = diskLruCache.edit(key);
                    if (edit != null) {
                        OutputStream outputStream = edit.newOutputStream(0);
                        //获取线程池中线程，下载大文件
                        boolean flag = DownloadManager.downloadFileRunnable(url, outputStream);
                        if (flag) {
                            edit.commit();
                        } else {
                            edit.abort();
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            if (diskLruCache != null) {
                diskLruCache.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

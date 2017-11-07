package com.zjrb.sjzsw.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.executor.FifoPriorityThreadPoolExecutor;
import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.utils.JsoupUtil;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Thinkpad on 2017/11/1.
 */

public class NewsDetailActivity extends BaseActivity implements View.OnClickListener{

    String url = "http://news.sina.com.cn/china/xlxw/2017-11-01/doc-ifynmnae0815561.shtml";
    String url2 = "http://video.sina.com.cn/p/ent/z/v/doc/2017-08-17/164466898279.html?opsubject_id=enttopnews";

    private ImageButton leftImage;
    private TextView titleText,htmltext, rightText;
    private ImageView img;
    StringBuffer text = new StringBuffer();
    String imgUrl;
    MyHandler handler;
    ExecutorService ThreadPool;


    @Override
    protected int getLayoutId() {
        return R.layout.activity_newsdetail;
    }

    @Override
    protected void init(@Nullable Bundle savedInstanceState) {

        initView();
        handler = new MyHandler(this);
        createThreadPool(3);
        JsoupTest();
    }

    private void initView() {


        leftImage = (ImageButton) findViewById(R.id.leftImage);
        leftImage.setOnClickListener(this);
        rightText = (TextView) findViewById(R.id.rightText);
        rightText.setOnClickListener(this);
        titleText = (TextView) findViewById(R.id.titleText);
        titleText.setText("Html解析");
        htmltext = (TextView) findViewById(R.id.htmltext);
        img = (ImageView) findViewById(R.id.img);
    }

    private void createThreadPool(int size)
    {
        ThreadPool = new ThreadPoolExecutor(size, 6, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<Runnable>(128));

    }



    private void JsoupTest() {

        ThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Elements titles_elements = JsoupUtil.getHtmlElements(url, "div.page-header");
                    String title = JsoupUtil.getString(titles_elements.get(0), "h1");
                    text.append(title);
                    text.append("\r\n");
                    Log.d("title", title);
                    Elements contents_elements = JsoupUtil.getHtmlElements(url, "div.article_16");
                    StringBuffer content = new StringBuffer();
                    for(Element e : contents_elements)
                    {
                        content.append(JsoupUtil.getString(e, "p"));
                        content.append("\r\n");
                    }
                    Log.d("content", content.toString());
                    text.append(content);
                    text.append("\r\n");
                    Elements img_elements = JsoupUtil.getHtmlElements(url, "div.img_wrapper");
                    imgUrl = JsoupUtil.getUrl(img_elements.get(0), "img", "src");
                    Log.d("imgUrl", imgUrl);
                    text.append(imgUrl);
                    handler.sendEmptyMessage(1);

                    Elements video_elements = JsoupUtil.getHtmlElements(url2, "div#myflashBox");
                    String videoUrl = JsoupUtil.getUrl(video_elements.get(0), "embed", "src");
                    Log.d("videoUrl", videoUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.leftImage:
                finish();
                break;
            case R.id.rightText:
                startActivity(new Intent(NewsDetailActivity.this, GreenDaoTestActivity.class));
                break;
            default:
                break;
        }
    }


    static class MyHandler extends Handler {

        WeakReference<NewsDetailActivity> activity;
        public MyHandler(NewsDetailActivity activity)
        {
            this.activity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            NewsDetailActivity newsDetailActivity = activity.get();
            switch (msg.what)
            {
                case 1:
                    newsDetailActivity.htmltext.setText(newsDetailActivity.text.toString());
                    Glide.with(newsDetailActivity).load(newsDetailActivity.imgUrl).centerCrop().placeholder(R.mipmap.img_defult).into(newsDetailActivity.img);
                    break;
                default:
                    break;
            }
        }
    }

}

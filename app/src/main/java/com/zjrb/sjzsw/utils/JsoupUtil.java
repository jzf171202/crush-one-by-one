package com.zjrb.sjzsw.utils;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Thinkpad on 2017/11/1.
 * Jsoup只能抓取静态页面数据，对于ajex动态加载的数据解析不出来
 *
 */

public class JsoupUtil {

    private static Document doc;
    private static Document initJsoup(String url) throws IOException {
        return Jsoup.connect(url).timeout(6*1000).get();

    }



    public static Elements getHtmlElements(String url, String key) throws IOException {
        doc = initJsoup(url);
//        Log.d("doc.title", doc.title());
        Elements elements = doc.select(key);

        return elements;
    }
    public static String getTitle()
    {
        return doc.title();
    }


    /**
     * el#id	定位 id 值某个元素，例如 a#logo -> <a id=logo href= … >
     el.class	定位 class 为指定值的元素，例如 div.head -> <div class="head">xxxx</div>
     el[attr]	定位所有定义了某属性的元素，例如 a[href]
     以上三个任意组合	例如 a[href]#logo 、a[name].outerlink
     * @param element
     * @param key
     * @return
     */
    public static String getString(Element element, String key)
    {
        return element.select(key).text();
    }

    public static String getUrl(Element element, String key, String childkey)
    {
        return  element.select(key).attr(childkey);
    }


}

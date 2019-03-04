package com.zjrb.sjzsw.jsBridge;

import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.zjrb.sjzsw.App;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class AppBridgeManager {
    private WebView webView;
    private Gson gson = new Gson();

    public AppBridgeManager(WebView webview) {
        this.webView = webview;
    }

    @JavascriptInterface
    public String appInvoke(String json) {
        String string = "";
        try {
            JsBridgeModel jsBridgeModel = gson.fromJson(json, JsBridgeModel.class);
            string = invoke(jsBridgeModel);
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        return string;
    }

    /**
     * 通过反射机制动态调用解析类方法
     *
     * @param jsBridgeModel
     * @return
     */
    private String invoke(JsBridgeModel jsBridgeModel) {
        if (jsBridgeModel == null) return "";
        //H5返回的service要和注册bridgeModelMap的key一致，确保唯一性
        Class<?> model = App.bridgeModelMap.get(jsBridgeModel.getService());
        if (model == null) return "";
        String result = "";
        try {
            //参数类型格式统一，便于反射动态调用
            Method method = model.getMethod(jsBridgeModel.getMethod(), String.class, String.class, WebView.class);
            JSONObject jsonObject = new JSONObject(jsBridgeModel.getData());
            if (jsonObject == null) return "";
            method.setAccessible(true);//忽略访问权限的限制
            result = (String) method.invoke(
                    model.newInstance(),
                    jsBridgeModel.getRequestId(),
                    jsonObject.toString(),
                    webView);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return result;
    }
}

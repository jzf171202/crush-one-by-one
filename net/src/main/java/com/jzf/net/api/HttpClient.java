package com.jzf.net.api;

import android.content.Context;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.jzf.net.Constant;
import com.jzf.net.cookie.CookieManger;
import com.jzf.net.exception.ApiException;
import com.jzf.net.interceptor.CaheInterceptor;
import com.jzf.net.interceptor.HeaderInterceptor;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author jinzifu
 */
public class HttpClient {
    private static final int DEFAULT_TIMEOUT = 5;
    private volatile static HttpClient instance;
    private static Context context;
    /**
     * 静态内部类——饿汉式
     */
    private static SchedulersTransformer schedulersTransformer = new SchedulersTransformer();
    private static ErrorTransformer errorTransformer = new ErrorTransformer();
    private Retrofit mRetrofit;
    private Cache cache = null;
    private File httpCacheDirectory;

    private HttpClient() {
        //header追加
        HashMap<String, String> headerMap = new HashMap<String, String>();

        //okhttp 3.0缓存
        CaheInterceptor caheInterceptor = new CaheInterceptor(context);
        if (httpCacheDirectory == null) {
            httpCacheDirectory = new File(context.getCacheDir(), "zjrb_cache");
        }
        try {
            if (cache == null) {
                cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
            }
        } catch (Exception e) {
            Log.e("OKHttp", "Could not apiServiceCreate http cache", e);
        }


        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.d("====HttpClient====", message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);

        //网络代理
//        Proxy proxy = new Proxy(Proxy.Type.HTTP,  new InetSocketAddress(proxyHost, proxyPort));

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .cookieJar(new CookieManger(context))
                .addInterceptor(new HeaderInterceptor(headerMap))
                .addInterceptor(loggingInterceptor)
                .addInterceptor(caheInterceptor)
//                .proxy(proxy)
                .addNetworkInterceptor(caheInterceptor)
                .cache(cache);
        //https请求 HostnameVerifier默认信任所有证书,如果要添加证书可参考 https://www.2cto.com/kf/201609/551319.html

        mRetrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(Constant.BASE_URL)
                .client(okHttpClient.build())
                .build();
    }

    public static void init(Context context) {
        HttpClient.context = context;
    }


    public static HttpClient getInstance() {
        if (instance == null) {
            synchronized (HttpClient.class) {
                if (instance == null) {
                    instance = new HttpClient();
                }
            }
        }
        return instance;
    }

    /**
     * @param observable
     * @param observer
     * @param <T>        例如：
     *                   ApiService customApi = HttpClient.getInstance().createService(ApiService.class);
     *                   HttpClient.getInstance().execute(customApi.getGirls(1, 10, "json1"), commonObserver);
     */
    public <T> void execute(Observable<T> observable, Observer<?> observer) {
        observable
                .compose(schedulersTransformer)
                .compose(errorTransformer)
                .subscribe(observer);
    }

    public <T> T createService(Class<T> tClass) {
        return mRetrofit.create(tClass);
    }

    /**
     * 处理线程调度
     */
    private static class SchedulersTransformer<T> implements ObservableTransformer {
        @Override
        public ObservableSource apply(Observable observable) {
            return ((Observable<T>) observable).subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread());
        }
    }

    /**
     * 处理数据及异常
     */
    private static class ErrorTransformer<T> implements ObservableTransformer {

        @Override
        public ObservableSource apply(Observable observable) {
            //onErrorResumeNext当发生错误的时候，由另外一个Observable来代替当前的Observable并继续发射数据
            return (Observable<T>) observable.map(new HandleFunction<T>()).onErrorResumeNext(new HttpResponseFunction<T>());
        }
    }

    private static class HttpResponseFunction<T> implements Function<Throwable, Observable<T>> {
        @Override
        public Observable<T> apply(Throwable throwable) throws Exception {
            return Observable.error(ApiException.handleException(throwable));
        }
    }

    public static class HandleFunction<T> implements Function<BaseResponse<T>, T> {
        @Override
        public T apply(BaseResponse<T> response) throws Exception {
            return (T) response;
        }
    }
}

package com.zjrb.sjzsw.manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 类描述：线程池管理器
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/12/7 1150
 */

public class ThreadPoolManager {
    /**
     * 核心线程的数量。当前设备可用处理器核心数*2 + 1,能够让cpu的效率得到最大程度执行。
     */
    private int corePoolSize;
    /**
     * 最大线程数量，表示当缓冲队列满的时候能继续容纳的等待任务的数量。
     */
    private int maximumPoolSize;
    /**
     * 非核心线程的超时时长，当系统中非核心线程闲置时间超过keepAliveTime之后，则会被回收。
     * 如果ThreadPoolExecutor的allowCoreThreadTimeOut属性设置为true，则该参数也表示核心线程的超时时长。
     */
    private long keepAliveTime = 1;
    /**
     * 时间单位，有纳秒、微秒、毫秒、秒、分、时、天等。
     */
    private TimeUnit timeUnit = TimeUnit.MINUTES;
    private ThreadPoolExecutor threadPoolExecutor;
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();

    private ThreadPoolManager() {
        init();
    }

    private static class SingleClass {
        private static final ThreadPoolManager THREAD_POOL_MANAGER = new ThreadPoolManager();
    }

    /**
     * 静态内部类单例模式
     *
     * @return
     */
    public static ThreadPoolManager getInstance() {
        return SingleClass.THREAD_POOL_MANAGER;
    }

    /**
     * 配置线程池属性
     * 部分参考AsyncTask的配置设计
     */
    private void init() {
        corePoolSize = CPU_COUNT + 1;
        maximumPoolSize = CPU_COUNT * 2 + 1;
        threadPoolExecutor = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime,
                timeUnit,
                new LinkedBlockingDeque<Runnable>(),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy());
    }

    /**
     * 执行任务
     *
     * @param runnable
     */
    public void execute(Runnable runnable) {
        if (runnable != null) {
            threadPoolExecutor.execute(runnable);
        }
    }


    /**
     * 从线程池移除任务
     *
     * @param runnable
     */
    public void remove(Runnable runnable) {
        if (runnable != null) {
            threadPoolExecutor.remove(runnable);
        }
    }
}

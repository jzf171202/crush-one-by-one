package com.zjrb.sjzsw.test;

import android.util.Log;

public class Sync {
    private static String string = new String("test");
    public Sync() {
        Log.d("Sync", "线程开始执行");
    }

    /**
     * e()和 f()总结：
     * 未经synchronized修饰的非静态方法和静态方法，两者所属不同对象实例。
     * 对数据源count的操作未实现同步（示例现象：未有序递减）。
     */
    public void e() {
        print();
    }

    public static void f() {
        print();
    }

    /**
     *  a()方法是经过synchronized修饰的非静态方法，同步的是在外部调用的对象实例。
     * 由于外部多线程中是不同的对象实例，则对count的操作未实现同步（示例现象：未有序递减）。
     */
    public synchronized void a() {
        print();
    }

    /**
     * b()方法是经过synchronized修饰的静态方法，同步的是本类的对象实例，和外部多线程中的调用对象实例无关。
     * 外部多线程，谁先获得本类的对象锁，其他线程则处于阻塞状态，直到获得锁的线程解锁。
     */
    public static synchronized void b() {
        print();
    }


    /**
     * c()和d()是静态方法和非静态方法的壳，内部synchronized修饰的是方法块。
     * 同步的对象是synchronized(obj)内的非NULL的Object。
     * 1.若obj为Sync.class，则同步Sync.class本身，同理synchronized修饰静态方法；
     * 2.若obj为类属性，如string变量，则同步的是变量所属对象实例，即是外部调用的对象实例。
     */
    public void c() {
        synchronized (new String()) {
            print();
        }
    }

    public static void d() {
        synchronized (Sync.class) {
            print();
        }
    }

    public static void print() {
        while (Count.INSTANT.count > 0) {
            Count.INSTANT.count = Count.INSTANT.count - 1;
            Log.d("Sync", "count = " + Count.INSTANT.count);
        }
    }
}

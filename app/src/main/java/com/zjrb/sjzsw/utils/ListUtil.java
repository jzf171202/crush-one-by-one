package com.zjrb.sjzsw.utils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by jinzifu on 15/11/25.
 */
public class ListUtil {

    /**
     * 去除list中重复数据——乱序返回
     *
     * @param list
     */
    public static List<String> removeDuplicate(List<String> list) {
        HashSet<String> h = new HashSet<String>(list);
        List<String> mList = new ArrayList<String>();
        mList.addAll(h);
        return mList;
    }

    /**
     * list倒序
     *
     * @param list
     * @return
     */
    public static List<String> replaceSort(List<String> list) {
        List<String> list2 = new ArrayList<String>();
        if (isListEmpty(list)) {
            return list2;
        }
        if (list.size() == 1) {
            return list;
        }
        for (int i = list.size() - 1; i >= 0; i--) {
            list2.add(list.get(i));
        }
        return list2;
    }

    /**
     * list是否为空
     */
    public static boolean isListEmpty(List<?> list) {
        if (list == null || list.size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * byte转Short
     *
     * @param src
     * @return
     */
    public static short[] byteToShort(byte[] src) {
        int count = src.length >> 1;
        short[] dest = new short[count];
        for (int i = 0; i < count; i++) {
            dest[i] = (short) ((src[i * 2] & 0xff) | ((src[2 * i + 1] & 0xff) << 8));
        }
        return dest;
    }

    /**
     * Short转byte
     *
     * @param src
     * @return
     */
    public static byte[] shortToByte(short[] src) {
        int count = src.length;
        byte[] dest = new byte[count << 1];
        for (int i = 0; i < count; i++) {
            dest[i * 2] = (byte) (src[i]);
            dest[i * 2 + 1] = (byte) (src[i] >> 8);
        }
        return dest;
    }
}

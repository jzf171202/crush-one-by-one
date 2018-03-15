package com.zjrb.sjzsw.runnable;

import com.zjrb.sjzsw.utils.ListUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 类描述：音频转码任务类
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2018/3/15 2241
 */

public class DecodeRunnable implements Runnable {
    private FileOutputStream fileOutputStream = null;
    // TODO: 2018/3/16 synchronizedList的用法和原理，是否会因为阻塞导致上下游流速不平衡（背压引入）
    private List<Task> mTasks = Collections.synchronizedList(new ArrayList<Task>());

    //异常上抛原则：异常根本原因不在此层就上抛，否则就在此层捕获处理
    public DecodeRunnable(int bufferSize, File decodeFile) throws FileNotFoundException {
        fileOutputStream = new FileOutputStream(decodeFile);
    }

    @Override
    public void run() {
        while (!ListUtil.isListEmpty(mTasks)){

        }
    }



    public void addTask(byte[] rawData, int readSize) {
        mTasks.add(new Task(rawData, readSize));
    }

    private class Task {
        private byte[] rawData;
        private int readSize;

        public Task(byte[] rawData, int readSize) {
            this.rawData = rawData.clone();
            this.readSize = readSize;
        }

        public byte[] getData() {
            return rawData;
        }

        public int getReadSize() {
            return readSize;
        }
    }
}

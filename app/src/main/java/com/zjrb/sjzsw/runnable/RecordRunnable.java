package com.zjrb.sjzsw.runnable;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Process;

import com.zjrb.sjzsw.manager.ThreadPoolManager;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * 类描述：音频录制任务类
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2018/1/4 1126
 */

public class RecordRunnable implements Runnable {
    private final String TAG = getClass().getSimpleName();

    private static final int DEFAULT_SAMPLING_RATE = 44100;
    private boolean isRecroding = false;
    private AudioRecord audioRecord = null;
    // TODO: 2018/3/15 和其他输出流的区别，输入流同样疑问
    private int bufferSize = 0;
    private File recordFile = null;
    private DecodeRunnable decodeRunnable = null;

    public RecordRunnable(File recordFile) throws FileNotFoundException {
        this.recordFile = recordFile;
        initAudioRecord();
    }

    /**
     * 初始化音频录制参数
     */
    private void initAudioRecord() throws FileNotFoundException {
        /**
         *  获取最小缓冲区大小，该值不能低于一帧“音频帧”（Frame）的大小，且须是一音频帧大小的整数倍。
         *  一帧音频帧的大小计算如下：int size = 采样率 x 位宽 x 采样时间 x 通道数
         */
        bufferSize = AudioRecord.getMinBufferSize(DEFAULT_SAMPLING_RATE,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

        //参数是：声源，采样率，声道，采样格式，缓冲区大小
        if (audioRecord == null) {
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, DEFAULT_SAMPLING_RATE,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize * 4);
        }

        //初始化转码工作线程
        ThreadPoolManager.getInstance().execute(decodeRunnable = new DecodeRunnable(bufferSize,
                recordFile));
    }

    public boolean startRecord() throws FileNotFoundException {
        isRecroding = true;
        if (audioRecord == null) {
            initAudioRecord();
        }
        audioRecord.startRecording();
        return isRecroding;
    }

    public boolean stopRecord() {
        isRecroding = false;
        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
        ThreadPoolManager.getInstance().remove(decodeRunnable);
        return !isRecroding;
    }

    @Override
    public void run() {
        //设置线程优先级
        Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
        // TODO: 2018/3/15 byte数组和short数组的区别
        byte[] bytes = new byte[bufferSize];
        int readSize = 0;
        while (isRecroding) {
            readSize = audioRecord.read(bytes, 0, bufferSize);
            if (readSize > 0) {
                //加入到转码线程任务队列中
                decodeRunnable.addTask(bytes, readSize);
            }
        }
    }
}

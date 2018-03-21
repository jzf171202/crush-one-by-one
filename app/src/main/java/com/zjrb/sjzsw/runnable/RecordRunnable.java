package com.zjrb.sjzsw.runnable;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Process;
import android.util.Log;

import com.zjrb.sjzsw.manager.ThreadPoolManager;

import java.io.File;

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
    private EncodeRunnable decodeRunnable = null;

    public RecordRunnable(File recordFile) {
        initAudio(recordFile);
    }

    public void setRecroding(boolean recroding) {
        isRecroding = recroding;
    }

    public void initAudio(File recordFile) {
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

        //初始化转码等工作线程
        ThreadPoolManager.getInstance().execute(decodeRunnable = new EncodeRunnable(bufferSize,
                recordFile));

        audioRecord.startRecording();
        setRecroding(true);
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
                Log.d(TAG, "本次录音数据 = " + readSize);
                //加入到转码线程任务队列中
                decodeRunnable.addTask(bytes, readSize);
            }
        }

        if (audioRecord != null) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
    }
}

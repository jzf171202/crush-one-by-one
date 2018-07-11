package com.zjrb.sjzsw.runnable;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Process;

import com.zjrb.sjzsw.manager.ThreadPoolManager;
import com.zjrb.sjzsw.utils.FileUtil;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 类描述：音频录制任务类
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2018/1/4 1126
 */

public class RecordRunnable implements Runnable {
    private static final int DEFAULT_SAMPLING_RATE = 44100;
    private final String TAG = getClass().getSimpleName();
    private boolean isRecroding = false;
    private AudioRecord audioRecord = null;
    private int bufferSize = 0;
    private EncodeRunnable encodeRunnable;
    private DataOutputStream dataOutputStream;

    public RecordRunnable() {
        /**
         *  获取最小缓冲区大小，该值不能低于一帧“音频帧”（Frame）的大小，且宜是一音频帧大小的整数倍。
         *  一帧音频帧的大小计算如下：int size = 采样率 x 位宽 x 采样时间 x 通道数
         */
        bufferSize = AudioRecord.getMinBufferSize(DEFAULT_SAMPLING_RATE,
                AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

        //参数是：声源，采样率，声道，采样格式，缓冲区大小
        if (audioRecord == null) {
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, DEFAULT_SAMPLING_RATE,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize * 4);
        }
        ThreadPoolManager.getInstance().execute(encodeRunnable = new EncodeRunnable());
        if (audioRecord != null && audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
            audioRecord.startRecording();
            setRecroding(true);
        }
    }

    public void setRecroding(boolean recroding) {
        isRecroding = recroding;
    }

    @Override
    public void run() {
        File recorderFile = new File(FileUtil.getDiskCacheDir("audio"), "test.pcm");
        try {
            dataOutputStream = new DataOutputStream(
                    new BufferedOutputStream(new FileOutputStream(recorderFile)));

            //设置线程优先级
            Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
            byte[] bytes = new byte[bufferSize];
            int readSize = 0;
            while (isRecroding) {
                readSize = audioRecord.read(bytes, 0, bufferSize);
                if (readSize > 0) {
                    encodeRunnable.addTask(bytes, readSize);
                    dataOutputStream.write(bytes, 0, readSize);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (audioRecord != null && audioRecord.getState() == AudioRecord.STATE_INITIALIZED) {
            audioRecord.stop();
            audioRecord.release();
            audioRecord = null;
        }
        encodeRunnable.myHandler.sendEmptyMessage(1);
    }
}

package com.zjrb.sjzsw.runnable;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import com.czt.mp3recorder.PcmSampleFormat;

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
    private final String TAG = getClass().getSimpleName();
    private static final int DEFAULT_SAMPLING_RATE = 44100;
    private static final PcmSampleFormat PCM_SAMPLE_FORMAT = PcmSampleFormat.PCM_16BIT;
    private static final int FRAME_COUNT = 160;
    private boolean isRecroding = false;
    private File recordFile;
    private byte[] bytes;

    public RecordRunnable(File recordFile) {
        if (null == recordFile) {
            throw new NullPointerException("recordFile为null");
        }
        this.recordFile = recordFile;
    }

    public void setRecroding(boolean recroding) {
        isRecroding = recroding;
    }

    @Override
    public void run() {
        AudioRecord audioRecord = null;
        DataOutputStream dataOutputStream = null;
        try {
            dataOutputStream = new DataOutputStream(
                    new BufferedOutputStream(new FileOutputStream(recordFile)));
            /**
             *  获取最小缓冲区大小，该值不能低于一帧“音频帧”（Frame）的大小，且须是一音频帧大小的整数倍。
             *  一帧音频帧的大小计算如下：int size = 采样率 x 位宽 x 采样时间 x 通道数
             */
            int bufferSize = AudioRecord.getMinBufferSize(DEFAULT_SAMPLING_RATE,
                    AudioFormat.CHANNEL_IN_MONO, PCM_SAMPLE_FORMAT.getAudioFormat());
            //确保能被整除，方便周期性通知
            int sampleNum = bufferSize/PCM_SAMPLE_FORMAT.getBytesPerSample();


            //参数是：声源，采样率，声道，采样格式，缓冲区大小
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, DEFAULT_SAMPLING_RATE,
                    AudioFormat.CHANNEL_IN_MONO, PCM_SAMPLE_FORMAT.getAudioFormat(), bufferSize);

            bytes = new byte[bufferSize];

            audioRecord.startRecording();
            int writeCount = 0;
            while (isRecroding) {
                writeCount = audioRecord.read(bytes, 0, bufferSize);
                if (writeCount != 0 && writeCount != -1) {
                    Log.d(TAG, "writeCount=" + writeCount);
                    //在此可以对录制音频的数据进行二次处理 比如变声，压缩，降噪，增益等操作
                    dataOutputStream.write(bytes, 0, writeCount);
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (audioRecord != null) {
                    audioRecord.stop();
                    audioRecord.release();
                }
                if (dataOutputStream != null) {
                    dataOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

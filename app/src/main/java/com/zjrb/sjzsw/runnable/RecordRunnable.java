package com.zjrb.sjzsw.runnable;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

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
    private boolean isRecroding = false;
    private File recordFile;

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
            //获取最小缓冲区大小
            int bufferSize = AudioRecord.getMinBufferSize(44100,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, 44100,
                    AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize * 4);
            byte[] bytes = new byte[bufferSize];
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

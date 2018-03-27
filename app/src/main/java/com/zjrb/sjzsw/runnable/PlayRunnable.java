package com.zjrb.sjzsw.runnable;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 类描述：pcm播放任务类
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2018/1/4 1516
 */

public class PlayRunnable implements Runnable {
    private final String TAG = getClass().getSimpleName();
    private static final int DEFAULT_SAMPLING_RATE = 44100;
    private File recordFile;

    public PlayRunnable(File recordFile) {
        if (null == recordFile) {
            throw new NullPointerException("recordFile为null");
        }
        this.recordFile = recordFile;
    }

    @Override
    public void run() {
        //获取最小缓冲区大小
        int bufferSize = AudioTrack.getMinBufferSize(DEFAULT_SAMPLING_RATE,
                AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT);
        byte[] bytes = new byte[bufferSize];
        //定义输入流，将音频写入到AudioTrack类中
        AudioTrack audioTrack = null;
        DataInputStream dataInputStream = null;
        try {
            audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, DEFAULT_SAMPLING_RATE,
                    AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, bufferSize * 4, AudioTrack.MODE_STREAM);
            dataInputStream = new DataInputStream(new BufferedInputStream(new FileInputStream(recordFile)));
            audioTrack.play();
            int readCount = 0;
            while (dataInputStream.available() > 0) {
                readCount = dataInputStream.read(bytes);
                if (readCount != 0 && readCount != -1) {
                    Log.d(TAG, "readCount=" + readCount);
                    audioTrack.write(bytes, 0, readCount);
                }
            }
            stopPlay(audioTrack, dataInputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            stopPlay(audioTrack, dataInputStream);
        }
    }

    /**
     * 停止播放
     *
     * @param audioTrack
     * @param dataInputStream
     */
    private void stopPlay(AudioTrack audioTrack, DataInputStream dataInputStream) {
        try {
            if (audioTrack != null) {
                audioTrack.stop();
                audioTrack.release();
            }
            if (dataInputStream != null) {
                dataInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package com.zjrb.sjzsw.runnable;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.zjrb.sjzsw.utils.FileUtil;
import com.zjrb.sjzsw.utils.ListUtil;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
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

public class EncodeRunnable implements Runnable {
    private static final int DEFAULT_SAMPLING_RATE = 44100;
    private static final String MIME = MediaFormat.MIMETYPE_AUDIO_AAC;
    private final String TAG = getClass().getSimpleName();
    public MyHandler myHandler;
    private DataOutputStream dataOutputStream = null;
    private List<Task> mTasks = Collections.synchronizedList(new ArrayList<Task>());
    private File recorderFile;
    private MediaCodec.BufferInfo bufferInfo;
    private MediaCodec mediaCodec;
    private MediaPlayer mediaPlayer;

    public EncodeRunnable() {
        try {
            recorderFile = new File(
                    FileUtil.getDiskCacheDir("audio"), "test.aac");
            dataOutputStream = new DataOutputStream(
                    new BufferedOutputStream(new FileOutputStream(recorderFile)));
            initMediaCodec();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始化音频播放器
     *
     * @throws IOException
     */
    private void initMediaPlayer() throws IOException {
        mediaPlayer = new MediaPlayer();
        //设置声源
        mediaPlayer.setDataSource(recorderFile.getAbsolutePath());
        //是否循环播放
        mediaPlayer.setLooping(false);
        //播放的流媒体类型
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //设置左右音量
        mediaPlayer.setVolume(1f, 1f);
        //预加载音频
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;
                return true;
            }
        });
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mediaPlayer.stop();
                mediaPlayer.reset();
                mediaPlayer.release();
                mediaPlayer = null;

                myHandler.removeCallbacksAndMessages(null);
            }
        });
    }

    ;

    /**
     * 初始化编码器
     *
     * @throws IOException
     */
    private void initMediaCodec() throws IOException {
        //转码类型，采样率，声道数
        MediaFormat mediaFormat = MediaFormat.createAudioFormat(MIME, DEFAULT_SAMPLING_RATE, 1);
        //比特率
        mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 48 * 1024);
        //标记AAC类型
        mediaFormat.setInteger(MediaFormat.KEY_AAC_PROFILE,
                MediaCodecInfo.CodecProfileLevel.AACObjectLC);
        //单声道是所有设备都支持的
        mediaFormat.setInteger(MediaFormat.KEY_CHANNEL_MASK, AudioFormat.CHANNEL_IN_MONO);
        //初始化编码器
        mediaCodec = MediaCodec.createEncoderByType(MIME);
        mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
        mediaCodec.start();
        //记录编码完成的缓存的信息
        bufferInfo = new MediaCodec.BufferInfo();
    }

    @Override
    public void run() {
        Looper.prepare();
        myHandler = new MyHandler(Looper.myLooper(), this);
        Looper.loop();
    }

    /**
     * PCM转码成AAC
     */
    private int encodeToAAC() {
        if (ListUtil.isListEmpty(mTasks)) {
            return 0;
        }
        Task task = mTasks.remove(0);
        // TODO: 2018/3/27 API20以下的系统，此处debug不下去，应该是MediaCodec参数配置未兼容好。
        int inputBufferIndex = mediaCodec.dequeueInputBuffer(-1);
        if (inputBufferIndex > 0) {
            ByteBuffer inputBuffer = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                inputBuffer = mediaCodec.getInputBuffer(inputBufferIndex);
            } else {
                inputBuffer = mediaCodec.getInputBuffers()[inputBufferIndex];
            }
            inputBuffer.clear();
            inputBuffer.limit(task.getData().length);
            inputBuffer.put(task.getData());
            mediaCodec.queueInputBuffer(inputBufferIndex, 0, task.getData().length, 0, 0);
        }

        int outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 10000);
        try {
            while (outputBufferIndex >= 0) {
                int outBitSize = bufferInfo.size;
                // 7 为adts头部大小（byte）
                int outPacketSize = outBitSize + 7;
                ByteBuffer outBuffer = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    outBuffer = mediaCodec.getOutputBuffer(outputBufferIndex);
                } else {
                    outBuffer = mediaCodec.getOutputBuffers()[outputBufferIndex];
                }
                outBuffer.position(bufferInfo.offset);
                outBuffer.limit(bufferInfo.offset + outBitSize);
                //添加adts头
                byte[] bytes = new byte[outPacketSize];
                addADTStoPacket(bytes, outPacketSize, 4, 1);
                //将ByteBuffer内的数据偏移7字节写入到bytes数组中
                outBuffer.get(bytes, 7, outBitSize);
                outBuffer.position(bufferInfo.offset);

                //将转码好的数据通过输出流写入文件
                dataOutputStream.write(bytes, 0, bytes.length);

                mediaCodec.releaseOutputBuffer(outputBufferIndex, false);
                outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 10000);
            }
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mTasks.size();
    }

    /**
     * 给编码出的aac流添加adts头字段
     *
     * @param packet    AAC原始流数据
     * @param packetLen 元数据长度
     * @param freqIdx   采样率下标
     * @param chanCfg   通道数
     */
    private void addADTStoPacket(byte[] packet, int packetLen, int freqIdx, int chanCfg) {
        int profile = 2;  //AAC LC
        packet[0] = (byte) 0xFF;
        packet[1] = (byte) 0xF9;
        packet[2] = (byte) (((profile - 1) << 6) + (freqIdx << 2) + (chanCfg >> 2));
        packet[3] = (byte) (((chanCfg & 3) << 6) + (packetLen >> 11));
        packet[4] = (byte) ((packetLen & 0x7FF) >> 3);
        packet[5] = (byte) (((packetLen & 7) << 5) + 0x1F);
        packet[6] = (byte) 0xFC;
    }

    /**
     * 给编码出的aac流添加adts头字段
     *
     * @param packet    要空出前7个字节，否则会搞乱数据
     * @param packetLen
     */

    public void addTask(byte[] rawData, int readSize) {
        mTasks.add(new Task(rawData, readSize));
        myHandler.sendEmptyMessage(0);
    }

    static class MyHandler extends Handler {
        private EncodeRunnable encodeRunnable;

        public MyHandler(Looper looper, EncodeRunnable encodeRunnable) {
            super(looper);
            this.encodeRunnable = encodeRunnable;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    encodeRunnable.encodeToAAC();
                    break;
                case 1:
                    while (encodeRunnable.encodeToAAC() > 0) {
                    }
                    // TODO: 2018/3/27 这里加一个addWindow的封装dialog就好了。
                    //转码完毕，自动播放
                    try {
                        encodeRunnable.initMediaPlayer();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    break;
            }
        }
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

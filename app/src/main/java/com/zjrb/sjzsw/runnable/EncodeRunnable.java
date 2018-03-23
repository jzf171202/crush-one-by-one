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

import com.czt.mp3recorder.util.LameUtil;
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
    private final String TAG = getClass().getSimpleName();
    private static final int DEFAULT_SAMPLING_RATE = 44100;
    private DataOutputStream dataOutputStream = null;
    private static final String MIME = MediaFormat.MIMETYPE_AUDIO_AAC;
    //TODO: 2018/3/16 synchronizedList的用法和原理，是否会因为阻塞导致上下游流速不平衡（背压引入）
    private List<Task> mTasks = Collections.synchronizedList(new ArrayList<Task>());
    private File recorderFile;
    private ByteBuffer[] inputBuffers;
    private ByteBuffer[] outputBuffers;
    private MediaCodec.BufferInfo bufferInfo;
    private MediaCodec mediaCodec;
    private MediaPlayer mediaPlayer;
    public MyHandler myHandler;
    private byte[] bytes;
    /**
     * 0表示AAC，1表示MP3
     */
    private int type = 0;

    public EncodeRunnable(int bufferSize) {
        try {
            switch (this.type) {
                case 0:
                    recorderFile = new File(FileUtil.getDiskCacheDir("audio"), System
                            .currentTimeMillis() + ".aac");
                    initMediaCodec();
                    break;
                case 1:
                    recorderFile = new File(FileUtil.getDiskCacheDir("audio"), System
                            .currentTimeMillis() + ".mp3");
                    //Lame官方规定了计算公式：7200 + (1.25 * buffer.length)。（可以在lame.h文件中看到）
                    bytes = new byte[(int) (7200 + (bufferSize * 2 * 1.25))];
                    break;
                default:
                    break;
            }
            dataOutputStream = new DataOutputStream(new BufferedOutputStream(new
                    FileOutputStream(recorderFile)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                    encodeRunnable.encode();
                    break;
                case 1:
                    // TODO: 2018/3/23 MP3的收尾未做
                    while (encodeRunnable.encode() > 0) {
                    }

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

    ;

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

    /**
     * 初始化编码器
     *
     * @throws IOException
     */
    private void initMediaCodec() throws IOException {
        //转码类型(mp3为audio/mpeg；aac为audio/mp4a-latm；mp4为video/mp4v-es)，采样率，声道数
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

        inputBuffers = mediaCodec.getInputBuffers();
        outputBuffers = mediaCodec.getOutputBuffers();
        bufferInfo = new MediaCodec.BufferInfo();
    }

    @Override
    public void run() {
        Looper.prepare();
        myHandler = new MyHandler(Looper.myLooper(), this);
        Looper.loop();
    }

    private int encode() {
        if (ListUtil.isListEmpty(mTasks)) {
            return 0;
        }
        Task task = mTasks.remove(0);
        switch (type) {
            case 0:
                encodeToAAC(task);
                break;
            case 1:
                encodeToMp3(task);
                break;
            default:
                break;
        }
        return mTasks.size();
    }

    /**
     * PCM转码成MP3
     *
     * @param task
     */
    private void encodeToMp3(Task task) {
        try {
            short[] leftData = ListUtil.byteToShort(task.getData());
            byte[] bytes2 = ListUtil.shortToByte(leftData);

            //当前声道选的是单声道，因此bufferLeft和bufferRight传入一样的buffer。
            int encodedSize = LameUtil.encode(leftData, leftData, task.getReadSize(), bytes);
            if (encodedSize > 0) {
                dataOutputStream.write(bytes, 0, encodedSize);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (dataOutputStream != null) {
                try {
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    dataOutputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * PCM转码成AAC
     *
     * @param task
     */
    private void encodeToAAC(Task task) {
        int inputBufferIndex = mediaCodec.dequeueInputBuffer(-1);
        if (inputBufferIndex > 0) {
            ByteBuffer byteBuffer = inputBuffers[inputBufferIndex];
            byteBuffer.clear();
            byteBuffer.limit(task.getData().length);
            byteBuffer.put(task.getData());
            mediaCodec.queueInputBuffer(inputBufferIndex, 0, task.getData().length, 0, 0);
        }

        int outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 10000);
        try {
            while (outputBufferIndex >= 0) {
                int outBitSize = bufferInfo.size;
                // 7 为adts头部大小
                int outPacketSize = outBitSize + 7;
                ByteBuffer byteBuffer = outputBuffers[outputBufferIndex];
                byteBuffer.position(bufferInfo.offset);
                byteBuffer.limit(bufferInfo.offset + outBitSize);
                //添加adts头
                byte[] bytes = new byte[outPacketSize];
                addADTStoPacket(bytes, outPacketSize);
                byteBuffer.get(bytes, 7, outBitSize);
                byteBuffer.position(bufferInfo.offset);

                dataOutputStream.write(bytes, 0, bytes.length);

                mediaCodec.releaseOutputBuffer(outputBufferIndex, false);
                outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 10000);
            }
            dataOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (dataOutputStream != null) {
                try {
                    dataOutputStream.flush();
                    dataOutputStream.close();
                    dataOutputStream = null;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 给编码出的aac流添加adts头字段
     *
     * @param packet    要空出前7个字节，否则会搞乱数据
     * @param packetLen
     */
    private void addADTStoPacket(byte[] packet, int packetLen) {
        int profile = 2;  //AAC LC
        int freqIdx = 4;  //44.1KHz
        int chanCfg = 2;  //CPE
        packet[0] = (byte) 0xFF;
        packet[1] = (byte) 0xF9;
        packet[2] = (byte) (((profile - 1) << 6) + (freqIdx << 2) + (chanCfg >> 2));
        packet[3] = (byte) (((chanCfg & 3) << 6) + (packetLen >> 11));
        packet[4] = (byte) ((packetLen & 0x7FF) >> 3);
        packet[5] = (byte) (((packetLen & 7) << 5) + 0x1F);
        packet[6] = (byte) 0xFC;
    }


    public void addTask(byte[] rawData, int readSize) {
        mTasks.add(new Task(rawData, readSize));
        myHandler.sendEmptyMessage(0);
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

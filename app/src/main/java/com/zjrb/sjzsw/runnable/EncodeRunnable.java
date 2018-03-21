package com.zjrb.sjzsw.runnable;

import android.media.AudioFormat;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.util.Log;

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
    private static final String TAG = "EncodeRunnable";
    private static final int DEFAULT_SAMPLING_RATE = 44100;
    private DataOutputStream dataOutputStream = null;
    private static final String MIME = MediaFormat.MIMETYPE_AUDIO_AAC;
    // TODO: 2018/3/16 synchronizedList的用法和原理，是否会因为阻塞导致上下游流速不平衡（背压引入）
    private List<Task> mTasks = Collections.synchronizedList(new ArrayList<Task>());

    private ByteBuffer[] inputBuffers;
    private ByteBuffer[] outputBuffers;
    private MediaCodec.BufferInfo bufferInfo;
    private MediaCodec mediaCodec;

    public EncodeRunnable(int bufferSize, File decodeFile) {
        try {
            dataOutputStream = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(decodeFile)));

            //转码类型，采样率，声道数
            MediaFormat mediaFormat =
                    MediaFormat.createAudioFormat(MIME, DEFAULT_SAMPLING_RATE, 1);
            //比特率
            mediaFormat.setInteger(MediaFormat.KEY_BIT_RATE, 48 * 1024);
            //采样率
            mediaFormat.setInteger(MediaFormat.KEY_SAMPLE_RATE, DEFAULT_SAMPLING_RATE);
            //标记AAC类型
            mediaFormat.setInteger(MediaFormat.KEY_AAC_PROFILE, MediaCodecInfo.CodecProfileLevel.AACObjectLC);
            //CHANNEL_IN_STEREO 立体声
            mediaFormat.setInteger(MediaFormat.KEY_CHANNEL_MASK, AudioFormat.CHANNEL_IN_MONO);
            //音频格式频道数
            mediaFormat.setInteger(MediaFormat.KEY_CHANNEL_COUNT, 1);
            //传入的数据大小
//        mediaFormat.setInteger(MediaFormat.KEY_MAX_INPUT_SIZE, 1024 * 1024);
            //用来标记AAC是否有adts头，1-有
//        mediaFormat.setInteger(MediaFormat.KEY_IS_ADTS, 1);
            //初始化编码器
            mediaCodec = MediaCodec.createEncoderByType(MIME);
            mediaCodec.configure(mediaFormat, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
            mediaCodec.start();

            inputBuffers = mediaCodec.getInputBuffers();
            outputBuffers = mediaCodec.getOutputBuffers();
            bufferInfo = new MediaCodec.BufferInfo();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            if (!ListUtil.isListEmpty(mTasks)) {
                encode();
            }
        }
    }

    private void encode() {
        if (ListUtil.isListEmpty(mTasks)) {
            return;
        }
        Task task = mTasks.remove(0);
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

                Log.d(TAG, "本次写入文件数据=" + bytes.length);
                dataOutputStream.write(bytes, 0, bytes.length);

                mediaCodec.releaseOutputBuffer(outputBufferIndex, false);
                outputBufferIndex = mediaCodec.dequeueOutputBuffer(bufferInfo, 10000);
            }

//            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 给编码出的aac裸流添加adts头字段
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

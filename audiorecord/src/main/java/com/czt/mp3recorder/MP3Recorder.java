package com.czt.mp3recorder;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Process;

import com.czt.mp3recorder.util.LameUtil;

import java.io.File;
import java.io.IOException;

public class MP3Recorder {
	//=======================AudioRecord Default Settings=======================
	private static final int DEFAULT_AUDIO_SOURCE = MediaRecorder.AudioSource.MIC;
	/**
	 * 以下三项为默认配置参数。Google Android文档明确表明只有以下3个参数是可以在所有设备上保证支持的。
	 */
	private static final int DEFAULT_SAMPLING_RATE = 44100;//模拟器仅支持从麦克风输入8kHz采样率
	private static final int DEFAULT_CHANNEL_CONFIG = AudioFormat.CHANNEL_IN_MONO;
	/**
	 * 下面是对此的封装
	 * private static final int DEFAULT_AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
	 */
	private static final PcmSampleFormat DEFAULT_AUDIO_FORMAT = PcmSampleFormat.PCM_16BIT;
	
	//======================Lame Default Settings=====================
	private static final int DEFAULT_LAME_MP3_QUALITY = 7;
	/**
	 * 与DEFAULT_CHANNEL_CONFIG相关，因为是mono单声，所以是1
	 */
	private static final int DEFAULT_LAME_IN_CHANNEL = 1;
	/**
	 *  Encoded bit rate. MP3 file will be encoded with bit rate 32kbps 
	 */ 
	private static final int DEFAULT_LAME_MP3_BIT_RATE = 32;
	
	//==================================================================
	
	/**
	 * 自定义 每160帧作为一个周期，通知一下需要进行编码
	 */
	private static final int FRAME_COUNT = 160;
	private AudioRecord mAudioRecord = null;
	private int mBufferSize;
	private short[] mPCMBuffer;
	private DataEncodeThread mEncodeThread;
	private boolean mIsRecording = false;
	private File mRecordFile;
	/**
	 * Default constructor. Setup recorder with default sampling rate 1 channel,
	 * 16 bits pcm
	 * @param recordFile target file
	 */
	public MP3Recorder(File recordFile) {
		mRecordFile = recordFile;
	}

	/**
	 * Start recording. Create an encoding thread. Start record from this
	 * thread.
	 * 
	 * @throws IOException  initAudioRecorder throws
	 */
	public void start() throws IOException {
		if (mIsRecording) {
			return;
		}
		mIsRecording = true; // 提早，防止init或startRecording被多次调用
	    initAudioRecorder();
		mAudioRecord.startRecording();

		new Thread() {
			@Override
			public void run() {
				//设置线程优先级 声音线程最高级别
				Process.setThreadPriority(Process.THREAD_PRIORITY_URGENT_AUDIO);
				while (mIsRecording) {
					int readSize = mAudioRecord.read(mPCMBuffer, 0, mBufferSize);
					if (readSize > 0) {
						//往转码线程中的转码队列中添加缓存，类似歌曲批量下载
						mEncodeThread.addTask(mPCMBuffer, readSize);
						// TODO: 2018/3/9 怎么没有对mPCMBuffer清理呢？
					}
				}
				// release and finalize audioRecord
				mAudioRecord.stop();
				mAudioRecord.release();
				mAudioRecord = null;
				// stop the encoding thread and try to wait
				// until the thread finishes its job
				mEncodeThread.sendStopMessage();
			}
		}.start();
	}
	private int mVolume;

	/**
	 * 获取真实的音量。 [算法来自三星]
	 * @return 真实音量
     */
	public int getRealVolume() {
		return mVolume;
	}

	/**
	 * 获取相对音量。 超过最大值时取最大值。
	 * @return 音量
     */
	public int getVolume(){
		if (mVolume >= MAX_VOLUME) {
			return MAX_VOLUME;
		}
		return mVolume;
	}
	private static final int MAX_VOLUME = 2000;

	/**
	 * 根据资料假定的最大值。 实测时有时超过此值。
	 * @return 最大音量值。
     */
	public int getMaxVolume(){
		return MAX_VOLUME;
	}
	public void stop(){
		mIsRecording = false;
	}
	public boolean isRecording() {
		return mIsRecording;
	}
	/**
	 * Initialize audio recorder
	 */
	private void initAudioRecorder() throws IOException {
		//最小缓存字节数byte
		mBufferSize = AudioRecord.getMinBufferSize(DEFAULT_SAMPLING_RATE,
				DEFAULT_CHANNEL_CONFIG, DEFAULT_AUDIO_FORMAT.getAudioFormat());

		//AudioFormat.ENCODING_PCM_16BIT 每份采样数据（采样大小）为PCM 16bit 即位宽2byte
		int bytesPerFrame = DEFAULT_AUDIO_FORMAT.getBytesPerSample();
		/* Get number of samples. Calculate the buffer size 
		 * (round up to the factor of given frame size) 
		 * 使能被整除，方便下面的周期性通知
		 * */

		//得出该最小缓存值情况下需多少次采样 frame不是音频帧，因为一音频帧的数据（int size = 采样率 x 位宽 x 采样时间 x 通道数）
		/**
		 * frame不是音频帧
		 */
		// TODO: 2018/3/9 他这样计算是把AudioFormat.ENCODING_PCM_16BIT当做每帧数据为16bit即2byte了
		int frameSize = mBufferSize / bytesPerFrame;
		//确保frameSize份采样数据是设定周期FRAME_COUNT的整数倍

		// TODO: 2018/3/9 ？单位不统一哦，前面的是采样个数，后面是音频帧数
		if (frameSize % FRAME_COUNT != 0) {
			/**
			 * 如frameSize是340，FRAME_COUNT是160，frameSize % FRAME_COUNT就是20，
			 * 160-20=140，140+frameSize的340=480，这样的frameSize正好是FRAME_COUNT整数倍。
			 */
			frameSize += (FRAME_COUNT - frameSize % FRAME_COUNT);
			//保证缓存区大小是FRAME_COUNT整数倍，便于周期性通知
			mBufferSize = frameSize * bytesPerFrame;
		}
		
		/* Setup audio recorder */
		mAudioRecord = new AudioRecord(DEFAULT_AUDIO_SOURCE,
				DEFAULT_SAMPLING_RATE, DEFAULT_CHANNEL_CONFIG, DEFAULT_AUDIO_FORMAT.getAudioFormat(),
				mBufferSize);

		mPCMBuffer = new short[mBufferSize];
		/*
		 * Initialize lame buffer
		 * mp3 sampling rate is the same as the recorded pcm sampling rate 
		 * The bit rate is 32kbps
		 * 
		 */
		LameUtil.init(DEFAULT_SAMPLING_RATE, DEFAULT_LAME_IN_CHANNEL, DEFAULT_SAMPLING_RATE,
				DEFAULT_LAME_MP3_BIT_RATE, DEFAULT_LAME_MP3_QUALITY);
		// Create and run thread used to encode data
		// The thread will 
		mEncodeThread = new DataEncodeThread(mRecordFile, mBufferSize);
		mEncodeThread.start();
		mAudioRecord.setRecordPositionUpdateListener(mEncodeThread, mEncodeThread.getHandler());
		//录音来通知编码线程开始工作，以160帧为一个单位。要确保缓冲区大小(字节)是FRAME_COUNT（帧）的整数倍
		mAudioRecord.setPositionNotificationPeriod(FRAME_COUNT);
	}
}
package com.zjrb.sjzsw.ui.activity;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;

import com.czt.mp3recorder.MP3Recorder;
import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.manager.ThreadPoolManager;
import com.zjrb.sjzsw.runnable.RecordRunnable;
import com.zjrb.sjzsw.utils.FileUtil;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 类描述：音视频录制及合成
 *
 * @author jinzifu
 * @Email jinzifu123@163.com
 * @date 2017/12/13 1737
 */

public class AudioVideoActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    @BindView(R.id.surfaceview)
    SurfaceView surfaceview;
    private boolean isRecording = false;
    private RecordRunnable recordRunnable;
//    private PlayRunnable playRunnable;
    private File recorderFile;
    private MP3Recorder mRecorder = new MP3Recorder(new File(Environment.getExternalStorageDirectory(), "test.mp3"));

    @Override
    protected int getLayoutId() {
        return R.layout.ac_audiovideo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        //创建一个文件，用于存储录制内存
        File file = FileUtil.getDiskCacheDir("media");
        try {
            recorderFile = File.createTempFile("recording", ".pcm", file);
            Log.d(TAG, "recorderFile=" + recorderFile.getName());
        } catch (IOException e) {
            e.printStackTrace();
        }
//        playRunnable = new PlayRunnable(recorderFile);
    }

    @OnClick({R.id.record_audio, R.id.play_audio, R.id.record_vedio, R.id.play_vedio, R.id.record_audio_vedio, R.id.play_audio_vedio})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.record_audio:
                isRecording = !isRecording;
                if (recordRunnable == null) {
                    ThreadPoolManager.getInstance().execute(recordRunnable = new RecordRunnable(recorderFile));
                }
                recordRunnable.setRecroding(isRecording);
                showToast(isRecording == true ? "开始" : "暂停");
                break;
            case R.id.play_audio:
//                if (!isRecording) {
//                    ThreadPoolManager.getInstance().execute(playRunnable);
//                }
                break;
            case R.id.record_vedio:
                break;
            case R.id.play_vedio:
                isRecording = !isRecording;
                if (isRecording){
                    try {
                        mRecorder.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    mRecorder.stop();
                }
                break;
            case R.id.record_audio_vedio:
                break;
            case R.id.play_audio_vedio:
                break;
            default:
                break;
        }
    }
}
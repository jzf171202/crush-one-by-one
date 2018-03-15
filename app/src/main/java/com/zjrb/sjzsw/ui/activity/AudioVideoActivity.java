package com.zjrb.sjzsw.ui.activity;

import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.manager.ThreadPoolManager;
import com.zjrb.sjzsw.runnable.RecordRunnable;
import com.zjrb.sjzsw.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
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
    //音频是否录制
    private boolean isAudioRecording = false;
    private RecordRunnable recordRunnable = null;
    private File recorderFile;
//    private MP3Recorder mRecorder = new MP3Recorder(new File(Environment.getExternalStorageDirectory(), "test.mp3"));

    @Override
    protected int getLayoutId() {
        return R.layout.ac_audiovideo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        File file = FileUtil.getDiskCacheDir("media");
        try {
            recorderFile = File.createTempFile("recording", ".pcm", file);
            ThreadPoolManager.getInstance().execute(recordRunnable = new RecordRunnable(recorderFile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.record_audio, R.id.play_audio, R.id.record_vedio, R.id.play_vedio, R.id.record_audio_vedio, R.id.play_audio_vedio})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.record_audio:
                isAudioRecording = !isAudioRecording;
                if (isAudioRecording) {
                    try {
                        recordRunnable.startRecord();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } else {
                    recordRunnable.stopRecord();
                }
                showToast(isAudioRecording == true ? "开始" : "停止");
                break;
            case R.id.play_audio:
                break;
            case R.id.record_vedio:
                break;
            case R.id.play_vedio:
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
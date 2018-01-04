package com.zjrb.sjzsw.ui.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.manager.ThreadPoolManager;
import com.zjrb.sjzsw.runnable.PlayRunnable;
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
    @BindView(R.id.start_audio)
    Button startAudio;
    private boolean isRecodering = false;
    private RecordRunnable recordRunnable;
    private PlayRunnable playRunnable;
    private File recorderFile;

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
        playRunnable = new PlayRunnable(recorderFile);
    }

    @OnClick({R.id.play_audio, R.id.start_audio})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.play_audio:
                ThreadPoolManager.getInstance().execute(playRunnable);
                break;
            case R.id.start_audio:
                isRecodering = !isRecodering;
                if (recordRunnable == null) {
                    ThreadPoolManager.getInstance().execute(recordRunnable = new RecordRunnable(recorderFile));
                }
                recordRunnable.setRecroding(isRecodering);
                showToast(isRecodering == true ? "开始" : "暂停");
                break;
            default:
                break;
        }
    }
}
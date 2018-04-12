package com.zjrb.sjzsw.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.ui.fragment.AnimatorFragment;
import com.zjrb.sjzsw.ui.fragment.CustomViewFragment;
import com.zjrb.sjzsw.ui.fragment.NetFragment;
import com.zjrb.sjzsw.ui.fragment.Rxjava2Fragment;
import com.zjrb.sjzsw.ui.fragment.ThreadFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * @author jinzifu
 */
public class MainActivity extends BaseActivity {
    private final String TAG = getClass().getSimpleName();
    @BindView(R.id.leftImage)
    ImageButton leftImage;
    @BindView(R.id.titleText)
    TextView titleText;

    @Override
    protected int getLayoutId() {
        return R.layout.ac_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        leftImage.setVisibility(View.INVISIBLE);
        selectFragment(2);
    }

    /**
     * 切换不同的fragment
     *
     * @param type
     */
    private void selectFragment(int type) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (type) {
            case 2:
                fragmentTransaction.replace(R.id.fragment_id, new NetFragment());
                titleText.setText("网络架构");
                break;
            case 3:
                fragmentTransaction.replace(R.id.fragment_id, new Rxjava2Fragment());
                titleText.setText("RxJava2");
                break;
            case 4:
                fragmentTransaction.replace(R.id.fragment_id, new ThreadFragment());
                titleText.setText("线程池");
                break;
            case 5:
                fragmentTransaction.replace(R.id.fragment_id, new AnimatorFragment());
                titleText.setText("动画");
                break;
            case 6:
                fragmentTransaction.replace(R.id.fragment_id, new CustomViewFragment());
                titleText.setText("自定义view");
                break;
            default:
                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(TAG, "dispatchTouchEvent= super.dispatchTouchEvent(ev)");
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        Log.d(TAG, "onTouchEvent= super.onTouchEvent(ev)");
        return super.onTouchEvent(event);
    }
}
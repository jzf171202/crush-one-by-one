package com.zjrb.sjzsw.ui.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.ui.fragment.ImageFragment;
import com.zjrb.sjzsw.ui.fragment.NetFragment;
import com.zjrb.sjzsw.ui.fragment.Rxjava2Fragment;
import com.zjrb.sjzsw.ui.fragment.SeekBarFragment;
import com.zjrb.sjzsw.ui.fragment.ThreadFragment;

import butterknife.ButterKnife;


/**
 * @author jinzifu
 */
public class MainActivity extends BaseActivity {

    @Override
    protected int getLayoutId() {
        return R.layout.ac_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_main);
        ButterKnife.bind(this);
        selectFragment(5);
    }

    /**
     * 切换不同的fragment
     *
     * @param type
     */
    private void selectFragment(int type) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (type) {
            case 1:
                fragmentTransaction.replace(R.id.fragment_id, new ImageFragment());
                break;
            case 2:
                fragmentTransaction.replace(R.id.fragment_id, new NetFragment());
                break;
            case 3:
                fragmentTransaction.replace(R.id.fragment_id, new Rxjava2Fragment());
                break;
            case 4:
                fragmentTransaction.replace(R.id.fragment_id, new ThreadFragment());
                break;
            case 5:
                fragmentTransaction.replace(R.id.fragment_id, new SeekBarFragment());
                break;
            default:
                break;
        }
        fragmentTransaction.commit();
    }
}
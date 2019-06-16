package com.zjrb.sjzsw.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.zjrb.sjzsw.R;
import com.zjrb.sjzsw.widget.animation.AnimationView;

public class TestActvity extends AppCompatActivity {
    AnimationView animationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_test);
        animationView = findViewById(R.id.test);
        animationView.setOnClickListener(v -> {
//            testMath();
            testAnimation();
        });
    }

    private void testAnimation() {
        Toast.makeText(this, "执行动画", Toast.LENGTH_SHORT).show();
        animationView.postDelayed(() -> {
//            Animation animation = AnimationUtils.loadAnimation(TestActvity.this, R.anim.view_anim);
//            animation.setInterpolator(new OvershootInterpolator());
//            animationView.startAnimation(animation);
        }, 800);
    }

    private void testMath() {
        int[] originArray = new int[]{1, 2, 3, 5, 4, 6, 31, 8, 93, 89, 87, 54, 76, 32, 84, 26, 17, 28};
        int length = originArray.length;
        int temp = 0;


    }
}

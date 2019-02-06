package com.zjrb.sjzsw.ui.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class TestActvity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new CustomerView(this));
    }

    class CustomerView extends View {
        Paint paint1, paint2, paint3;

        public CustomerView(Context context) {
            super(context);
            paint1 = new Paint();//圆饼画笔
            paint1.setAntiAlias(true);//抗锯齿
            paint1.setStrokeWidth(2);//画笔宽度
            paint1.setColor(Color.RED);//画笔颜色

            paint2 = new Paint();//圆心画笔
            paint2.setAntiAlias(true);
            paint2.setColor(Color.YELLOW);

            paint3 = new Paint();//文本画笔
            paint3.setAntiAlias(true);
            paint3.setTextSize(30);//文本大小
            paint3.setColor(Color.WHITE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);
            //绘制圆饼
            canvas.drawCircle(300, 300, 200, paint1);
            //绘制圆心
            canvas.drawCircle(300, 300, 10, paint2);
            //绘制文本
            canvas.drawText("圆心", 320, 310, paint3);
        }
    }
}

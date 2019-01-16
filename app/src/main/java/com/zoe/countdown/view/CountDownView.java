package com.zoe.countdown.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by zz on 2019/1/7.
 */

public class CountDownView extends LinearLayout {
    public CountDownView(Context context) {
        super(context);
        init(context);
    }
    public CountDownView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public CountDownView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private Handler mHandler = new Handler();
    private long time = 60 * 60 * 2 + 60*40 + 50;
    private TextView hourText, minutesText;//, minutes, second, hour;
    private VerticalWheelView second,minutes,hour;
    private void init(Context context) {
        setGravity(Gravity.CENTER);
        hour = new VerticalWheelView(context);
        minutes = new VerticalWheelView(context);
        second = new VerticalWheelView(context);
        hourText = getTextUnit(context);
        minutesText = getTextUnit(context);
        TextView secondText = getTextUnit(context);
        LayoutParams params = new LayoutParams(dp2px(29),dp2px(36));
        params.gravity = Gravity.CENTER_VERTICAL;
        addView(hour, params);
        addView(hourText);
        addView(minutes, params);
        addView(minutesText);
        addView(second, params);
        addView(secondText);

        hourText.setText("时");
        minutesText.setText("分");
        secondText.setText("秒");
        secondText.setPadding(dp2px(5),0,0,0);
    }

    private TextView getTextUnit(Context context){
        TextView tv = new TextView(context);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 19);
        tv.setPadding(dp2px(5),0,dp2px(5),0);
        tv.setTextColor(Color.BLACK);
        return tv;
    }

    public void setTime(long l){
        time = l / 1000;
        if(time > 24 * 60 * 60) {
            return;
        }
        h = (int) (time / 3600);
        m = (int) ((time - h * 3600) / 60);
        s = (int) (time - h * 3600 - m * 60);

        /*实际数据
        second.refreshData(60);
        second.setStart(s);
        minutes.refreshData(60);
        minutes.setStart(m);
        hour.refreshData(24);
        hour.setStart(h);
        second.start();
        */
        //测试数据 start
        second.refreshData(4);
        second.setStart(3);
        minutes.refreshData(3);
        minutes.setStart(2);
        hour.refreshData(3);
        hour.setStart(2);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                second.start();
            }
        }, 2000);
        //测试数据 end
        second.setOnPeriodEndListener(new VerticalWheelView.OnPeriodEndListener() {
            @Override
            public boolean onPeriodEnd() {
                return minutes.anim();
            }
        });
        minutes.setOnPeriodEndListener(new VerticalWheelView.OnPeriodEndListener() {
            @Override
            public boolean onPeriodEnd() {
                return hour.anim();
            }
        });
    }
    private int h, m, s;

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        release();
    }

    private int dp2px(int dp){
        return (int) (getResources().getDisplayMetrics().density * dp);
    }

    public void release(){
        try {
            mHandler.removeCallbacksAndMessages(null);
        }catch (Exception e){

        }
    }
}

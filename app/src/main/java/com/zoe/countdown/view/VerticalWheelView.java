package com.zoe.countdown.view;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zoe.countdown.R;

import java.util.ArrayList;
import java.util.List;

public class VerticalWheelView extends FrameLayout implements Handler.Callback {
    private static final String TAG = "WheelView";
    public VerticalWheelView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }
    public VerticalWheelView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }
    public VerticalWheelView(@NonNull Context context) {
        super(context);
        init(context);
    }
    private Handler mHandler;
    private TextView[] texts = new TextView[2];
    private List<Integer> mList;

    private static final int TIME_DETAIL = 1000;
    private static final int TIME_ANIMATION = 500;

    private static final int MSG_START_ANIM = 102;
    private static final int MSG_START_ANIM1 = 103;
    private static final int MSG_INIT_POSITION = 104;
    private int count = 0;
    private int len = 0;
    private int index = 0;
    private void init(Context context){
        mHandler = new Handler(Looper.getMainLooper(), this);
        texts[0] = getText(context);
        texts[1] = getText(context);
    }
    private TextView getText(Context context){
        TextView tv = new TextView(context);
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundResource(R.drawable.time_bg_radius);
        tv.setTextColor(getResources().getColor(R.color.yellow_713A04));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 21);
        return tv;
    }

    public void refreshData(int number){
        mList = new ArrayList<>();
        for(int i = number -1; i >= 0; i--){
            mList.add(i);
        }
    }


    public void setStart(int startPosition){
        len = mList.size();
        index = 0;
        count = 60 - startPosition%60;
        if(len == 0) return;
        setText(texts[0], mList.get(count % len));
        setText(texts[1], mList.get(++count % len));
    }
    public void start(){
        isAuto = true;
        mHandler.removeCallbacksAndMessages(null);
        mHandler.sendEmptyMessageDelayed(MSG_START_ANIM,TIME_DETAIL);
    }

    private boolean isFirst = true;
    private boolean isAuto = false;
    public boolean anim(){
        isAuto = false;
        if(isFirst) {
            isFirst = false;
            return startAnim();
        }else {
            return startAnim1();
        }
    }

    private void stop(){
        mHandler.removeCallbacksAndMessages(null);
    }

    private void setText(TextView tv, int num){
        if(num < 10){
            tv.setText("0"+num);
        }else {
            tv.setText(String.valueOf(num));
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if(msg.what == MSG_START_ANIM) {
            startAnim();
        }else if(msg.what == MSG_START_ANIM1){
            startAnim1();
        } else if(msg.what == MSG_INIT_POSITION){
            animStartPosition(getNextIndex());
        }
        return false;
    }

    private void animStartPosition(int index){
        if(len > 0) {
            count++;
            setText(texts[index], mList.get(count % len));
        }
        removeView(texts[index]);
        addView(texts[index], 0, params);
    }

    private int getNextIndex(){
        return index == 0 ? 1 : 0;
    }
    private boolean startAnim() {
        if (mList.get(count % len) == len - 1){
            if(mListener == null){
                return true;
            }else {
                if(mListener.onPeriodEnd()){
                    return true;
                }
            }
        }
        if (isAuto){
            mHandler.sendEmptyMessageDelayed(MSG_START_ANIM1, TIME_DETAIL);
        }
        mHandler.sendEmptyMessageDelayed(MSG_INIT_POSITION,TIME_ANIMATION+100);
        index = getNextIndex();
        PropertyValuesHolder pvhX1 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0, height);
        ObjectAnimator.ofPropertyValuesHolder(texts[getNextIndex()], pvhX1).setDuration(TIME_ANIMATION-100).start();

        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0, height);
        ObjectAnimator.ofPropertyValuesHolder(texts[index], pvhX).setDuration(TIME_ANIMATION).start();
        return false;
    }
    private boolean startAnim1(){
        if (mList.get(count % len) == len - 1){
            if(mListener == null){
                return true;
            }else {
                if(mListener.onPeriodEnd()){
                    return true;
                }
            }
        }
        if (isAuto){
            mHandler.sendEmptyMessageDelayed(MSG_START_ANIM1, TIME_DETAIL);
        }
        mHandler.sendEmptyMessageDelayed(MSG_INIT_POSITION,TIME_ANIMATION+100);
        index = getNextIndex();
        PropertyValuesHolder pvhX1 = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, height, height * 2);
        ObjectAnimator.ofPropertyValuesHolder(texts[getNextIndex()], pvhX1).setDuration(TIME_ANIMATION-100).start();

        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y, 0, height);
        ObjectAnimator.ofPropertyValuesHolder(texts[index], pvhX).setDuration(TIME_ANIMATION).start();
        return false;
    }

    private int height;
    private LayoutParams params;
    @SuppressLint("DrawAllocation")
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(changed){
            height = bottom - top;
            params = new LayoutParams(right - left, height);
            params.topMargin = -height;
            addView(texts[index+1],params);
            addView(texts[index]);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        count = 0;
        isFirst = true;
        try {
            mHandler.removeCallbacksAndMessages(null);
        }catch (Exception e){

        }
    }
    private OnPeriodEndListener mListener;
    public void setOnPeriodEndListener(OnPeriodEndListener l){
        mListener = l;
    }
    public interface OnPeriodEndListener{
        boolean onPeriodEnd();
    }
}

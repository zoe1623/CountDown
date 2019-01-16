package com.zoe.countdown.view;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class VerticalRollTextView extends FrameLayout implements Handler.Callback {
    public VerticalRollTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    public VerticalRollTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    public VerticalRollTextView(@NonNull Context context) {
        super(context);
        init();
    }
    private Handler mHandler;
    private TextView tv;
    private List<String> mList;
    private static final int TIME_DETAIL = 4000;
    private static final int TIME_ANIMATION_IN = 300;
    private static final int TIME_ANIMATION_OUT = 800;
    private static final int MSG_IN = 100;
    private static final int MSG_OUT = 101;
    private int count = 0;
    private int len = 0;
    private TranslateAnimation animationIn, animationOut;
    private void init(){
        mHandler = new Handler(Looper.getMainLooper(), this);
        tv = new TextView(getContext());
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
        tv.setSingleLine(true);
        tv.setGravity(Gravity.CENTER);
        tv.setEllipsize(TextUtils.TruncateAt.END);
        tv.setTextColor(Color.WHITE);
        addView(tv);

        animationIn = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 1.0f, Animation.RELATIVE_TO_PARENT, 0.0f
        );
        animationIn.setDuration(TIME_ANIMATION_IN);

        animationOut = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, -1.0f
        );
        animationOut.setDuration(TIME_ANIMATION_OUT);
    }

    public void refreshData(List<String> list){
        if(list == null){
            mList = new ArrayList<>();
        }else {
            mList = list;
        }
        len = mList.size();
        mHandler.removeCallbacksAndMessages(null);
        if(len == 0) return;
        mHandler.removeCallbacksAndMessages(null);
        setText();
        mHandler.sendEmptyMessageDelayed(MSG_OUT,TIME_DETAIL - TIME_ANIMATION_OUT);
    }

    public void doAnimation(boolean isAnimation){
        if(len == 0) return;
        mHandler.removeCallbacksAndMessages(null);
        setText();
        if(isAnimation){
            mHandler.sendEmptyMessageDelayed(MSG_OUT,TIME_DETAIL - TIME_ANIMATION_OUT);
        }
    }

    @Override
    public boolean handleMessage(Message msg) {
        if(msg.what == MSG_OUT){
            animOut();
            mHandler.sendEmptyMessageDelayed(MSG_IN,TIME_ANIMATION_OUT);
        } else {
            count++;
            animIn();
            mHandler.sendEmptyMessageDelayed(MSG_OUT,TIME_DETAIL);
        }
        return false;
    }

    private void animOut(){
        if(tv != null) {
            tv.startAnimation(animationOut);
        }
    }
    private void animIn(){
        if(tv != null) {
            setText();
            tv.startAnimation(animationIn);
        }
    }

    private void setText(){
        if(len > 0) {
            tv.setText(mList.get(count % len));
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mHandler.removeCallbacksAndMessages(null);
    }
}

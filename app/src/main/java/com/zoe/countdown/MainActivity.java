package com.zoe.countdown;

import android.app.Activity;
import android.os.Bundle;

import com.zoe.countdown.view.CountDownView;
import com.zoe.countdown.view.VerticalRollTextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CountDownView c = (CountDownView) findViewById(R.id.count_down);
        c.setTime((60 * 60 + 60 * 10 + 10) * 1000);

        VerticalRollTextView rollTv = (VerticalRollTextView) findViewById(R.id.roll_tv);
        List<String> list = new ArrayList<>();
        list.add("鑫苑");
        list.add("慷宝机器人");
        list.add("爱接力科技");
        list.add("慷宝");
        rollTv.refreshData(list);
    }
}

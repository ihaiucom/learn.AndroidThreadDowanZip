package com.ihaiu.androidthreaddowanzip;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ihaiu.androidthreaddowanzip.aria.LearnAria;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 学习线程
//        new LearnThreadMain(this).run();
//        new LearnTask().setContext(this).init();
        new LearnAria().setContext(this).init();
    }

}

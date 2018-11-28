package com.ihaiu.androidthreaddowanzip;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ihaiu.androidthreaddowanzip.com.ihaiu.androidthreaddowanzip.learnthreads.LearnTask;
import com.ihaiu.androidthreaddowanzip.com.ihaiu.androidthreaddowanzip.learnthreads.LearnThreadMain;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 学习线程
//        new LearnThreadMain(this).run();
        new LearnTask().setContext(this).init();
    }
}

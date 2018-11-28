package com.ihaiu.androidthreaddowanzip.com.ihaiu.androidthreaddowanzip.learnthreads;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;


/***
 * 1.1 继承 Thread 类
 * 新建一个类继承自 Thread，然后重写父类的 run() 方法：
 */
public class CustomThreadA extends Thread
{
    public Context context;
    public static final int CHANGE_CONTENT = 1;

    public CustomThreadA setContext(Context context) {
        this.context = context;
        return  this;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CHANGE_CONTENT:
                    Toast.makeText(context, "计算结束=" + msg.arg1, Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void run()
    {

        int num = 1;
        for(int i = 1; i < 100000000; i ++)
        {
            num += i;
        }

        Log.i("LearnThread", String.format("计算结果 num=%d", num));
        Message message = new Message();
        message.what = CHANGE_CONTENT;
        message.arg1 = num;
        handler.sendMessage(message);

    }
}

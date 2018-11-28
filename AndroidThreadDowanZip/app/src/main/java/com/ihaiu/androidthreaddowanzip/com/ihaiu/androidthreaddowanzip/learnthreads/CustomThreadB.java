package com.ihaiu.androidthreaddowanzip.com.ihaiu.androidthreaddowanzip.learnthreads;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

/**
 * 1.2 实现 Runnable 接口
 * 一般采用此方法实现多线程，这样耦合度更低，而且可以实现类的扩展性更好，因为 Java 类支持实现多接口。
 * */
public class CustomThreadB implements Runnable
{
    public Context context;
    public static final int CHANGE_CONTENT = 1;

    public CustomThreadB setContext(Context context) {
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
        for(int i = 1; i < 300000000; i ++)
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

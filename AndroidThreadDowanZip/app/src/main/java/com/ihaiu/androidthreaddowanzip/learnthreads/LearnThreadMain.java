package com.ihaiu.androidthreaddowanzip.learnthreads;

import android.content.Context;

/**
 * 学习线程
 * */
public class LearnThreadMain
{
    public Context context;
    public CustomTask customTask;


    public  LearnThreadMain(Context context)
    {
        this.context = context;
    }
    public void run()
    {
//        new CustomThreadA().setContext(context).start();
//        new CustomThreadA().setContext(context).run();

//        new Thread(new CustomThreadB().setContext(context)).start();

        //使用匿名类来实现 Runnable 接口
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                int num = 1;
//                for(int i = 1; i < 30000000; i ++)
//                {
//                    num += i;
//                }
//
//                Log.i("LearnThread", String.format("计算结果 num=%d", num));
//            }
//        }).start();




        customTask = new CustomTask().setContext(context);
        /**
         * 步骤3：手动调用execute(Params... params) 从而执行异步线程任务
         * 注：
         *    a. 必须在UI线程中调用
         *    b. 同一个AsyncTask实例对象只能执行1次，若执行第2次将会抛出异常
         *    c. 执行任务中，系统会自动调用AsyncTask的一系列方法：onPreExecute() 、doInBackground()、onProgressUpdate() 、onPostExecute()
         *    d. 不能手动调用上述方法
         */
        customTask.execute(100);


    }
}

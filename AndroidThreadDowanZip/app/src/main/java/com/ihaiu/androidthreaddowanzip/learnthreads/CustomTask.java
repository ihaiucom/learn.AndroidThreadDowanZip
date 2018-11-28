package com.ihaiu.androidthreaddowanzip.learnthreads;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class CustomTask extends AsyncTask<Integer, Integer, Integer>
{
    public Context context;


    public CustomTask setContext(Context context) {
        this.context = context;
        return  this;
    }

    Toast toast;
    int count;



    // 方法1：onPreExecute（）
    // 作用：执行 线程任务前的操作
    @Override
    protected void onPreExecute() {
        toast = Toast.makeText(context, "执行 线程任务前的操作", Toast.LENGTH_LONG);
        toast.show();
    }

    // 方法2：doInBackground（）
    // 作用：接收输入参数、执行任务中的耗时操作、返回 线程任务执行的结果
    // 此处通过计算从而模拟“加载进度”的情况
    @Override
    protected Integer doInBackground(Integer ... args)
    {
        int num = 0;
        this.count = args[0];

        try
        {
            for(int i = 1; i <=  count; i ++)
            {
                for(int j = 0; j < 100000; j ++)
                {
                    num += j;
                }
                // 可调用publishProgress（）显示进度, 之后将执行onProgressUpdate（）
                publishProgress(i);
                // 模拟耗时任务
                Thread.sleep(50);
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        return num;
    }

    // 方法3：onProgressUpdate（）
    // 作用：在主线程 显示线程任务执行的进度
    @Override
    protected void onProgressUpdate(Integer... progresses)
    {
        int curr = progresses[0];
        int val = (int)(curr * 1f / count * 100);
        toast.setText(String.format("当前进度 %d / %d   (%d%%)", curr, count, val));
        toast.show();

    }

    // 方法4：onPostExecute（）
    // 作用：接收线程任务执行结果、将执行结果显示到UI组件
    @Override
    protected void onPostExecute(Integer result) {
        // 执行完毕后，则更新UI

        Toast.makeText(context, String.format("计算完成 num=%d", result), Toast.LENGTH_LONG).show();
    }

    // 方法5：onCancelled()
    // 作用：将异步任务设置为：取消状态
    @Override
    protected void onCancelled()
    {
        Toast.makeText(context, "已取消", Toast.LENGTH_LONG).show();

    }

}

package com.ihaiu.androidthreaddowanzip.aria;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadTask;
import com.ihaiu.androidthreaddowanzip.R;


public class LearnAria
{

    public AppCompatActivity context;


    public LearnAria setContext(AppCompatActivity context)
    {
        this.context = context;
        return  this;
    }

    public String TAG = "LearnAria";
    public String DOWNLOAD_URL = "https://ldc.layabox.com/download/1.8.0/win/LayaAirIDE_beta.zip";
    public String DOWNLOAD_PATH = "/data/data/com.ihaiu.androidthreaddowanzip/files/xxx";

    public Button startButton;
    public Button cancelButton;
    public Button stopButton;
    public ProgressBar progressBar;
    public TextView progressText;
    public TextView resultText;
    private LearnAria instance;


    public void unRegister() {
        Aria.download(this).unRegister();
    }

    public LearnAria init()
    {
        instance = this;
        DOWNLOAD_PATH = context.getFilesDir().getPath() + "/" + DOWNLOAD_URL.substring(DOWNLOAD_URL.lastIndexOf('/')+1);
        Log.i(TAG, "DOWNLOAD_URL=" + DOWNLOAD_URL);
        Log.i(TAG, "DOWNLOAD_PATH=" + DOWNLOAD_PATH);


        Aria.init(context);
        Aria.download(this).register();



        startButton = context.findViewById(R.id.startButton);
        cancelButton = context.findViewById(R.id.cancelButton);
        stopButton = context.findViewById(R.id.stopButton);
        progressBar = context.findViewById(R.id.progressBar);
        progressText = context.findViewById(R.id.progressText);
        resultText = context.findViewById(R.id.resutText);

        setButtonState(true);

        startButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Aria.download(instance)
                        .load(DOWNLOAD_URL)     //读取下载地址
                        .setDownloadPath(DOWNLOAD_PATH) //设置文件保存的完整路径
                        .start();   //启动下载
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Aria.download(instance).load(DOWNLOAD_URL).stop();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Aria.download(this).load(DOWNLOAD_URL).cancel();
            }
        });
        return this;
    }


    @Download.onWait
    protected void onWait(DownloadTask task)
    {
        Log.d(TAG, "wait ==> " + task.getDownloadEntity().getFileName());
    }

    @Download.onPre
    protected void onPre(DownloadTask task)
    {
        Log.d(TAG, "onPre");
    }


    @Download.onTaskPre
    public void onTaskPre(DownloadTask task)
    {
        setButtonState(false);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressText.setText("0%");

    }

    @Download.onTaskStart
    protected void taskStart(DownloadTask task)
    {
        Log.d(TAG, "onStart");
    }


    @Download.onTaskRunning
    protected void running(DownloadTask task) {
        if(task.getKey().equals(DOWNLOAD_URL))
        {
        }

        int percent = task.getPercent();  //任务进度百分比
        String speed = task.getConvertSpeed();  //转换单位后的下载速度，单位转换需要在配置文件中打开
        long speed1 = task.getSpeed(); //原始byte长度速度


        progressBar.setProgress(percent);
        progressText.setText(String.format("%d%% (%s/%s  %s)", percent, task.getConvertCurrentProgress(), task.getConvertFileSize() , speed));
    }

    @Download.onTaskComplete
    public void taskComplete(DownloadTask task)
    {
        //在这里处理任务完成的状态
        progressBar.setProgress(100);
        progressText.setText(String.format("%d%%", 100));
        resultText.setText("下载完成");
        setButtonState(true);
    }


    @Download.onTaskStop
    public void onTaskStop(DownloadTask task)
    {
        resultText.setText("停止下载");
        setButtonState(true);
    }

    @Download.onTaskCancel
    public void onTaskCancel(DownloadTask task)
    {
        resultText.setText("取消下载");
        setButtonState(true);
    }

    @Download.onTaskResume
    public void taskResume(DownloadTask task)
    {
        Log.d(TAG, "resume");
    }

    @Download.onTaskFail
    void taskFail(DownloadTask task)
    {
        Log.d(TAG, "fail");
    }



    private void setButtonState(boolean startEnable) {
        startButton.setEnabled(startEnable);
        cancelButton.setEnabled(!startEnable);
        stopButton.setEnabled(!startEnable);
    }
}

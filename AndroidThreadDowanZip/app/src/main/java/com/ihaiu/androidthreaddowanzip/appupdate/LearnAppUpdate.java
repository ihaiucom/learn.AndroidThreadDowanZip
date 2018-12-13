package com.ihaiu.androidthreaddowanzip.appupdate;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.arialyy.aria.core.Aria;
import com.game.appupdate.AppUpdate;
import com.game.appupdate.AppUpdateType;
import com.game.appupdate.IAppUpdateDownloadProgress;
import com.game.utils.SharedPreferencesKey;
import com.game.utils.SharedPreferencesUtil;
import com.ihaiu.androidthreaddowanzip.R;
import com.ihaiu.androidthreaddowanzip.learnthreads.LearnTask;
import com.updateapputils.util.UpdateAppUtils;

import java.io.File;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.logging.LogRecord;

public class LearnAppUpdate
{
    Activity context;

    public String TAG = "LearnAppUpdate";

    public String DOWNLOAD_URL = "http://mbqb.ihaiu.com/GamePF/apk/PF-V1.33.23_test_20181114_nofilters.apk";
    public String DOWNLOAD_PATH = "/data/data/com.ihaiu.androidthreaddowanzip/files/xxx";

    public Button startButton;
    public Button cancelButton;
    public Button stopButton;
    public ProgressBar progressBar;
    public TextView progressText;
    public TextView resultText;

    public LearnAppUpdate setContext(AppCompatActivity context)
    {
        this.context = context;
        return  this;
    }

    private LearnAppUpdate instance;

    public LearnAppUpdate init()
    {
        SharedPreferencesUtil.init(context);
        instance = this;
//        DOWNLOAD_URL = "https://downs.muzhiwan.com/2018/03/16/jp.co.hit_point.tabikaeru_5aabb96144155.apk";
        DOWNLOAD_PATH = context.getFilesDir().getPath() + "/" + DOWNLOAD_URL.substring(DOWNLOAD_URL.lastIndexOf('/')+1);
        DOWNLOAD_PATH = DOWNLOAD_URL.substring(DOWNLOAD_URL.lastIndexOf('/') + 1 );
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
//                new Thread(new DownloadRunable()).start();
//                StartDownloadManager(DOWNLOAD_URL, DOWNLOAD_PATH);
//                onClickUpdate();

                AppUpdate.init(context)
                    .setDownloadType(AppUpdateType.DowanloadManager)
                    .setApkUrl(DOWNLOAD_URL)
                    .setApkPath(DOWNLOAD_PATH)
                    .setProgressBar(new DownloadProgressBar())
                    .download();
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
        return this;
    }

    private void setButtonState(boolean startEnable) {
        startButton.setEnabled(startEnable);
        cancelButton.setEnabled(!startEnable);
        stopButton.setEnabled(!startEnable);
    }

    public class DownloadProgressBar implements IAppUpdateDownloadProgress
    {
        public int max = 0;

        @Override
        public void onPre()
        {

            setButtonState(false);
            progressBar.setProgress(0);
            progressText.setText("");
        }

        @Override
        public void onProgress(int progress, long current, long total) {

            max = (int)total;
            progressText.setText(String.format("%d/%d  (%d%%)", current, total, progress));
            progressBar.setProgress((int)current);
            progressBar.setMax(max);
        }

        @Override
        public void onComplete()
        {
            progressBar.setProgress(max);
            setButtonState(true);
        }

        @Override
        public void onFaile(String error) {

            resultText.setText(String.format("失败: %s", error));
        }
    }


    public void onClickUpdate()
    {
        UpdateAppUtils.from(context)
                .checkBy(UpdateAppUtils.CHECK_BY_VERSION_NAME) //更新检测方式，默认为VersionCode
                .serverVersionCode(5)
                .serverVersionName("5.0")
                .apkPath(DOWNLOAD_URL)
                .showNotification(true) //是否显示下载进度到通知栏，默认为true
                .updateInfo("更新日志说点什么呢")  //更新日志信息 String
                .downloadBy(UpdateAppUtils.DOWNLOAD_BY_BROWSER) //下载方式：app下载、手机浏览器下载。默认app下载
                .isForce(true) //是否强制更新，默认false 强制更新情况下用户不同意更新则不能使用app
                .update();
    }

    DownloadManager downloadManager;
    long downloadId;
    AppDownaloadTask task;


    public void StartDownloadManager(String urlPath, String savePath)
    {
        downloadManager = (DownloadManager) context.getSystemService(Activity.DOWNLOAD_SERVICE);

        //使用DownLoadManager来下载
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlPath));
        //将文件下载到自己的Download文件夹下,必须是External的
        //这是DownloadManager的限制
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), savePath);
        if (file.exists()){
            file.delete();
        }
        request.setDestinationUri(Uri.fromFile(file));
        request.setTitle(context.getString(R.string.app_name) );request.allowScanningByMediaScanner();
        // 如果要通过MediaScanner扫描要下载的文件，则应在调用之前enqueue(Request)调用此方法。
        request.allowScanningByMediaScanner();
        // 设置是否应在系统的下载UI中显示此下载。默认为True。
        request.setVisibleInDownloadsUi(true);
        // 控制下载管理器在下载运行或完成下载时是否发布系统通知。
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        // 设置文件类型
        request.setMimeType("application/vnd.android.package-archive");
        //添加请求 开始下载
        downloadId= downloadManager.enqueue(request);
        SharedPreferencesUtil.put(context, SharedPreferencesKey.AppUpdateDownloadId, downloadId);

        long myDwonloadID = (long) SharedPreferencesUtil.get(context,SharedPreferencesKey.AppUpdateDownloadId, -1l);


        task = new AppDownaloadTask();
        task.executeOnExecutor(AsyncTask.SERIAL_EXECUTOR, 0);
    }


    public class AppDownaloadTask extends AsyncTask<Integer, Integer, Integer>
    {
        public int max = 100;
        public int value = 0;
        public int progress = 0;

        @Override
        protected void onPreExecute()
        {
            setButtonState(false);
            progressBar.setProgress(0);
            progressText.setText("");
            progress = 0;
            value = 0;
        }

        @Override
        protected Integer doInBackground(Integer... integers)
        {
            //查询进度
            DownloadManager.Query query = new DownloadManager.Query()
                    .setFilterById(downloadId);
            Cursor cursor = null;
            try {
                while (progress < 100)
                {
                    cursor = downloadManager.query(query);//获得游标
                    if (cursor != null && cursor.moveToFirst()) {

                        //当前的下载量
                        value = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR));
                        //文件总大小
                        max = cursor.getInt(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES));
                        progress = (int) (value * 1.0f / max * 100);

                        publishProgress(progress);

                        try {
                            if (progress < 100) Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }

            return progress;
        }

        @Override
        protected void onProgressUpdate(Integer... values)
        {
            progressText.setText(String.format("%d/%d  (%d%%)", value, max, progress));
            progressBar.setProgress(value);
            progressBar.setMax(max);

        }


        @Override
        protected void onPostExecute(Integer integer)
        {
            resultText.setText(String.format("执行结果: %d", integer));
            progressBar.setProgress(max);
            setButtonState(true);
        }

        @Override
        protected void onCancelled(Integer integer)
        {
            resultText.setText(String.format("取消执行，当前结果: %d", integer));
            setButtonState(true);
        }

        @Override
        protected void onCancelled()
        {
            resultText.setText(String.format("取消执行"));
            setButtonState(true);
        }

        @Override
        protected void finalize() throws Throwable
        {
            super.finalize();
        }

    }






    private Handler downloadHandler = new Handler() {

        @Override
        public void handleMessage(Message msg)
        {
            String result = (String) msg.getData().get("result");

            Toast.makeText(context, result, Toast.LENGTH_LONG).show();
        }
    };

    private class DownloadRunable implements Runnable
    {

        @Override
        public void run() {

            String result = "Success";
            try {
                downLoad(DOWNLOAD_URL, DOWNLOAD_PATH);
            } catch (Exception e) {
                e.printStackTrace();
                result = "Error:" + e.toString();
            }

            Bundle bundle  = new Bundle();
            bundle.putString("result", result);

            Message msg = new Message();
            msg.setData(bundle);
            downloadHandler.sendMessage(msg);
        }
    }

    public void downLoad(String urlPath, String savePath)throws Exception
    {
        URL url = new URL(urlPath);
        InputStream is = url.openStream();
        //截取最后的文件名
        String end = urlPath.substring(urlPath.lastIndexOf("."));
        //打开手机对应的输出流,输出到文件中
        OutputStream os = context.openFileOutput(savePath, Context.MODE_PRIVATE);
        byte[] buffer = new byte[1024];
        int len = 0;
        //从输入六中读取数据,读到缓冲区中
        while((len = is.read(buffer)) > 0)
        {
            os.write(buffer,0,len);
        }
        //关闭输入输出流
        is.close();
        os.close();
    }


}

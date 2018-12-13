package com.game.appupdate.handlers;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.DownloadManager;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import com.game.utils.SharedPreferencesKey;
import com.game.utils.SharedPreferencesUtil;
import com.ihaiu.androidthreaddowanzip.R;

import java.io.File;

public class DMDownloadHandler extends AbstractDownloadHandler
{

    DownloadManager downloadManager;
    long downloadId;
    DMDownloadHandler.AppDownaloadTask task;

    @Override
    public AbstractDownloadHandler download(String apkUrl, String savePath)
    {
        startServer();

        if(savePath == null)
            savePath = apkUrl.substring(apkUrl.lastIndexOf('/') + 1 );

        startDownloadManager(apkUrl, savePath);
        return this;
    }

    public void startDownloadManager(String urlPath, String savePath)
    {

        downloadManager = (DownloadManager) activity.getSystemService(Activity.DOWNLOAD_SERVICE);

        //使用DownLoadManager来下载
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlPath));
        //将文件下载到自己的Download文件夹下,必须是External的
        //这是DownloadManager的限制
        File file = new File(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), savePath);
        if (file.exists()){
            file.delete();
        }
        request.setDestinationUri(Uri.fromFile(file));
        request.setTitle(activity.getString(R.string.app_name) );request.allowScanningByMediaScanner();
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
        SharedPreferencesUtil.put(activity, SharedPreferencesKey.AppUpdateDownloadId, downloadId);
        SharedPreferencesUtil.put(activity, SharedPreferencesKey.AppUpdateAppFilePath, file.getAbsolutePath());

        this.apkUrl = urlPath;
        this.apkPath = file.getAbsolutePath();

        task = new DMDownloadHandler.AppDownaloadTask();
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
            progress = 0;
            value = 0;

            if(progressBar != null)
            {
                progressBar.onPre();
            }
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

            if(progressBar != null)
            {
                progressBar.onProgress(progress, value, max);
            }
        }


        @Override
        protected void onPostExecute(Integer integer)
        {
            if(progressBar != null)
            {
                progressBar.onComplete();
            }

            sendComplete();
        }

        @Override
        protected void onCancelled()
        {
            if(progressBar != null)
            {
                progressBar.onFaile("onCancelled");
            }
        }


    }

}

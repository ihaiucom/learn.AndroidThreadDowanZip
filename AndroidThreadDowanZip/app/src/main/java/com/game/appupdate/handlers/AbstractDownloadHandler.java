package com.game.appupdate.handlers;

import android.app.Activity;
import android.content.Intent;

import com.game.appupdate.AppUpdateReceiver;
import com.game.appupdate.AppUpdateService;
import com.game.appupdate.IAppUpdateDownloadProgress;
import com.updateapputils.util.UpdateAppService;

/** 应用更新 下载处理器 抽象类 */
public abstract class AbstractDownloadHandler
{
    protected Activity activity;
    protected IAppUpdateDownloadProgress progressBar;
    protected String apkUrl;
    protected String apkPath;

    public AbstractDownloadHandler setActivity(Activity activity)
    {
        this.activity = activity;
        return this;
    }

    public AbstractDownloadHandler setProgressBar(IAppUpdateDownloadProgress progressBar)
    {
        this.progressBar = progressBar;
        return this;
    }

    public abstract AbstractDownloadHandler download(String apkUrl,  String savePath);

    public void startServer()
    {
        activity.startService(new Intent(activity, AppUpdateService.class));
    }


    public void sendComplete()
    {
        Intent intent = new Intent(AppUpdateReceiver.ACTION_DOWNLOAD_COMPLETE);
        intent.putExtra("apkUrl", apkUrl);
        intent.putExtra("apkPath", apkPath);
        activity.sendBroadcast(intent);
    }
}

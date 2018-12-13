package com.game.appupdate.handlers;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Environment;

import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadTask;
import com.game.appupdate.AppUpdateType;
import com.game.permissions.RequestPermissionsCode;
import com.ihaiu.androidthreaddowanzip.R;

import java.io.File;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class AppDownloadHandler extends AbstractDownloadHandler
{

    public AbstractDownloadHandler setActivity(Activity activity)
    {
        super.setActivity(activity);

        Aria.init(activity);
        Aria.download(this).register();
        return this;
    }

    public String apkUrl;
    public String savePath;
    @Override
    public AbstractDownloadHandler download(String apkUrl,  String savePath)
    {
        this.apkUrl = apkUrl;
        this.savePath = savePath;

        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        boolean hasPermission = EasyPermissions.hasPermissions(activity, perms);

        if(!hasPermission)
        {
            // 没有申请过权限，现在去申请
            EasyPermissions.requestPermissions(activity, activity.getString(R.string.appupdate),
                    RequestPermissionsCode.APP_UPDATE_TYPE_APP, perms);
        }
        else
        {
            download();
        }



        return this;
    }

    @AfterPermissionGranted(RequestPermissionsCode.APP_UPDATE_TYPE_APP)
    public void download()
    {
        startServer();

        if(savePath == null)
            savePath = apkUrl.substring(apkUrl.lastIndexOf('/') + 1 );

        File file = new File(activity.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), savePath);
        savePath = file.getAbsolutePath();

        this.apkUrl = apkUrl;
        this.apkPath = savePath;
        Aria.download(this)
                .load(apkUrl) //读取下载地址
                .setDownloadPath(savePath) //设置文件保存的完整路径
                .start();  //启动下载
    }

    @Download.onTaskPre
    public void onTaskPre(DownloadTask task)
    {
        if(progressBar != null)
        {
            progressBar.onPre();
        }
    }


    @Download.onTaskRunning
    protected void onTaskRunning(DownloadTask task)
    {
        if(progressBar != null)
        {
            progressBar.onProgress(task.getPercent(), task.getCurrentProgress(), task.getFileSize());
        }
    }

    @Download.onTaskComplete
    public void onTaskComplete(DownloadTask task)
    {
        if(progressBar != null)
        {
            progressBar.onComplete();
        }

        sendComplete();
    }


    @Download.onTaskStop
    public void onTaskCancel(DownloadTask task)
    {

        if(progressBar != null)
        {
            progressBar.onFaile("onTaskCancel");
        }
    }

    @Download.onTaskFail
    public void onTaskFail(DownloadTask task)
    {
        Aria.download(this).load(task.getKey()).cancel();

        if(progressBar != null)
        {
            progressBar.onFaile("onTaskFail");
        }
    }
}

package com.game.appupdate;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;

import com.game.appupdate.handlers.AbstractDownloadHandler;
import com.game.appupdate.handlers.AppDownloadHandler;
import com.game.appupdate.handlers.BrowserDownloadHandler;
import com.game.appupdate.handlers.DMDownloadHandler;
import com.game.permissions.RequestPermissionsForInstallUnknown;
import com.game.utils.SharedPreferencesKey;
import com.game.utils.SharedPreferencesUtil;

import java.io.File;

import pub.devrel.easypermissions.EasyPermissions;

public class AppUpdateManager
{

    @SuppressLint("StaticFieldLeak") private static volatile AppUpdateManager INSTANCE = null;
    public static final Object LOCK = new Object();
    protected String TAG = this.getClass().getSimpleName();

    public static AppUpdateManager getInstance(Activity activity) {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                INSTANCE = new AppUpdateManager(activity);
            }
        }
        return INSTANCE;
    }

    static AppUpdateManager getInstance()
    {
        return INSTANCE;
    }


    private Activity activity;
    public AppUpdateSetting setting = new AppUpdateSetting();
    private AppDownloadHandler appDownloadHandler = new AppDownloadHandler();
    private DMDownloadHandler dmDownloadHandler = new DMDownloadHandler();
    private BrowserDownloadHandler browserDownloadHandler = new BrowserDownloadHandler();

    private AbstractDownloadHandler selectDownloadHandler;
    private AppUpdateManager(Activity activity)
    {
        this.activity = activity;
        appDownloadHandler.setActivity(activity);
        dmDownloadHandler.setActivity(activity);
        browserDownloadHandler.setActivity(activity);
    }

    /** 设置进度条监听器 */
    public AppUpdateManager setProgressBar(IAppUpdateDownloadProgress progressBar)
    {
        appDownloadHandler.setProgressBar(progressBar);
        dmDownloadHandler.setProgressBar(progressBar);
        browserDownloadHandler.setProgressBar(progressBar);
        return this;
    }

    /** 设置下载方式 */
    public AppUpdateManager setDownloadType(int downloadType)
    {
        switch (downloadType)
        {
            case AppUpdateType.Auto:
                boolean isInstallAllowed =  RequestPermissionsForInstallUnknown.getInstallAllowed(activity);
                boolean hasSDCard = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
                String[] perms = {Manifest.permission.REQUEST_INSTALL_PACKAGES, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                boolean hasPermission = EasyPermissions.hasPermissions(activity, perms);

                if(isInstallAllowed && hasSDCard && hasPermission)
                {
                    downloadType = AppUpdateType.App;
                }
                else
                {
                    downloadType = AppUpdateType.BROWSER;
                }
                setDownloadType(downloadType);
                break;
            case  AppUpdateType.App:
                selectDownloadHandler = appDownloadHandler;
                break;
            case  AppUpdateType.DowanloadManager:
                selectDownloadHandler = dmDownloadHandler;
                break;
            case  AppUpdateType.BROWSER:
            default:
                selectDownloadHandler = browserDownloadHandler;
                break;
        }
        setting.setDownloadType(downloadType);
        return this;
    }

    /** 设置APK Url */
    public AppUpdateManager setApkUrl(String apkUrl)
    {
        setting.setApkUrl(apkUrl);
        return this;
    }

    /** 设置APK Url */
    public AppUpdateManager setApkPath(String apkPath)
    {
        setting.setApkPath(apkPath);
        return this;
    }

    /** 设置是强制退出 */
    public AppUpdateManager setForce(boolean force)
    {
        setting.setForce(force);
        return this;
    }




    /** 下载 */
    public AppUpdateManager download()
    {
        if(selectDownloadHandler == null)
        {
            selectDownloadHandler = browserDownloadHandler;
        }
        selectDownloadHandler.download(setting.getApkUrl(), setting.getApkPath());
        return this;
    }


    /**
     * 跳转安装
     */
    public  AppUpdateManager installApk(String apkPath)
    {
        installApk(activity, apkPath);

        return this;
    }




    /**
     * 跳转安装
     */
    public static  void installApk(Context activity,  String apkPath)
    {
        long time = (long) SharedPreferencesUtil.get(activity, SharedPreferencesKey.AppUpdateCallInstallTime, -1l);
        long subTime = System.currentTimeMillis() - time;
        if( subTime < 1000)
        {
            return;
        }
        SharedPreferencesUtil.put(activity, SharedPreferencesKey.AppUpdateCallInstallTime,  System.currentTimeMillis());


        Intent i = new Intent(Intent.ACTION_VIEW);
        File apkFile = new File(apkPath);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {

            i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(
                    activity, activity.getPackageName() + ".fileprovider", apkFile);
            i.setDataAndType(contentUri, "application/vnd.android.package-archive");

        } else {
            i.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        }
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(i);

    }


    /**
     * 通过浏览器下载APK包
     */
    public static void downloadForWebView(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }









}

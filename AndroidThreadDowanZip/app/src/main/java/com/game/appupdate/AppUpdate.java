package com.game.appupdate;

import android.app.Activity;


/**
 * 应用更新门面类
 * */
public class AppUpdate
{
    /** 初始化 */
    public static AppUpdateManager init(Activity activity)
    {
        return AppUpdateManager.getInstance(activity);
    }

    /** 安装APK */
    public static AppUpdateManager installApk(String apkPath)
    {
        return AppUpdateManager.getInstance().installApk(apkPath);
    }


}

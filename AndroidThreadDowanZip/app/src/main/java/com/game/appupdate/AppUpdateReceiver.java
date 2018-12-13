package com.game.appupdate;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppUpdateReceiver extends BroadcastReceiver
{
    public final static String ACTION_DOWNLOAD_COMPLETE = "com.game.appupdate.complete";

    @Override
    public void onReceive(Context context, Intent intent)
    {
        String apkPath = intent.getStringExtra("apkPath");
        AppUpdateManager.installApk(context, apkPath);
    }

}

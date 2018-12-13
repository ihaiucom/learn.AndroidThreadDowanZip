package com.game.permissions;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

/** 请求安装未知来源权限 */
public class RequestPermissionsForInstallUnknown
{
    public static final int GET_UNKNOWN_APP_SOURCES = 7;
    public static IAppInstallHandler appInstallHandler;

    /** 检测是否有安装未知来源权限 */
    public static void check(Activity activity, IAppInstallHandler installHandler)
    {
        appInstallHandler = installHandler;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            if(getInstallAllowed(activity))
            {
                installApk();
            }
            else
            {
                requestPermissions(activity);
            }
        }
        else
        {
            installApk();
        }
    }

    /** 是否有读取权限 */
    public static boolean getInstallAllowed(Activity context)
    {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            return context.getPackageManager().canRequestPackageInstalls();
        }
        return true;
    }

    /** 申请权限 */
    public static void requestPermissions(Activity activity)
    {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, RequestPermissionsCode.INSTALL_APK_REQUESTCODE);
    }


    /**
     * 权限申请回调
     * @param requestCode requestCode
     * @param permissions permissions
     * @param grantResults grantResults
     */
    public static void onRequestPermissionsResult(Activity activity,  int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionsCode.INSTALL_APK_REQUESTCODE:
                //有注册权限且用户允许安装
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e("appInstall---", "有注册权限且用户允许安装");
                    installApk();
                } else {
                    //将用户引导至安装未知应用界面。
                    Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                    activity.startActivityForResult(intent, GET_UNKNOWN_APP_SOURCES);
                    Log.e("appInstall---", "将用户引导至安装未知应用界面");
                }
                break;
            default:break;
        }
    }


    /**
     * 将用户引导至安装未知应用界面，允许安装未知应用后，回到当前activity继续安装应用
     * @param requestCode requestCode
     * @param resultCode resultCode
     * @param data data
     */
    public static void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        Log.e("appInstall---", "将用户引导至安装未知应用界面，允许安装未知应用后，回到当前activity继续安装应用");
        if(appInstallHandler != null)
            installApk();
    }

    protected static void installApk()
    {
        if(appInstallHandler != null)
        {
            appInstallHandler.installApk();
        }

        appInstallHandler = null;
    }
}

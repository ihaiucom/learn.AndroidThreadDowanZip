package com.game.appupdate;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.util.Log;

public class AppUpdateHelper
{

    public static final int INSTALL_APK_REQUESTCODE = 3;
    public static final int GET_UNKNOWN_APP_SOURCES = 7;

    /** 检测是否有安装未知来源应用权限 */
    public void check(Context context)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            boolean hasInstallPermission = isHasInstallPermissionWithO(context);
            if (!hasInstallPermission) {
                startInstallPermissionSettingActivity(context);
                return;
            }
        }
    }

    /** 如果为8.0以上系统，则判断是否有 未知应用安装权限 */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean isHasInstallPermissionWithO(Context context)
    {
        if (context == null){
            return false;
        }
        return context.getPackageManager().canRequestPackageInstalls();
    }


    /**
     * 开启设置安装未知来源应用权限界面
     * @param context
     */
    @RequiresApi (api = Build.VERSION_CODES.O)
    private void startInstallPermissionSettingActivity(Context context) {
        if (context == null){
            return;
        }
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
        ((Activity)context).startActivityForResult(intent,GET_UNKNOWN_APP_SOURCES);
    }


    /**
     * 将用户引导至安装未知应用界面，允许安装未知应用后，回到当前activity继续安装应用
     * @param requestCode requestCode
     * @param resultCode resultCode
     * @param data data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("appInstall---", "将用户引导至安装未知应用界面，允许安装未知应用后，回到当前activity继续安装应用");
    }
}

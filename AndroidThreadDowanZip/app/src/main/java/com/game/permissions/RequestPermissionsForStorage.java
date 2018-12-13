package com.game.permissions;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;

/** 请求SDCard读写权限 */
public class RequestPermissionsForStorage
{
    // 成功后的处理器
    private static IPermissionHandler permissionHandler;

    /** 检测是否有安装未知来源权限 */
    public static void check(Activity activity, IPermissionHandler _permissionHandler)
    {
        permissionHandler = _permissionHandler;

    }

    /** 申请权限 */
    public static void requestPermissions(Activity activity)
    {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES}, RequestPermissionsCode.INSTALL_APK_REQUESTCODE);
    }


    /** 获取权限后运行 */
    private void onSuccess()
    {
        if(permissionHandler != null)
        {
            permissionHandler.onPermissionSuccess();
        }
    }
}

package com.ihaiu.androidthreaddowanzip;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.game.permissions.RequestPermissionsCode;
import com.game.permissions.RequestPermissionsForInstallUnknown;
import com.ihaiu.androidthreaddowanzip.appupdate.LearnAppUpdate;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 学习线程
//        new LearnThreadMain(this).run();
//        new LearnTask().setContext(this).init();
//        // 拷贝
//        LearnAria aria = new LearnAria().setContext(this).init();
//        LearnZip zip = new LearnZip().setContext(this).setZipFilePath(aria.DOWNLOAD_PATH).init();
//        new LearnAssetCopy().setContext(this).init();

        new LearnAppUpdate().setContext(this).init();
    }



    /**
     * 权限申请回调
     * @param requestCode requestCode
     * @param permissions permissions
     * @param grantResults grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 把执行结果的操作给EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

        switch (requestCode) {
            case RequestPermissionsCode.INSTALL_APK_REQUESTCODE:
                RequestPermissionsForInstallUnknown.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
                break;
            default:break;
        }

    }


    //申请成功时调用
    public void onPermissionsGranted(int requestCode, List<String> list) {
        //请求成功执行相应的操作

        StringBuffer sb = new StringBuffer();
        for (String str : list){
            sb.append(str);
            sb.append("\n");
        }

        Toast.makeText(this, "已获取权限" + sb, Toast.LENGTH_SHORT).show();
    }

    @Override //申请失败时调用
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // 请求失败，执行相应操作
        //处理权限名字字符串
        StringBuffer sb = new StringBuffer();
        for (String str : list){
            sb.append(str);
            sb.append("\n");
        }
        sb.replace(sb.length() - 2,sb.length(),"");


        if (EasyPermissions.somePermissionPermanentlyDenied(this, list))
        {
            Toast.makeText(this, "已拒绝权限" + sb + "并不再询问" , Toast.LENGTH_SHORT).show();
            new AppSettingsDialog
                    .Builder(this)
                    .setRationale("此功能需要" + sb + "权限，否则无法正常使用，是否打开设置")
                    .setPositiveButton("是")
                    .setNegativeButton("否")
                    .build()
                    .show();
        }
    }

    /**
     * 将用户引导至安装未知应用界面，允许安装未知应用后，回到当前activity继续安装应用
     * @param requestCode requestCode
     * @param resultCode resultCode
     * @param data data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        RequestPermissionsForInstallUnknown.onActivityResult(requestCode, resultCode, data);
    }

}

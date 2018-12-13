package com.game.appupdate.handlers;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.game.appupdate.AppUpdateManager;
import com.game.utils.SharedPreferencesKey;
import com.game.utils.SharedPreferencesUtil;

import java.io.File;


/**
 * 应用下载完成 广播接收
 * */
public class DMDownloadReceiver extends BroadcastReceiver
{
    @SuppressLint("NewApi")
    @Override
    public void onReceive(Context context, Intent intent)
    {
        long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        long refernece = (long) SharedPreferencesUtil.get(context, SharedPreferencesKey.AppUpdateDownloadId, -1l);
        if (refernece != myDwonloadID) {
            return;
        }


        File file = queryDownloadedApk(context);
        if (file.exists()) {
            AppUpdateManager.installApk(context, file.getAbsolutePath());
        }
    }


    /**
     * 通过downLoadId查询下载的apk，解决6.0以后安装的问题
     * @param context
     * @return
     */
    public File queryDownloadedApk(Context context) {
        File targetApkFile = null;
        DownloadManager downloader = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        long downloadId = (long) SharedPreferencesUtil.get(context, SharedPreferencesKey.AppUpdateDownloadId, -1l);
        if (downloadId != -1) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            query.setFilterByStatus(DownloadManager.STATUS_SUCCESSFUL);
            Cursor cur = downloader.query(query);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    String uriString = cur.getString(cur.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI));
                    if (!TextUtils.isEmpty(uriString)) {
                        targetApkFile = new File(Uri.parse(uriString).getPath());
                    }
                }
                cur.close();
            }
        }
        return targetApkFile;

    }

}

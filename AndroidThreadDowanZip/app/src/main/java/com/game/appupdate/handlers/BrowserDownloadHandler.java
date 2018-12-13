package com.game.appupdate.handlers;

import com.game.appupdate.AppUpdateManager;

public class BrowserDownloadHandler extends AbstractDownloadHandler
{

    @Override
    public AbstractDownloadHandler download(String apkUrl, String savePath)
    {
        AppUpdateManager.downloadForWebView(activity, apkUrl);
        return this;
    }
}

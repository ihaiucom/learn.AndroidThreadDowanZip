package com.game.appupdate;

/** 应用更新 进度条设置 */
public interface IAppUpdateDownloadProgress
{
    void onPre();
    void onProgress(int progress, long current, long total );
    void onComplete();
    void onFaile(String error);

}

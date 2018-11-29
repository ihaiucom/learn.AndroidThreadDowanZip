package com.game.archiver;

/**
 * 解压Zip 事件监听
 * */
public interface IArchiverListener
{
    /**
     * Zip解压--预开始，可以初始化UI
     * */
    void onArchiverPre();


    /**
     * Zip解压--开始
     * */
    void onArchiverStart();

    /**
     * Zip解压--进度
     * */
    void onArchiverProgress(int current, int total);


    /**
     * Zip解压--完成
     * */
    void onArchiverComplete();


    /**
     * Zip解压--出错(失败)
     * */
    void onArchiverFail(String error);
}

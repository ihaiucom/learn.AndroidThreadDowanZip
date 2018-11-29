package com.game.archiver;

import android.annotation.SuppressLint;
import android.content.Context;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ArchiverManager
{

    @SuppressLint("StaticFieldLeak") private static volatile ArchiverManager INSTANCE = null;
    public static final Object LOCK = new Object();
    protected String TAG = this.getClass().getSimpleName();
    private Executor mThreadPool;

    private ArchiverManager()
    {
        mThreadPool= Executors.newSingleThreadExecutor();
    }

    public static ArchiverManager getInstance() {
        if (INSTANCE == null) {
            synchronized (LOCK) {
                INSTANCE = new ArchiverManager();
            }
        }
        return INSTANCE;
    }


    /**
     * 解压文件
     * @param srcfile  zip文件
     * @param unrarPath 解压目录
     * @param password 密码
     * @param password 监听消息处理器
     */
    public ArchiverManager unArchiver(final String srcfile,final String unrarPath,final String password,final IArchiverListener listener)
    {
        final BaseArchiver archiver = new ZipArchiver();
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                archiver.unArchiver(srcfile,unrarPath,password,listener);
            }
        });
        return this;
    }


    /**
     * assets 资源拷贝到其他目录
     * @param context  Context
     * @param assetPath assets目录路径
     * @param destPath 目标文件夹路径
     * @param estimateTotal 预估总文件多大(字节)
     */
    public ArchiverManager assetsCopyTo(final Context context,final String assetPath,final String destPath,final int estimateTotal, final IArchiverListener listener)
    {
        final AssetsCopyArchiver archiver = new AssetsCopyArchiver();
        mThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                archiver.assetsCopyTo(context,assetPath,destPath,estimateTotal, listener);
            }
        });
        return this;
    }


}

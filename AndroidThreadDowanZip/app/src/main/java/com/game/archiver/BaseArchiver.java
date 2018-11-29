package com.game.archiver;

import android.os.Handler;
import android.os.Message;

public abstract class BaseArchiver
{

    protected String TAG=this.getClass().getSimpleName();


    protected Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };


    /**
     * 解压文件
     * @param srcfile  zip文件
     * @param unrarPath 解压目录
     */
    public abstract void unArchiver(String srcfile, String unrarPath,String password,IArchiverListener listener);


    /**
     * 抛事件--预开始（可以初始化UI）
     * */
    protected void onPre(final IArchiverListener listener)
    {
        if (listener != null)
        {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onArchiverPre();
                }
            });
        }
    }


    /**
     * 抛事件--开始
     * */
    protected void onStart(final IArchiverListener listener)
    {
        if (listener != null)
        {
            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    listener.onArchiverStart();
                }
            });
        }
    }


    /**
     * 抛事件--进度
     * */
    protected void onProgress(final int current,final int total, final IArchiverListener listener)
    {
        if (listener != null)
        {
            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    listener.onArchiverProgress(current, total);
                }
            });
        }
    }


    /**
     * 抛事件--完成
     * */
    protected void onComplete(final IArchiverListener listener)
    {
        if (listener != null)
        {
            mHandler.post(new Runnable()
            {
                @Override
                public void run()
                {
                    listener.onArchiverComplete();
                }
            });
        }
    }


    /**
     * 抛事件--失败
     * */
    protected void onError(final String error, final IArchiverListener listener)
    {
        if (listener != null)
        {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    listener.onArchiverFail(error);
                }
            });
        }
    }


}

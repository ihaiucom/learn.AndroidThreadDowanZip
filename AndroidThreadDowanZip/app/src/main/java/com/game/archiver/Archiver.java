package com.game.archiver;


import android.content.Context;

public class Archiver
{
    protected String TAG=this.getClass().getSimpleName();


    /**
     * 解压文件
     * @param srcfile  zip文件
     * @param unrarPath 解压目录
     * @param password 密码
     * @param listener 监听消息处理器
     */
    public static ArchiverManager unArchiver(String srcfile, String unrarPath, String password, IArchiverListener listener)
    {
        return ArchiverManager.getInstance().unArchiver(srcfile,unrarPath,password,listener);
    }


    /**
     * assets 资源拷贝到其他目录
     * @param context  Context
     * @param assetPath assets目录路径
     * @param destPath 目标文件夹路径
     * @param estimateTotal 预估总文件多大(字节)
     */
    public static ArchiverManager assetsCopyTo(Context context, String assetPath, String destPath,  int estimateTotal, IArchiverListener listener)
    {
        return ArchiverManager.getInstance().assetsCopyTo(context,assetPath,destPath, estimateTotal, listener);
    }
}

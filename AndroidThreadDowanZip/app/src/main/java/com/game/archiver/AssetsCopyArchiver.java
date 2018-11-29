package com.game.archiver;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class AssetsCopyArchiver extends BaseArchiver
{
    @Override
    public void unArchiver(String srcfile, String unrarPath, String password, IArchiverListener listener) {

    }

    private IArchiverListener listener;
    private  boolean hasError = false;
    private int current = 0;
    private int estimateTotal = 133537339;

    /**
     * assets 资源拷贝到其他目录
     * @param context  Context
     * @param assetsPath assets目录路径
     * @param savePath 目标文件夹路径
     * @param estimateTotal 预估总文件多大(字节)
     */
    public void assetsCopyTo(Context context, String assetsPath, String savePath, int estimateTotal,  IArchiverListener listener)
    {
        hasError = false;
        current = 0;
        this.estimateTotal = estimateTotal;
        this.listener = listener;
        // 抛事件--预开始
        onPre(listener);
        doAssetsCopyTo(context, assetsPath, savePath);

        if(!hasError)
        {
            // 抛事件--完成
            onComplete(listener);
        }
    }

    private void doAssetsCopyTo(Context context, String assetsPath, String savePath)
    {

        try
        {
            String fileNames[] = context.getAssets().list(assetsPath);// 获取assets目录下的所有文件及目录名
            // 如果是目录
            if (fileNames.length > 0)
            {
                File file = new File(savePath);
                file.mkdirs();// 如果文件夹不存在，则递归
                for (String fileName : fileNames) {
                    doAssetsCopyTo(context, assetsPath + File.separator + fileName,
                            savePath + File.separator+ fileName);
                }
            }
            // 如果是文件
            else
            {
                InputStream is = context.getAssets().open(assetsPath);
                FileOutputStream fos = new FileOutputStream(new File(savePath));

                byte[] buffer = new byte[1024];
                int byteCount = 0;
                while ((byteCount = is.read(buffer)) != -1) {// 循环从输入流读取
                    // buffer字节
                    fos.write(buffer, 0, byteCount);// 将读取的输入流写入到输出流
                    current += byteCount;
                }
                fos.flush();// 刷新缓冲区
                is.close();
                fos.close();

                // 抛事件--进度
                onProgress(current, estimateTotal, listener);
            }
        }
        catch (Exception e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            hasError = true;
            onError(e.toString(), listener);
        }

    }
}

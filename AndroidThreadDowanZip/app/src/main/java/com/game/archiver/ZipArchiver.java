package com.game.archiver;

import android.text.TextUtils;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;

import java.io.File;

public class ZipArchiver extends BaseArchiver
{

    /**
     * 解压文件
     * @param srcfile  zip文件
     * @param unrarPath 解压目录
     */
    @Override
    public void unArchiver(final String srcfile, final String unrarPath, String password, final IArchiverListener listener)
    {
        // 抛事件--预开始
        onPre(listener);

        // 检测是否是空路径
        if (TextUtils.isEmpty(srcfile) || TextUtils.isEmpty(unrarPath))
        {
            onError(String.format("【解压文件失败】无效文件路径 srcfile=%s,  unrarPath=%s", srcfile,  unrarPath), listener);
            return;
        }

        // 检测 zip文件是否存在
        File src = new File(srcfile);
        if (!src.exists())
        {
            onError(String.format("【解压文件失败】zip文件不存在 srcfile=%s", srcfile), listener);
            return;
        }

        ZipFile zFile = null;
        try
        {
            zFile = new ZipFile(srcfile);
            zFile.setFileNameCharset("GBK");
            if (!zFile.isValidZipFile())
                throw new ZipException("文件不合法!");

            // 检测输出目录
            File destDir = new File(unrarPath);
            if (destDir.isDirectory() && !destDir.exists()) {
                destDir.mkdir();
            }

            // 设置密码
            if (zFile.isEncrypted()) {
                zFile.setPassword(password.toCharArray());
            }

            // 抛事件--开始
            onStart(listener);



//            zFile.extractAll(unrarPath);
            FileHeader fh = null;
            final int total = zFile.getFileHeaders().size();
            for (int i = 0; i < zFile.getFileHeaders().size(); i++) {
                fh = (FileHeader) zFile.getFileHeaders().get(i);
//                String entrypath = "";
//                if (fh.isFileNameUTF8Encoded()) {//解決中文乱码
//                    entrypath = fh.getFileName().trim();
//                } else {
//                    entrypath = fh.getFileName().trim();
//                }
//                entrypath = entrypath.replaceAll("\\\\", "/");
//
//                File file = new File(unrarPath + entrypath);
//                Log.d(TAG, "unrar entry file :" + file.getPath());

                zFile.extractFile(fh,unrarPath);

                // 抛事件--进度
                onProgress(i + 1, total, listener);
            }




        }
        catch (Exception e)
        {
            e.printStackTrace();
            onError(e.toString(), listener);
            return;
        }

        // 抛事件--完成
        onComplete(listener);


    }


}

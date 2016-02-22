package com.myth.shishi.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

public class FileUtils {

    /** 根目录 */
    public static final String ROOT_DIR = Environment
            .getExternalStorageDirectory() + "/shishi";

    /** 背景目录 */
    public static final String BACKGROUND_DIR = ROOT_DIR + "/background";

    /** 分享目录 */
    public static final String SHARE_DIR = ROOT_DIR + "/share";

    /**
     * 保存文件
     * 
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static String saveFile(Bitmap bm) throws IOException {
        createDir(ROOT_DIR);
        createDir(BACKGROUND_DIR);
        String fileName = MD5.MD5_16(Bitmap2Bytes(bm));
        File myCaptureFile = new File(BACKGROUND_DIR + "/" + fileName + ".jpg");
        if (!myCaptureFile.exists()) {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        }
        return myCaptureFile.getAbsolutePath();

    }

    private static void createDir(String dir) {
        File root = new File(dir);
        if (!root.exists()) {
            root.mkdir();
        }
    }

    /**
     * 保存文件
     * 
     * @param bm
     * @param fileName
     * @throws IOException
     */
    public static String saveFile(Bitmap bm, String fileName)
            throws IOException {
        createDir(ROOT_DIR);
        createDir(SHARE_DIR);
        File myCaptureFile = new File(SHARE_DIR + "/" + fileName + ".jpg");
        {
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        }
        return myCaptureFile.getAbsolutePath();
    }

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    public static Bitmap Bytes2Bimap(byte[] b) {
        if (b.length != 0) {
            return BitmapFactory.decodeByteArray(b, 0, b.length);
        } else {
            return null;
        }
    }

    /**
     * 解压assets的zip压缩文件到指定目录
     * 
     * @param context上下文对象
     * @param assetName压缩文件名
     * @param outputDirectory输出目录
     * @param isReWrite是否覆盖
     * @throws IOException
     */
    public static void unZip(Context context, int assetName,
            String outputDirectory, boolean isReWrite) throws IOException {
        // 创建解压目标目录
        File file = new File(outputDirectory);
        // 如果目标目录不存在，则创建
        if (!file.exists()) {
            file.mkdirs();
        }
        // 打开压缩文件
        InputStream inputStream =  context.getResources().openRawResource(assetName);

        if (inputStream == null) {
            Log.e("inputStream", (inputStream == null) + "");
        }
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);

        
        // 读取一个进入点
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        
        if (zipEntry == null) {
            Log.e("zipEntry", (zipEntry == null) + "");
        }
        // 使用1Mbuffer
        byte[] buffer = new byte[1024 * 1024];
        // 解压时字节计数
        int count = 0;
        // 如果进入点为空说明已经遍历完所有压缩包中文件和目录
        while (zipEntry != null) {
            // 如果是一个目录
            if (zipEntry.isDirectory()) {
                file = new File(outputDirectory + File.separator
                        + zipEntry.getName());
                // 文件需要覆盖或者是文件不存在
                if (isReWrite || !file.exists()) {
                    file.mkdir();
                }
            } else {
                // 如果是文件
                file = new File(outputDirectory + File.separator
                        + zipEntry.getName());
                // 文件需要覆盖或者文件不存在，则解压文件
                if (isReWrite || !file.exists()) {
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(
                            file);
                    while ((count = zipInputStream.read(buffer)) > 0) {
                        fileOutputStream.write(buffer, 0, count);
                    }
                    fileOutputStream.close();
                }
            }
            // 定位到下一个文件入口
            zipEntry = zipInputStream.getNextEntry();
        }
        zipInputStream.close();
    }

   

}

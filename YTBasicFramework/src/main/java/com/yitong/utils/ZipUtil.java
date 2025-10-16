package com.yitong.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;

import com.yitong.mbank.util.security.SafeCloseUtils;

/**
 * ZIP压缩与解压工具
 * @author tongxu_li
 * Copyright (c) 2015 Shanghai P&C Information Technology Co., Ltd.
 */
public class ZipUtil {
	
	/**
	 * 解压指定路径下的文件
	 * @param zipFileName
	 * @param outputDirectory
	 * @param isReWrite
	 * @return
	 */
	public static boolean unzip(String zipFileName, String outputDirectory, boolean isReWrite) {
        boolean isSuccess = false;
        InputStream inputStream = null;
        
        try {
        	File zipFile = new File(zipFileName);
        	if (!zipFile.exists()) {
        		return isSuccess;
        	}
        	inputStream = new FileInputStream(zipFile);
			innerUnzip(inputStream, outputDirectory, isReWrite);			
			isSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SafeCloseUtils.close(inputStream);
		}
        return isSuccess;
	}
	
	/**
	 * 解压assets下ZIP文件到指定目录
	 * @param context
	 * @param zipFileName
	 * @param outputDirectory
	 * @param isReWrite
	 * @return
	 */
    public static boolean unzipFromAssets(Context context, String zipFileName, String outputDirectory, boolean isReWrite) {
        boolean isSuccess = false;
        InputStream inputStream = null;
        try {
        	// 打开压缩文件
        	inputStream = context.getAssets().open(zipFileName);
			innerUnzip(inputStream, outputDirectory, isReWrite);			
			isSuccess = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SafeCloseUtils.close(inputStream);
		}
        return isSuccess;
    }
    
    /**
     * 内部解压文件
     * @param inputStream
     * @param outputDirectory
     * @param isReWrite
     * @throws Exception
     */
    private static void innerUnzip(InputStream inputStream, String outputDirectory, boolean isReWrite) throws Exception {
    	if (inputStream == null) {
    		return;
    	}
    	// 创建解压目标目录
        File file = new File(outputDirectory);
        // 如果目标目录不存在，则创建
        if (!file.exists()) {
            file.mkdirs();
        }
        ZipInputStream zipInputStream = new ZipInputStream(inputStream);
        // 读取一个进入点
        ZipEntry zipEntry = zipInputStream.getNextEntry();
        // 使用1Mbuffer
        byte[] buffer = new byte[1024 * 1024];
        // 解压时字节计数
        int count = 0;
        // 如果进入点为空说明已经遍历完所有压缩包中文件和目录
        while (zipEntry != null) {
            // 如果是一个目录
            if (zipEntry.isDirectory()) {
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                // 文件需要覆盖或者是文件不存在
                if (isReWrite || !file.exists()) {
                    file.mkdir();
                }
            } else {
                // 如果是文件
                file = new File(outputDirectory + File.separator + zipEntry.getName());
                // 文件需要覆盖或者文件不存在，则解压文件
                if (isReWrite || !file.exists()) {
                    file.createNewFile();
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
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

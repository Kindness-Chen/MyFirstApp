package com.yitong.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.util.Log;

import com.yitong.logs.Logs;
import com.yitong.mbank.util.security.SafeCloseUtils;

public class FileUtil {
    public static final String TAG = "FileUtil";

    public static final String SDCARD = Environment
            .getExternalStorageDirectory() + File.separator;

    /**
     * 在SD卡上创建文件
     *
     * @throws IOException
     */
    public File creatSDFile(String fileName) throws IOException {
        File file = new File(SDCARD + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上创建目录
     *
     * @param dirName
     */
    public File creatSDDir(String dirName) {
        File dir = new File(SDCARD + dirName);
        dir.mkdir();
        return dir;
    }

    /**
     * 判断SD卡上的文件夹是否存在
     */
    public boolean isFileExist(String fileName) {
        File file = new File(SDCARD + fileName);
        return file.exists();
    }

    /**
     * 将一个InputStream里面的数据写入到SD卡中
     */
    public File write2SDFromInput(String path, String fileName,
                                  InputStream input) {
        File file = null;
        OutputStream output = null;
        try {
            creatSDDir(path);
            file = creatSDFile(path + fileName);
            output = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            while ((input.read(buffer)) != -1) {
                output.write(buffer);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            SafeCloseUtils.close(output);
        }
        return file;
    }

    /**
     * 文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExist2(String filePath) {
        File f = new File(filePath);
        return f.exists() && f.isFile();
    }

    /**
     * 目录是否存在
     *
     * @param dirPath
     * @return
     */
    public static boolean isDirExist(String dirPath) {
        File f = new File(dirPath);
        return f.exists() && f.isDirectory();
    }

    /**
     * 创建文件
     *
     * @param path     文件路径
     * @param filename 文件名
     * @return
     */
    public static boolean createFile(String path, String filename) {
        if (!createDir(path)) {
            return false;
        }
        String filePath = path + File.separator + filename;
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            try {
                // 创建新文件
                return file.createNewFile();
            } catch (Exception e) {
                Logs.e(TAG, e.getMessage(), e);
            }
        }
        return true;
    }

    /**
     * 创建目录
     *
     * @param dirPath
     * @return
     */
    public static boolean createDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs();
        }
        return true;
    }

    /**
     * 创建文件
     *
     * @param filePath 文件绝对路径
     * @return
     */
    public static boolean createFile(String filePath) {
        String pathName = getPathName(filePath);
        String fileName = getFileName(filePath);
        return createFile(pathName, fileName);
    }

    /**
     * @param path
     * @return
     * @Description 文件重命名
     * @Author zhaoqianpeng(zqp @ yitong.com.cn) 2014-4-15
     */
    public static boolean renameTo(String oldPath, String newPath) {
        boolean renameFlg = false;
        File ofile = new File(oldPath);
        if (ofile.exists()) {
            File nfile = new File(newPath);
            renameFlg = ofile.renameTo(nfile);
        }
        return renameFlg;
    }

    /**
     * @param path
     * @Description 删除指定路径文件
     * @Author zhaoqianpeng(zqp @ yitong.com.cn) 2014-4-15
     */
    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        // 判断文件是否存在
        if (file.exists() && file.isFile()) {
            //删除
            file.delete();
        }
    }

    /**
     * 删除指定目录下的所有文件
     *
     * @param dirPath
     */
    public static boolean deleteFiles(String dirPath) {
        File fi = new File(dirPath);
        if (fi.exists() && fi.isDirectory()) {
            File[] files = fi.listFiles();
            for (File f : files) {
                if (f.exists()) {
                    if (f.isFile()) {
                        if (!f.delete()) {
                            return false;
                        }
                    } else {
                        // 文件为目录的情况，需要进行递归删除
                        deleteFiles(f.getAbsolutePath());
                    }
                }
            }
        }
        return true;
    }

    public static void cache(String path, byte[] data) throws IOException {
        OutputStream os = null;
        try {
            os = new FileOutputStream(path);
            os.write(data);
        } catch (IOException e) {
            Log.w(TAG, "file cache(" + path + ") error!");
        } finally {
            if (null != os)
                os.close();
            os = null;
        }
    }

    public static boolean checkIsImgFile(String path) {
        boolean isImgFile = false;
        // 获取扩展名
        String fileEnd = path.substring(path.lastIndexOf(".") + 1,
                path.length()).toLowerCase();
        if (fileEnd.equals("png") || fileEnd.equals("jpg")) {
            isImgFile = true;
        } else {
            isImgFile = false;
        }
        return isImgFile;
    }

    public static boolean checkIsPdfFile(String path) {
        boolean isPdfFile = false;
        // 获取扩展名
        String fileEnd = path.substring(path.lastIndexOf(".") + 1,
                path.length()).toLowerCase();
        if (fileEnd.equals("pdf")) {
            isPdfFile = true;
        } else {
            isPdfFile = false;
        }
        return isPdfFile;
    }

    public static boolean checkIsVideoFile(String path) {
        boolean isVideoFile = false;
        // 获取扩展名
        String fileEnd = path.substring(path.lastIndexOf(".") + 1,
                path.length()).toLowerCase();
        if (fileEnd.equals("mp4") || fileEnd.equals("3gp")) {
            isVideoFile = true;
        } else {
            isVideoFile = false;
        }
        return isVideoFile;
    }

    public static String getFileName(String path) {
        String fileName = path.substring(path.lastIndexOf("/")).substring(1);
        return fileName;
    }

    public static String getPathName(String filePath) {
        return filePath.substring(0, filePath.lastIndexOf(File.separator));
    }

    public static Bitmap getDiskBitmap(String pathString) {
        Bitmap bitmap = null;
        try {
            File file = new File(pathString);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(pathString);
            }
        } catch (Exception e) {
        }
        return bitmap;
    }

    /**
     * 获取系统缓存路径<br>
     * 当SD卡存在或者SD卡不可被移除时，获取SD卡路径；否则获取内存路径
     *
     * @param context 上下文
     * @return 缓存路径
     */
    @SuppressLint("NewApi")
    public static String getDiskCachePath(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getAbsolutePath();
        } else {
            cachePath = context.getCacheDir().getAbsolutePath();
        }
        return cachePath;
    }

    /**
     * 获取系统文件路径<br>
     * 当SD卡存在或者SD卡不可被移除时，获取SD卡路径；否则获取内存路径
     *
     * @param context 上下文
     * @return 缓存路径
     */
    @SuppressLint("NewApi")
    public static String getDiskFilePath(Context context) {
        String filePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            filePath = context.getExternalFilesDir(null).getAbsolutePath();
        } else {
            filePath = context.getFilesDir().getAbsolutePath();
        }
        return filePath;
    }

    public static void copyFile(String oldPath, String newPath) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                // int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; // 字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    public static InputStream readFile(File file) {
        InputStream is = null;
        try {
            is = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            Log.d(TAG, "file not found: " + file.getAbsolutePath());
        }
        return is;
    }

    /**
     * 获取文件byte
     *
     * @param file
     * @return
     */
    public static byte[] getFileByte(File file) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;

    }

    /**
     * 保存图片
     */
    public static boolean saveImageToGallery(Context context, String bmPath, String bmName, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(bmPath);
        if (!appDir.exists()) {
            appDir.mkdirs();
        }
        File file = new File(appDir, bmName);
        if (file.exists() && file.isFile()) {
            file.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //通过io流的方式来压缩保存图片
            boolean isSuccess = bmp.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();

            //把文件插入到系统图库
            //MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), fileName, null);

            //保存图片后发送广播通知更新数据库
            Uri uri = Uri.fromFile(file);
            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
            return isSuccess;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * encodeBase64File:(将文件转成base64 字符串). <br/>
     *
     * @param path 文件路径
     * @return
     * @throws Exception
     */
    public static String encodeBase64File(String path) throws Exception {
        File file = new File(path);
        FileInputStream inputFile = new FileInputStream(file);
        byte[] buffer = new byte[(int) file.length()];
        inputFile.read(buffer);
        inputFile.close();
        return Base64.encodeToString(buffer, Base64.DEFAULT);
    }

    /**
     * decoderBase64File:(将base64字符解码保存文件). <br/>
     *
     * @param base64Code 编码后的字串
     * @param savePath   文件保存路径
     * @throws Exception
     */
    public static void decoderBase64File(String base64Code, String savePath) throws Exception {
        byte[] buffer = Base64.decode(base64Code, Base64.DEFAULT);
        FileOutputStream out = new FileOutputStream(savePath);
        out.write(buffer);
        out.close();
    }

    /**
     * file转为base64
     */
    public static String file2Base64(File file) {

        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (Exception e) {
            Logs.e("file 文件转byte64", e.getMessage());
        }
        byte[] encodedBytes = org.apache.commons.codec.binary.Base64.encodeBase64(bytes);
        byte[] decodedBytes = org.apache.commons.codec.binary.Base64.decodeBase64(encodedBytes);
        String result = Base64.encodeToString(decodedBytes, Base64.NO_WRAP);
        return result;
    }

    /**
     * 字节转文件
     *
     * @param file
     * @param bytes
     * @return
     */
    public static boolean bytes2File(File file, byte[] bytes) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes, 0, bytes.length);
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

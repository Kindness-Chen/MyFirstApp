package com.yitong.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import com.yitong.logs.Logs;
import com.yitong.mbank.util.security.SafeCloseUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;

/**
 * @author Mayouwei mayouwei@outlook.com
 * @Description 位图处理工具(包含将原图转换为圆角图 ， 与base64之间的相互转换 ， 位图质量压缩 ， 图片的截取 ， 图片的保存)
 * @date 2015年6月6日 上午9:55:28
 */
public class BitmapUtil {
    private static final String TAG = "BitmapUtil";
    /***************** 单例模式 *****************/
    /**
     * @Description BitmapUtil 本类静态对象，防止被创建
     */
    private static BitmapUtil instance;

    /**
     * @Description 空构造函数
     * @author Mayouwei mayouwei@outlook.com
     * @date 2015年6月6日 上午10:08:02
     */
    private BitmapUtil() {
    }

    /**
     * @return BitmapUtil 返回该工具类对象
     * @Description 对外暴露一个方法来获取本类对象
     * @author Mayouwei mayouwei@outlook.com
     * @date 2015年6月6日 上午10:08:11
     */
    public static BitmapUtil getInstance() {
        if (instance == null) {// 如果本类对象不存在
            instance = new BitmapUtil();// 直接创建一个新的本类对象
        }
        return instance;// 返回本类对象
    }

    /***************** 单例模式 *****************/

    /**
     * @param bitmap 需要被转换为圆角图片的原位图资源
     * @param radius 该圆角图片的半径。圆角直径和图片边长的比例。radius=1表示圆形
     * @return Bitmap 返回转换后的圆角图片。
     * @Description 将图片转换为圆角图片
     * @author Mayouwei mayouwei@outlook.com
     * @date 2015年6月6日 上午10:09:26
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float radius) {
        // 通过接收到位图来创建一个新的位图(接收的位图的尺寸，和属性)
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);// 通过新创建的位图来创建一个画布
        final int color = 0xff424242;// 定义一个颜色值
        final Paint paint = new Paint();// 定义一个画笔
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());// 创建一个矩形
        final RectF rectF = new RectF(rect);// 创建精度更高的矩形
        final float roundPx = bitmap.getWidth() / 2;// 图像的宽度除以二当做圆角图形的半径

        paint.setAntiAlias(true);// 设置抗锯齿
        canvas.drawARGB(0, 0, 0, 0);// 相当于清屏
        paint.setColor(color);// 设置画笔颜色
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);// 绘制圆角矩形(矩形对象,X轴的半径,Y轴的半径,画笔)
        // canvas原有的图片可以理解为背景，就是dst；
        // 新画上去的图片可以理解为前景，就是src。
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));// 设置两张图片相交时的模式
        canvas.drawBitmap(bitmap, rect, rect, paint);// 绘制图片(将要绘制出来的位图,前景图可为空,背景图,画笔)
        return output;// 返回最终的位图(画布上的图)
    }

    /**
     * @param bitmap 所需要转换的bitmap
     * @return String 返回一个转换后的base64的字符串
     * @Description 将bitmap转为base64
     * @author Mayouwei mayouwei@outlook.com
     * @date 2015年6月6日 下午2:34:40
     */
    public static String bitmapToBase64(Bitmap bitmap) {

        String result = null;// 定义一个结果字符串(用于存储最后的结果)
        ByteArrayOutputStream baos = null;// 定义一个字节数组输出流
        try {
            if (bitmap != null) {// 如果接收到的图片存在
                baos = new ByteArrayOutputStream();// 实例化字节数组输出流对象
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 将图片以JPEG格式，不进行任何压缩，将其读到流中

                baos.flush();// 刷新流

                byte[] bitmapBytes = baos.toByteArray();// 将存有数据的字节数组流转换为字节数组
                result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT).replace("\n", "");// 将字节数组以字符串的形式存储起来
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {// 如果流存在
                    baos.flush();// 刷新流
                    baos.close();// 关闭流
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;// 将转换后的base64字符串返回
    }

    /**
     * @param bitmap 所需要转换的bitmap
     * @return String 返回一个转换后的base64的字符串
     * @Description 将bitmap转为base64
     * @author Mayouwei mayouwei@outlook.com
     * @date 2015年6月6日 下午2:34:40
     */
    public static String bitmapToBase64New(Bitmap bitmap) {

        String result = null;// 定义一个结果字符串(用于存储最后的结果)
        ByteArrayOutputStream baos = null;// 定义一个字节数组输出流
        try {
            if (bitmap != null) {// 如果接收到的图片存在
                baos = new ByteArrayOutputStream();// 实例化字节数组输出流对象
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 将图片以JPEG格式，不进行任何压缩，将其读到流中

                baos.flush();// 刷新流

                byte[] bitmapBytes = baos.toByteArray();// 将存有数据的字节数组流转换为字节数组

                byte[] encodedBytes = org.apache.commons.codec.binary.Base64.encodeBase64(bitmapBytes);
                byte[] decodedBytes = org.apache.commons.codec.binary.Base64.decodeBase64(encodedBytes);
                result = Base64.encodeToString(decodedBytes, Base64.NO_WRAP);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {// 如果流存在
                    baos.flush();// 刷新流
                    baos.close();// 关闭流
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;// 将转换后的base64字符串返回
    }

    /**
     * @param base64Data 所需要被转换的base64字符串
     * @return Bitmap 返回转换好的bitmap
     * @Description 将base64形式的字符串转为bitmap
     * @author Mayouwei mayouwei@outlook.com
     * @date 2015年6月6日 下午2:36:08
     */
    public static Bitmap base64ToBitmap(String base64Data) {
        byte[] bytes = Base64.decode(base64Data, Base64.DEFAULT);// 将接收的字符串以base64的形式解码为字节数组
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);// 将字节数组转换为位图，然后返回该位图
    }

    /**
     * @param activity    调用者的上下文
     * @param uri         图片的地址
     * @param outputX     图片裁剪后的宽
     * @param outputY     图片裁剪后的高
     * @param requestCode 请求码
     * @Description 图片的裁剪
     * @author Mayouwei mayouwei@outlook.com
     * @date 2015年6月6日 下午3:29:52
     */
    public static void cropImage(Activity activity, Uri uri, int outputX, int outputY, int requestCode) {
        try {
            Intent intent = new Intent("com.android.camera.action.CROP");
            // 实现对图片的裁剪,必须要设置图片的属性和大小
            // 设置属性，表示获取任意类型的图片
            intent.setDataAndType(uri, "image/*");
            // 设置可以滑动选选择区域的属性,注意这里是字符串"true"
            intent.putExtra("crop", "true");
            // 设置剪切框1:1比例的效果
            // 这个是裁剪时候的 裁剪框的 X方向的比例
            intent.putExtra("aspectX", 1);// 宽高比
            // 同上Y方向的比例. (注意： aspectX, aspectY ，两个值都需要为 整数，如果有一个为浮点数，就会导致比例失效)
            intent.putExtra("aspectY", 1);
            // 返回数据的时候的 X像素大小
            intent.putExtra("outputX", outputX);// 宽高
            // 返回的时候 Y的像素大小
            intent.putExtra("outputY", outputY);
            // 图片输出格式
            intent.putExtra("outputFormat", "JPEG");
            intent.putExtra("scale", true);// 黑边
            intent.putExtra("scaleUpIfNeeded", true);// 黑边
            // 是否去除面部检测， 如果你需要特定的比例去裁剪图片，那么这个一定要去掉，因为它会破坏掉特定的比例
            intent.putExtra("noFaceDetection", true);
            // 是否要返回值
            intent.putExtra("return-data", true);
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param path  存储的目的路径
     * @param image 需要存储的图片
     * @return boolean 表示是否存储成功
     * @Description 将图片存储到文件
     * @author Mayouwei mayouwei@outlook.com
     * @date 2015年6月6日 下午3:31:27
     */
    public static boolean saveBitmapToFile(String path, Bitmap image) {
        try {
            File file = new File(path);// 通过接收的路径创建一个文件
            if (!file.exists() || file.isDirectory()) {// 如果该文件不存在，并且是一个目录
                file.createNewFile();// 创建一个新文件
            }
            FileOutputStream fos = new FileOutputStream(file);// 创建文件输出流
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 将图片文件转换为流
            fos.flush();// 刷新流
            fos.close();// 关闭流
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 保存本地图片
     *
     * @param bmp  图片
     * @param path 保存到的路径
     */
    public static void saveImageForLocal(Bitmap bmp, String path) {
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ByteArrayOutputStream baos = null; // 字节数组输出流
        try {
            baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArray = baos.toByteArray();// 字节数组输出流转换成字节数组
            File file = new File(path);
            file.getParentFile().mkdirs();
            // 将字节数组写入到刚创建的图片文件中
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(byteArray);

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * 保存本地图片
     *
     * @param bmp  图片
     * @param path 保存到的路径
     */
    public static boolean saveBitmapForLocal(Bitmap bmp, String path) {
        boolean succeed = false;
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ByteArrayOutputStream baos = null; // 字节数组输出流
        try {
            baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArray = baos.toByteArray();// 字节数组输出流转换成字节数组
            File file = new File(path);
            file.getParentFile().mkdirs();
            // 将字节数组写入到刚创建的图片文件中
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(byteArray);
            succeed = true;
        } catch (Exception e) {
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return succeed;
        }
    }

    /**
     * @param srcPath 需要被压缩的图片的地址
     * @return Bitmap 返回被压缩后的图片
     * @Description 比例压缩方式
     * @author Mayouwei mayouwei@outlook.com
     * @date 2015年6月6日 下午4:39:14
     */
    public static Bitmap compressImage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();// 创建位图属性
        newOpts.inJustDecodeBounds = true;// 读内容，如果为false，那么就是只读边(为了获取尺寸)不读内容
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);// 从接收的文件全路径和设定的属性来解析出位图
        // options.inSampleSize是以2的指数的倒数被进行放缩。这样，我们可以依靠inSampleSize的值的设定将图片放缩载入，
        // 这样一般情况也就不会出现上述的OOM问题了。现在问题是怎么确定inSampleSize的值？每张图片的放缩大小的比例应该是不一样的！
        // 这样的话就要运行时动态确定。在BitmapFactory.Options中提供了另一个成员inJustDecodeBounds。
        // 设置inJustDecodeBounds为true后，decodeFile并不分配空间，但可计算出原始图片的长度和宽度，即opts.width和opts.height。
        // 有了这两个参数，再通过一定的算法，即可得到一个恰当的inSampleSize。
        newOpts.inSampleSize = computeSampleSize(newOpts, -1, 760 * 480);
        // 这里一定要将其设置回false，因为之前我们将其设置成了true
        newOpts.inJustDecodeBounds = false;

        // int w = newOpts.outWidth;// 获取属性的宽
        // int h = newOpts.outHeight;// 获取属性的高
        // // float hh = 720f;// 自定义高
        // float hh = 480f;// 自定义高
        // float ww = 320f;// 自定义宽
        // int be = 1;// 定义默认采样率
        // if (w > h && w > ww) {
        // be = (int) (newOpts.outWidth / ww);
        // } else if (w < h && h > hh) {
        // be = (int) (newOpts.outHeight / hh);
        // }
        // if (be <= 0)
        // be = 1;
        // newOpts.inSampleSize = be;// 设置采样率

        newOpts.inPreferredConfig = Config.ARGB_8888;// 该模式是默认的,可不设
        newOpts.inPurgeable = true;// 同时设置才会有效
        newOpts.inInputShareable = true;// 。当系统内存不够时候图片自动被回收

        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return bitmap;
    }

    /**
     * @param srcPath 需要被压缩的图片的地址
     * @return Bitmap 返回被压缩后的图片
     * @description BitmapUtil compressPicture 质量压缩图片
     * @author 马有为 email: mayouwei@outlook.com
     * @date 2015年10月12日 下午4:08:42
     */
    public static Bitmap compressPicture(String srcPath) {
        Bitmap image = BitmapFactory.decodeFile(srcPath);
        return compressBitmap(image, 100);
    }

    public static Bitmap compressBitmap(Bitmap bmp, int kb) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > kb) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            Logs.d("compressBitmap", baos.toByteArray().length + "");
            baos.reset();// 重置baos即清空baos
            options -= 2;// 每次都减少10
            bmp.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
        }
        Logs.d("compressBitmap", "final->" + baos.toByteArray().length + "");
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap result = BitmapFactory.decodeStream(isBm, null, null);
        SafeCloseUtils.close(isBm);
        return result;
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128
                : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * @param context 调用者的上下文
     * @param uri     图片的URI
     * @return String 类型的图片路经
     * @description BitmapUtil getPath 根据URI获取图片路经
     * @author 马有为 email: mayouwei@outlook.com
     * @date 2015年7月12日 下午8:11:34
     */
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;// 版本信息是否高于Android4.4
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) { // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {// DownloadsProvider
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(uri)) {// MediaProvider
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {// MediaStore
            // (and
            // general)
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {// File
            return uri.getPath();
        }
        return null;
    }

    /**
     * * Get the value of the data column for this Uri. This is useful for *
     * MediaStore Uris, and other file-based ContentProviders. * @param context
     * The context. * @param uri The Uri to query. * @param selection (Optional)
     * Filter used in the query. * @param selectionArgs (Optional) Selection
     * arguments used in the query. * @return The value of the _data column,
     * which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static String selectImage(Context context, Intent data) {
        Uri selectedImage = data.getData();
        // Log.e(TAG, selectedImage.toString());
        if (selectedImage != null) {
            String uriStr = selectedImage.toString();
            String path = uriStr.substring(10, uriStr.length());
            if (path.startsWith("com.sec.android.gallery3d")) {
                Logs.e(TAG, "It's auto backup pic path:" + selectedImage.toString());
                return null;
            }
        }
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }

    /**
     * * @param uri The Uri to check. * @return Whether the Uri authority is
     * ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * * @param uri The Uri to check. * @return Whether the Uri authority is
     * DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * * @param uri The Uri to check. * @return Whether the Uri authority is
     * MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * * @param uri The Uri to check. * @return Whether the Uri authority is
     * Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @return 图片的旋转角度
     */

    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param ctx
     * @param uri 图片URI
     * @return 图片的旋转角度
     */

    public static int getBitmapDegree(Context ctx, Uri uri) {
        String path;
        Logs.e("getBitmapDegree", "uri=" + uri.toString());
        Logs.e("getBitmapDegree", "path=" + uri.getPath());
        if (uri.toString().startsWith("content://")) {
            path = getRealPathFromURI(ctx, uri);
        } else {
            path = uri.getPath();
        }
        if (path == null) {
            return 0;
        }
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 获取相册图片真实地址
     *
     * @param context
     * @param contentUri
     * @return
     */
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return null;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    /**
     * 保持长宽比缩小Bitmap
     *
     * @param bitmap
     * @param maxWidth
     * @param maxHeight
     * @return
     */
    public static Bitmap compressImageByScale(Bitmap bitmap, int maxWidth, int maxHeight) {
        int originWidth = bitmap.getWidth();
        int originHeight = bitmap.getHeight();
        // no need to resize
        if (originWidth < maxWidth && originHeight < maxHeight) {
            return bitmap;
        }
        int width = originWidth;
        int height = originHeight;
        // 若图片过宽, 则保持长宽比缩放图片
        if (originWidth > maxWidth) {
            width = maxWidth;
            double i = originWidth * 1.0 / maxWidth;
            height = (int) Math.floor(originHeight / i);
            bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        }
        if (maxHeight != -1) {//-1按照比例来
            // 若图片过长, 则从上端截取
            if (height > maxHeight) {
                height = maxHeight;
                bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
            }
        }
//        Log.i(TAG, width + " width");
//        Log.i(TAG, height + " height");
        return bitmap;
    }

    /**
     * 获取本地bitmap
     *
     * @param path
     * @return
     */
    public static Bitmap getBitmapFromPath(String path) {
        Bitmap bitmap = null;
        try {
            File file = new File(path);
            if (file.exists()) {
                bitmap = BitmapFactory.decodeFile(path);
            }
        } catch (Exception e) {
        }
        return bitmap;
    }

}

package com.example.myfirstapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Messenger;
import android.os.SystemClock;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.myfirstapp.R;
import com.example.myfirstapp.application.MyApplication;
import com.example.myfirstapp.downloaderLibrary.MyDownloaderService;
import com.example.myfirstapp.util.FormatTools;
import com.google.android.vending.expansion.downloader.DownloadProgressInfo;
import com.google.android.vending.expansion.downloader.DownloaderClientMarshaller;
import com.google.android.vending.expansion.downloader.DownloaderServiceMarshaller;
import com.google.android.vending.expansion.downloader.Helpers;
import com.google.android.vending.expansion.downloader.IDownloaderClient;
import com.google.android.vending.expansion.downloader.IDownloaderService;
import com.google.android.vending.expansion.downloader.IStub;
import com.google.android.vending.expansion.downloader.zipFile.APKExpansionSupport;
import com.google.android.vending.expansion.downloader.zipFile.ZipResourceFile;
import com.yitong.utils.AndroidUtil;
import com.google.android.vending.expansion.downloader.zipFile.ZipResourceFile.ZipEntryRO;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.zip.CRC32;

public class MyDownloaderActivity extends AppCompatActivity implements IDownloaderClient {

    private static final String LOG_TAG = "MyDownloaderActivity";

    private ProgressBar mPB;

    private TextView mStatusText;
    private TextView mProgressFraction;
    private TextView mProgressPercent;
    private TextView mAverageSpeed;
    private TextView mTimeRemaining;

    private View mDashboard;
    private View mCellMessage;

    private Button mPauseButton;
    private Button mWiFiSettingsButton;

    private ImageView imgBus;

    private boolean mStatePaused;
    private int mState;

    private IDownloaderService mRemoteService;

    private IStub mDownloaderClientStub;

    private void setState(int newState) {
        if (mState != newState) {
            mState = newState;
            mStatusText.setText(Helpers.getDownloaderStringResourceIDFromState(newState));
        }
    }

    private void setButtonPausedState(boolean paused) {
        mStatePaused = paused;
        int stringResourceID = paused ? R.string.text_button_resume :
                R.string.text_button_pause;
        mPauseButton.setText(stringResourceID);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_downloader);

        /*
         *在我们做任何事情之前，我们期望的文件是否已经在这里
         *交付（可能由Market提供）对于免费标题，这可能是
         *值得一做。（因此无需市场请求）
         */
        if (!expansionFilesDelivered()) {

            try {
                Intent launchIntent = this
                        .getIntent();
                Intent intentToLaunchThisActivityFromNotification = new Intent(this, this.getClass());
                intentToLaunchThisActivityFromNotification.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                        Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intentToLaunchThisActivityFromNotification.setAction(launchIntent.getAction());

                if (launchIntent.getCategories() != null) {
                    for (String category : launchIntent.getCategories()) {
                        intentToLaunchThisActivityFromNotification.addCategory(category);
                    }
                }

                //用于从通知中打开此活动的Build PendingIntent
                PendingIntent pendingIntent = PendingIntent.getActivity(this,
                        0, intentToLaunchThisActivityFromNotification,
                        PendingIntent.FLAG_UPDATE_CURRENT);

                //请求开始下载
                int startResult = DownloaderClientMarshaller.startDownloadServiceIfRequired(this,
                        pendingIntent, MyDownloaderService.class);

                if (startResult != DownloaderClientMarshaller.NO_DOWNLOAD_REQUIRED) {
                    //DownloaderService已开始下载文件，显示进度
                    initializeDownloadUI();
                    return;
                }//否则，不需要下载，我们就可以开始下一步了
            } catch (PackageManager.NameNotFoundException e) {
                Log.e(LOG_TAG, "Cannot find own package! MAYDAY!");
                e.printStackTrace();
            }

        }
        initializeDownloadUI();
        validateXAPKZipFiles();
    }

    /**
     * If the download isn't present, we initialize the download UI. This ties
     * all of the controls into the remote service calls.
     */
    private void initializeDownloadUI() {
        mDownloaderClientStub = DownloaderClientMarshaller.CreateStub
                (this, MyDownloaderService.class);

        mPB = (ProgressBar) findViewById(R.id.progressBar);
        mStatusText = (TextView) findViewById(R.id.statusText);
        mProgressFraction = (TextView) findViewById(R.id.progressAsFraction);
        mProgressPercent = (TextView) findViewById(R.id.progressAsPercentage);
        mAverageSpeed = (TextView) findViewById(R.id.progressAverageSpeed);
        mTimeRemaining = (TextView) findViewById(R.id.progressTimeRemaining);
        mDashboard = findViewById(R.id.downloaderDashboard);
        mCellMessage = findViewById(R.id.approveCellular);
        mPauseButton = (Button) findViewById(R.id.pauseButton);
        mWiFiSettingsButton = (Button) findViewById(R.id.wifiSettingsButton);
        imgBus = (ImageView) findViewById(R.id.img_bus);

        mPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mStatePaused) {
                    mRemoteService.requestContinueDownload();
                } else {
                    mRemoteService.requestPauseDownload();
                }
                setButtonPausedState(!mStatePaused);
            }
        });

        mWiFiSettingsButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
            }
        });

        Button resumeOnCell = (Button) findViewById(R.id.resumeOverCellular);
        resumeOnCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRemoteService.setDownloadFlags(IDownloaderService.FLAGS_DOWNLOAD_OVER_CELLULAR);
                mRemoteService.requestContinueDownload();
                mCellMessage.setVisibility(View.GONE);
            }
        });

    }

    /**
     * 计算验证速度的移动平均值，这样我们就不会感到紧张
     * 计算时间等。
     */
    static private final float SMOOTHING_FACTOR = 0.005f;

    /**
     * 由异步任务使用
     */
    private boolean mCancelValidation;

    /**
     * 浏览每个扩展APK文件，并将每个文件作为zip文件打开。
     * 计算每个文件的CRC，如果任何文件不匹配，则返回false。
     * 如果XAPKZipFile成功，@return true
     */
    @SuppressLint("StaticFieldLeak")
    private void validateXAPKZipFiles() {
        AsyncTask<Object, DownloadProgressInfo, Boolean> validationTask = new AsyncTask<Object, DownloadProgressInfo, Boolean>() {

            @Override
            protected void onPreExecute() {
                mDashboard.setVisibility(View.VISIBLE);
                mCellMessage.setVisibility(View.GONE);
                mStatusText.setText(R.string.text_verifying_download);
                mPauseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCancelValidation = true;
                    }
                });
                mPauseButton.setText(R.string.text_button_cancel_verify);
                super.onPreExecute();
            }

            @Override
            protected Boolean doInBackground(Object... params) {
                for (MyDownloaderActivity.XAPKFile xf : xAPKS) {
                    String fileName = Helpers.getExpansionAPKFileName(MyDownloaderActivity.this,
                            xf.mIsMain, xf.mFileVersion);
                    if (!Helpers.doesFileExist(MyDownloaderActivity.this, fileName,
                            xf.mFileSize, false))
                        return false;
                    fileName = Helpers
                            .generateSaveFileName(MyDownloaderActivity.this, fileName);
                    ZipResourceFile zrf;
                    byte[] buf = new byte[1024 * 256];
                    try {
                        zrf = new ZipResourceFile(fileName);
                        ZipEntryRO[] entries = zrf.getAllEntries();
                        /**
                         * First calculate the total compressed length
                         */
                        long totalCompressedLength = 0;
                        for (ZipEntryRO entry : entries) {
                            totalCompressedLength += entry.mCompressedLength;
                        }
                        float averageVerifySpeed = 0;
                        long totalBytesRemaining = totalCompressedLength;
                        long timeRemaining;
                        /**
                         * Then calculate a CRC for every file in the Zip file,
                         * comparing it to what is stored in the Zip directory
                         */
                        for (ZipEntryRO entry : entries) {
                            if (-1 != entry.mCRC32) {
                                long offset = entry.getOffset();
                                long length = entry.mCompressedLength;
                                CRC32 crc = new CRC32();
                                RandomAccessFile raf = new RandomAccessFile(fileName, "r");
                                raf.seek(offset);
                                long startTime = SystemClock.uptimeMillis();
                                while (length > 0) {
                                    int seek = (int) (length > buf.length ? buf.length : length);
                                    raf.readFully(buf, 0, seek);
                                    crc.update(buf, 0, seek);
                                    length -= seek;
                                    long currentTime = SystemClock.uptimeMillis();
                                    long timePassed = currentTime - startTime;
                                    if (timePassed > 0) {
                                        float currentSpeedSample = (float) seek / (float) timePassed;
                                        if (0 != averageVerifySpeed) {
                                            averageVerifySpeed = SMOOTHING_FACTOR
                                                    * currentSpeedSample
                                                    + (1 - SMOOTHING_FACTOR) * averageVerifySpeed;
                                        } else {
                                            averageVerifySpeed = currentSpeedSample;
                                        }
                                        totalBytesRemaining -= seek;
                                        timeRemaining = (long) (totalBytesRemaining / averageVerifySpeed);
                                        this.publishProgress(
                                                new DownloadProgressInfo(totalCompressedLength,
                                                        totalCompressedLength - totalBytesRemaining,
                                                        timeRemaining,
                                                        averageVerifySpeed)
                                        );
                                    }
                                    startTime = currentTime;
                                    if (mCancelValidation)
                                        return true;
                                }
                                if (crc.getValue() != entry.mCRC32) {
                                    Log.e(LOG_TAG, "CRC does not match for entry: "
                                            + entry.mFileName);
                                    Log.e(LOG_TAG, "In file: " + entry.getZipFileName());
                                    return false;
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        return false;
                    }
                }
                return true;
            }

            @Override
            protected void onProgressUpdate(DownloadProgressInfo... values) {
                onDownloadProgress(values[0]);
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                if (result) {
                    mDashboard.setVisibility(View.VISIBLE);
                    mCellMessage.setVisibility(View.GONE);
                    mStatusText.setText(R.string.text_validation_complete);
                    mPauseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });
                    mPauseButton.setText(android.R.string.ok);
                    getResource();
                } else {
                    mDashboard.setVisibility(View.VISIBLE);
                    mCellMessage.setVisibility(View.GONE);
                    mStatusText.setText(R.string.text_validation_failed);
                    mPauseButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });
                    mPauseButton.setText(android.R.string.cancel);
                }
                super.onPostExecute(result);
            }

        };
        validationTask.execute(new Object());
    }

    /**
     * 连接到我们的服务onResume.
     */
    @Override
    protected void onResume() {
        if (null != mDownloaderClientStub) {
            mDownloaderClientStub.connect(this);
        }
        super.onResume();
    }

    /**
     * 断开到我们的服务onStop
     */
    @Override
    protected void onStop() {
        if (null != mDownloaderClientStub) {
            mDownloaderClientStub.disconnect(this);
        }
        super.onStop();
    }

    /**
     * 关键实施细节。在onServiceConnected中，我们创建
     * 远程服务和封送拆收器。这就是我们传递客户信息的方式
     * 返回到服务，以便可以正确地通知客户端更改。我们
     * 每次我们重新连接到服务时都必须这样做。
     */
    @Override
    public void onServiceConnected(Messenger m) {
        mRemoteService = DownloaderServiceMarshaller.CreateProxy(m);
        mRemoteService.onClientUpdated(mDownloaderClientStub.getMessenger());
    }

    /**
     * 下载状态应触发UI中的更改，这可能很有用
     * 以将状态显示为有时不确定。此示例可以是
     * 被认为是一条准则。
     */
    @Override
    public void onDownloadStateChanged(int newState) {
        setState(newState);
        boolean showDashboard = true;
        boolean showCellMessage = false;
        boolean paused;
        boolean indeterminate;
        switch (newState) {
            case IDownloaderClient.STATE_IDLE:
                //STATE_IDLE表示服务正在侦听，因此
                //开始通过mRemoteService进行呼叫是安全的。
                paused = false;
                indeterminate = true;
                break;
            case IDownloaderClient.STATE_CONNECTING:
            case IDownloaderClient.STATE_FETCHING_URL:
                showDashboard = true;
                paused = false;
                indeterminate = true;
                break;
            case IDownloaderClient.STATE_DOWNLOADING:
                paused = false;
                showDashboard = true;
                indeterminate = false;
                break;

            case IDownloaderClient.STATE_FAILED_CANCELED:
            case IDownloaderClient.STATE_FAILED:
            case IDownloaderClient.STATE_FAILED_FETCHING_URL:
            case IDownloaderClient.STATE_FAILED_UNLICENSED:
                paused = true;
                showDashboard = false;
                indeterminate = false;
                break;
            case IDownloaderClient.STATE_PAUSED_NEED_CELLULAR_PERMISSION:
            case IDownloaderClient.STATE_PAUSED_WIFI_DISABLED_NEED_CELLULAR_PERMISSION:
                showDashboard = false;
                paused = true;
                indeterminate = false;
                showCellMessage = true;
                break;
            case IDownloaderClient.STATE_PAUSED_BY_REQUEST:
                paused = true;
                indeterminate = false;
                break;
            case IDownloaderClient.STATE_PAUSED_ROAMING:
            case IDownloaderClient.STATE_PAUSED_SDCARD_UNAVAILABLE:
                paused = true;
                indeterminate = false;
                break;
            case IDownloaderClient.STATE_COMPLETED:
                showDashboard = false;
                paused = false;
                indeterminate = false;
                validateXAPKZipFiles();
                return;
            default:
                paused = true;
                indeterminate = true;
                showDashboard = true;
        }
        int newDashboardVisibility = showDashboard ? View.VISIBLE : View.GONE;
        if (mDashboard.getVisibility() != newDashboardVisibility) {
            mDashboard.setVisibility(newDashboardVisibility);
        }
        int cellMessageVisibility = showCellMessage ? View.VISIBLE : View.GONE;
        if (mCellMessage.getVisibility() != cellMessageVisibility) {
            mCellMessage.setVisibility(cellMessageVisibility);
        }
        mPB.setIndeterminate(indeterminate);
        setButtonPausedState(paused);
    }

    /**
     * 基于progresinfo对象设置各种控件的状态
     * 从下载器服务发送。
     */
    @Override
    public void onDownloadProgress(DownloadProgressInfo progress) {
        mAverageSpeed.setText(getString(R.string.kilobytes_per_second,
                Helpers.getSpeedString(progress.mCurrentSpeed)));
        mTimeRemaining.setText(getString(R.string.time_remaining,
                Helpers.getTimeRemaining(progress.mTimeRemaining)));

        progress.mOverallTotal = progress.mOverallTotal;
        mPB.setMax((int) (progress.mOverallTotal >> 8));
        mPB.setProgress((int) (progress.mOverallProgress >> 8));
        mProgressPercent.setText(Long.toString(progress.mOverallProgress
                * 100 /
                progress.mOverallTotal) + "%");
        mProgressFraction.setText(Helpers.getDownloadProgressString
                (progress.mOverallProgress,
                        progress.mOverallTotal));
    }

    @Override
    protected void onDestroy() {
        this.mCancelValidation = true;
        super.onDestroy();
    }


    /**
     * 这是一个小助手类，它演示了
     * Market提供的扩展APK文件。您可能不希望硬编码
     * 诸如文件长度等可执行文件。。。你可能希望
     * 在应用程序开发期间关闭此代码。
     */
    private static class XAPKFile {
        //是否为Main/Patch
        public final boolean mIsMain;
        //版本号
        public final int mFileVersion;
        //obb文件扩展资源大小
        public final long mFileSize;

        XAPKFile(boolean isMain, int fileVersion, long fileSize) {
            mIsMain = isMain;
            mFileVersion = fileVersion;
            mFileSize = fileSize;
        }
    }

    //版本号动态获取，APK扩展包大小每次打包完更改其大小，目前想不到动态获取的方法
    private final XAPKFile[] xAPKS = {
            new XAPKFile(true, AndroidUtil.getVersionCode(MyApplication.getInstance()), 8894L)
    };

    private boolean expansionFilesDelivered() {
        for (XAPKFile xf : xAPKS) {
            String fileName = Helpers.getExpansionAPKFileName(this, xf.mIsMain, xf.mFileVersion);
            if (!Helpers.doesFileExist(this, fileName, xf.mFileSize, false))
                return false;
        }
        return true;
    }

    private void getResource() {
        ZipResourceFile zipResourceFile;
        try {
            zipResourceFile = APKExpansionSupport.getAPKExpansionZipFile(getApplicationContext(), 1, 0);
            AssetFileDescriptor fd = zipResourceFile.getAssetFileDescriptor("drawable/ic_shortcut_bus.png");
            Log.e(LOG_TAG, String.valueOf(fd.getLength()));
            InputStream inputStream =  zipResourceFile.getInputStream("drawable/ic_shortcut_bus.png");
            Drawable drawable = FormatTools.InputStream2Drawable(inputStream);
            imgBus.setBackground(drawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
package luo.library.base.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import luo.library.R;
import luo.library.base.base.BaseAndroid;
import luo.library.base.widget.BaseDialog;

/**
 * 版本更新
 */

public class UpdateManager {
    public String downLoadPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/downloads/";
    public int type = 0;//更新方式，0：引导更新，1：安装更新，2：强制更新
    public String url = "";//apk下载地址
    public String updateMessage = "";//更新内容
    public String fileName = null;//文件名
    public boolean isDownload = false;//是否下载
    public Notification notification;
    public RemoteViews contentView;
    public NotificationManager notificationManager;
    public BaseDialog dialog;

    public static UpdateManager updateManager;

    public static UpdateManager getInstance() {
        if (updateManager == null) {
            updateManager = new UpdateManager();
        }
        return updateManager;
    }

    private UpdateManager() {

    }

    /**
     * 弹出版本更新提示框
     */
    public void showDialog(final Context context) {
        String title = "";
        String left = "";
        boolean cancelable = true;
        if (type == 1 | isDownload) {
            title = "安装新版本";
            left = "立即安装";
        } else {
            title = "发现新版本";
            left = "立即更新";
        }
        if (type == 2) {
            cancelable = false;
        }
        dialog = new BaseDialog.Builder(context).setTitle(title).setMessage(updateMessage).setCancelable(cancelable)
                .setLeftClick(left, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (type == 1 | isDownload) {
                            installApk(context, new File(downLoadPath, fileName));
                        } else {
                            if (url != null && !TextUtils.isEmpty(url)) {
                                createNotification(context);
                                downloadFile(context, true);
                            } else {
                                Toast.makeText(context, "下载地址错误", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                })
                .setRightClick("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        if (type == 2) {
                            System.exit(0);
                        }
                    }
                })
                .create();
        dialog.show();
    }


    /**
     * 下载apk
     *
     * @param context
     * @param installApk 下载完成后是否立即安装
     */
    public void downloadFile(final Context context, final boolean installApk) {
        RequestParams params = new RequestParams(url);
        params.setSaveFilePath(downLoadPath + fileName);
        x.http().request(HttpMethod.GET, params, new Callback.ProgressCallback<File>() {

            @Override
            public void onSuccess(File result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(context, ex.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                //实时更新通知栏进度条
                if (type != 1) {
                    notifyNotification(current, total);
                }
                if (total == current) {
                    notificationManager.cancelAll();
                    if (installApk) {
                        installApk(context, new File(downLoadPath, fileName));
                    } else {
                        showDialog(context);
                    }
                }
            }
        });
    }


    @SuppressWarnings("deprecation")
    public void createNotification(Context context) {
        notification = new Notification(
                BaseAndroid.getBaseConfig().getAppLogo(),//应用的图标
                "安装包正在下载...",
                System.currentTimeMillis());
        /*** 自定义  Notification 的显示****/
        contentView = new RemoteViews(context.getPackageName(), R.layout.notification_item);
        contentView.setProgressBar(R.id.progress, 100, 0, false);
        contentView.setTextViewText(R.id.tv_progress, "0%");
        contentView.setImageViewResource(R.id.ic_logo, BaseAndroid.getBaseConfig().getAppLogo());
        notification.contentView = contentView;
        notification.flags = Notification.DEFAULT_ALL;
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, notification);
    }

    public void notifyNotification(long percent, long length) {
        contentView.setTextViewText(R.id.tv_progress, (percent * 100 / length) + "%");
        contentView.setProgressBar(R.id.progress, (int) length, (int) percent, false);
        notification.contentView = contentView;
        notificationManager.notify(0, notification);
    }

    /**
     * 安装apk
     *
     * @param context 上下文
     * @param file    APK文件
     */
    public void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    /**
     * @return 当前应用的版本号
     */
    public int getVersionCode(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            int version = info.versionCode;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 判断当前网络是否wifi
     */
    public boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public UpdateManager setUrl(String url) {
        this.url = url;
        return this;
    }

    public UpdateManager setType(int type) {
        this.type = type;
        return this;
    }

    public UpdateManager setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
        return this;
    }

    public UpdateManager setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public UpdateManager setIsDownload(boolean download) {
        isDownload = download;
        return this;
    }

}

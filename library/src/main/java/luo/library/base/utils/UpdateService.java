package luo.library.base.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import luo.library.R;
import luo.library.base.base.BaseAndroid;

/**
 * 版本更新服务
 */

public class UpdateService extends Service {

    public static final String DOWNLOAD_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/downloads/";
    private String url;//下载链接
    private String fileName = null;//文件名
    private Notification notification;
    private RemoteViews contentView;
    private NotificationManager notificationManager;

    private static final int URL_ERROR = 1;
    private static final int NET_ERROR = 2;
    private static final int DOWNLOAD_SUCCESS = 3;
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case DOWNLOAD_SUCCESS:
                    installApk(UpdateService.this, new File(DOWNLOAD_PATH, fileName));
                    Toast.makeText(UpdateService.this, "下载完成", Toast.LENGTH_SHORT).show();
                    break;
                case URL_ERROR:
                    Toast.makeText(UpdateService.this, "下载地址错误", Toast.LENGTH_SHORT).show();
                    break;
                case NET_ERROR:
                    Toast.makeText(UpdateService.this, "连接失败，请检查网络设置", Toast.LENGTH_SHORT).show();
            }
        }
    };


    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            url = intent.getStringExtra("url");
            if (url != null && !TextUtils.isEmpty(url)) {
                createNotification();
                downloadFile();
            } else {
                mHandler.sendEmptyMessage(URL_ERROR);
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }

    void downloadFile() {
        File dir = new File(DOWNLOAD_PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }
        fileName = this.url.substring(this.url.lastIndexOf("/") + 1, this.url.length());
        if (fileName == null && TextUtils.isEmpty(fileName) && !fileName.contains(".apk")) {
            fileName = getPackageName() + ".apk";
        }
        RequestParams params = new RequestParams(url);
        params.setSaveFilePath(DOWNLOAD_PATH + fileName);
        x.http().request(HttpMethod.GET, params, new Callback.ProgressCallback<File>() {

            @Override
            public void onSuccess(File result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                mHandler.sendEmptyMessage(NET_ERROR);
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
                notifyNotification(current, total);
                if (total == current) {
                    mHandler.sendEmptyMessage(DOWNLOAD_SUCCESS);
                }
            }
        });
    }


    @SuppressWarnings("deprecation")
    public void createNotification() {
        notification = new Notification(
                BaseAndroid.getBaseConfig().getAppLogo(),//应用的图标
                "安装包正在下载...",
                System.currentTimeMillis());
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        //notification.flags = Notification.FLAG_AUTO_CANCEL;

        /*** 自定义  Notification 的显示****/
        contentView = new RemoteViews(getPackageName(), R.layout.notification_item);
        contentView.setProgressBar(R.id.progress, 100, 0, false);
        contentView.setTextViewText(R.id.tv_progress, "0%");
        contentView.setImageViewResource(R.id.ic_logo, BaseAndroid.getBaseConfig().getAppLogo());
        notification.contentView = contentView;

        /*updateIntent = new Intent(this, AboutActivity.class);
          updateIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
     	updateIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
     	pendingIntent = PendingIntent.getActivity(this, 0, updateIntent, 0);
      	notification.contentIntent = pendingIntent;*/
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        //设置notification的PendingIntent
        /*Intent intt = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,100, intt,Intent.FLAG_ACTIVITY_NEW_TASK	| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		notification.contentIntent = pi;*/

        notificationManager.notify(R.layout.notification_item, notification);
    }

    private void notifyNotification(long percent, long length) {
        contentView.setTextViewText(R.id.tv_progress, (percent * 100 / length) + "%");
        contentView.setProgressBar(R.id.progress, (int) length, (int) percent, false);
        notification.contentView = contentView;
        notificationManager.notify(R.layout.notification_item, notification);
    }

    /**
     * 安装apk
     *
     * @param context 上下文
     * @param file    APK文件
     */
    public static void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");
        context.startActivity(intent);
    }
}

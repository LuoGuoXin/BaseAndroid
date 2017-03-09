package luo.library.base.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;

import luo.library.R;
import luo.library.base.base.BaseAndroid;

/**
 * Created by WIN7 on 2017/3/9.
 */

public class UpdateManager {
    public String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/downloads/";
    public int type = 0;//更新方式，0：引导更新，1：安装更新，2：强制更新
    public String url = "";//apk下载地址
    public String updateMessage = "";//更新内容
    public String fileName = null;//文件名
    public Notification notification;
    public RemoteViews contentView;
    public NotificationManager notificationManager;

    public static UpdateManager updateManager;

    public static UpdateManager getInstance() {
        if (updateManager == null) {
            updateManager = new UpdateManager();
        }
        return updateManager;
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


    public void showPop(final Context context, View view) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.base_dialog, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        TextView left = (TextView) contentView.findViewById(R.id.tv_left);
        TextView right = (TextView) contentView.findViewById(R.id.tv_right);
        final TextView title = (TextView) contentView.findViewById(R.id.tv_title);
        TextView message = (TextView) contentView.findViewById(R.id.tv_message);
        RelativeLayout relativeLayout = (RelativeLayout) contentView.findViewById(R.id.rl);

        if (type == 0) {
            title.setText("发现新版本");
            left.setText("立即更新");
        } else {
            title.setText("安装新版本");
            left.setText("立即安装");
        }
        right.setText("取消");
        message.setText(updateMessage);
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if (type == 0 | type == 2) {
                    if (url != null && !TextUtils.isEmpty(url)) {
                        createNotification(context);
                        downloadFile(context, view, true);
                    } else {
                        Toast.makeText(context, "下载地址错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    installApk(context, new File(DOWNLOAD_PATH, fileName));
                }

            }
        });
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if (type == 2) {
                    System.exit(0);
                }
            }
        });
        if (type != 2) {
            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    popupWindow.dismiss();
                }
            });
        }
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
    }


    public void downloadFile(final Context context, final View view, final boolean installApk) {
        File dir = new File(DOWNLOAD_PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }
        fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
        if (fileName == null && TextUtils.isEmpty(fileName) && !fileName.contains(".apk")) {
            fileName = context.getPackageName() + ".apk";
        }
        File file=new File(DOWNLOAD_PATH + fileName);
        if (file.exists()){
            if (installApk) {
                installApk(context, new File(DOWNLOAD_PATH, fileName));
            } else {
                showPop(context, view);
            }
            return;
        }
        RequestParams params = new RequestParams(url);
        params.setSaveFilePath(DOWNLOAD_PATH + fileName);
        x.http().request(HttpMethod.GET, params, new Callback.ProgressCallback<File>() {

            @Override
            public void onSuccess(File result) {

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(context, "网络错误", Toast.LENGTH_SHORT).show();
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
                if (type != 1) {
                    notifyNotification(current, total);
                }
                if (total == current) {
                    if (installApk) {
                        installApk(context, new File(DOWNLOAD_PATH, fileName));
                    } else {
                        showPop(context, view);
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
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        //notification.flags = Notification.FLAG_AUTO_CANCEL;

        /*** 自定义  Notification 的显示****/
        contentView = new RemoteViews(context.getPackageName(), R.layout.notification_item);
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
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //设置notification的PendingIntent
        /*Intent intt = new Intent(this, MainActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this,100, intt,Intent.FLAG_ACTIVITY_NEW_TASK	| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		notification.contentIntent = pi;*/

        notificationManager.notify(R.layout.notification_item, notification);
    }

    public void notifyNotification(long percent, long length) {
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
    public void installApk(Context context, File file) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

}

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
 * 版本更新
 */

public class UpdateManager {
    public String DOWNLOAD_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/downloads/";
    public int type = 0;//更新方式，0：引导更新，1：安装更新，2：强制更新
    public String url = "";//apk下载地址
    public String updateMessage = "";//更新内容
    public View view;//activity根布局的view
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

    private UpdateManager() {

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


    public UpdateManager setView(View view) {
        this.view = view;
        return this;
    }

    /**
     * 弹出版本更新提示框
     */
    public void showPop(final Context context) {
        View contentView = LayoutInflater.from(context).inflate(R.layout.base_dialog, null);
        final PopupWindow popupWindow = new PopupWindow(contentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        final TextView title = (TextView) contentView.findViewById(R.id.tv_title);
        TextView left = (TextView) contentView.findViewById(R.id.tv_left);
        TextView right = (TextView) contentView.findViewById(R.id.tv_right);
        TextView message = (TextView) contentView.findViewById(R.id.tv_message);
        RelativeLayout relativeLayout = (RelativeLayout) contentView.findViewById(R.id.rl);

        //根据更新方式显示不同的文案
        if (type == 0) {
            title.setText("发现新版本");
            left.setText("立即更新");
        } else {
            title.setText("安装新版本");
            left.setText("立即安装");
        }
        right.setText("取消");
        message.setText(updateMessage);
        //升级按钮，根据更新方式来做不同的操作，0和2是下载apk并显示通知栏，1是直接安装apk
        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if (type == 0 | type == 2) {
                    if (url != null && !TextUtils.isEmpty(url)) {
                        createNotification(context);
                        downloadFile(context, true);
                    } else {
                        Toast.makeText(context, "下载地址错误", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    installApk(context, new File(DOWNLOAD_PATH, fileName));
                }

            }
        });
        //取消按钮，当更新方式为强制更新时，直接退出
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if (type == 2) {
                    System.exit(0);
                }
            }
        });
        //点击外边区域消失窗口
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


    /**
     * 下载apk
     * @param context
     * @param installApk 下载完成后是否需要安装
     */
    public void downloadFile(final Context context, final boolean installApk) {
        File dir = new File(DOWNLOAD_PATH);
        if (!dir.exists()) {
            dir.mkdir();
        }
        //截取apk的文件名
        fileName = url.substring(url.lastIndexOf("/") + 1, url.length());
        if (fileName == null && TextUtils.isEmpty(fileName) && !fileName.contains(".apk")) {
            fileName = context.getPackageName() + ".apk";
        }
        //判断是否存在了这个apk，存在了就不下载了，直接安装或者弹框提示
        File file = new File(DOWNLOAD_PATH + fileName);
        if (file.exists()) {
            if (installApk) {
                installApk(context, new File(DOWNLOAD_PATH, fileName));
            } else {
                showPop(context);
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
                //实时更新通知栏进度条
                if (type != 1) {
                    notifyNotification(current, total);
                }
                if (total == current) {
                    if (installApk) {
                        installApk(context, new File(DOWNLOAD_PATH, fileName));
                    } else {
                        showPop(context);
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
        if (activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

}

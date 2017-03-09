package luo.library.base.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import luo.library.base.utils.UpdateManager;

/**
 *
 */

public class BaseAndroid {
    public static BaseConfig baseConfig;

    public static void init(BaseConfig config) {
        baseConfig = config;
    }

    public static BaseConfig getBaseConfig() {
        if (baseConfig == null) {
            throw new IllegalArgumentException("请先调用init方法");
        }
        return baseConfig;
    }

    /**
     * 版本更新
     *
     * @param context
     * @param versionCode   版本号
     * @param url           apk下载地址
     * @param updateMessage 更新内容
     * @param isForced      是否强制更新
     */
    public static void checkUpdate(Context context, View view, int versionCode, String url, String updateMessage, boolean isForced) {
        if (versionCode > getVersionCode(context)) {
            int type = 0;//更新方式，0：引导更新，1：安装更新，2：强制更新
            if (isWifi(context)) {
                type = 1;
            }
            if (isForced) {
                type = 2;
            }
            UpdateManager.getInstance().setType(type).setUrl(url).setUpdateMessage(updateMessage);
            switch (type) {
                case 0:
                    UpdateManager.getInstance().showPop(context, view);
                    break;
                case 1:
                    UpdateManager.getInstance().downloadFile(context, view, false);
                    break;
                case 2:
                    UpdateManager.getInstance().showPop(context, view);
                    break;
            }
        }

    }

    /**
     * @return 当前应用的版本号
     */
    public static int getVersionCode(Context context) {
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
    private static boolean isWifi(Context mContext) {
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

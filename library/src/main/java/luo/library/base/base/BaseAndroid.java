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
        if (versionCode > UpdateManager.getInstance().getVersionCode(context)) {
            int type = 0;//更新方式，0：引导更新，1：安装更新，2：强制更新
            if (UpdateManager.getInstance().isWifi(context)) {
                type = 1;
            }
            if (isForced) {
                type = 2;
            }
            //设置参数
            UpdateManager.getInstance().setView(view).setType(type).setUrl(url).setUpdateMessage(updateMessage);
            switch (type) {
                case 0:
                    //非wifi情况下，先弹框后下载
                    UpdateManager.getInstance().showPop(context);
                    break;
                case 1:
                    //wifi情况下，先下载后弹框
                    UpdateManager.getInstance().downloadFile(context, false);
                    break;
                case 2:
                    //强制更新情况下，无论是否wifi都应该是先弹框
                    UpdateManager.getInstance().showPop(context);
                    break;
            }
        }

    }


}

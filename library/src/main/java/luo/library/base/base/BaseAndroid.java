package luo.library.base.base;

import android.content.Context;
import android.content.Intent;

import luo.library.base.utils.UpdateService;

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
     * 下载apk
     */
    public static void updateApk(Context context,String url){
        Intent intent = new Intent(context,UpdateService.class);
        intent.putExtra("url", url);
        context.startService(intent);
    }
}

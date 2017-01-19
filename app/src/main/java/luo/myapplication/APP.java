package luo.myapplication;

import android.app.Application;

import org.xutils.x;

import luo.library.base.base.BaseConstant;

/**
 * Created by Administrator on 2017/1/6.
 */

public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
        //设置标题栏背景颜色
        BaseConstant.titleViewBackgroundColor = "#408F40";
    }
}

package luo.myapplication;

import android.app.Application;

import org.xutils.x;

/**
 * Created by Administrator on 2017/1/6.
 */

public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);
    }
}

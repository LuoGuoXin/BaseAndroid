package luo.myapplication;

import android.app.Application;

import org.xutils.x;

import luo.library.base.base.BaseAndroid;
import luo.library.base.base.BaseConfig;

/**
 * Created by Administrator on 2017/1/6.
 */

public class APP extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);

        BaseAndroid.init(new BaseConfig()
                .setAppColor(R.color.colorPrimary)//app主调颜色，用于标题栏等背景颜色
                .setAppLogo(R.mipmap.ic_launcher)//app图标
                .setFailPicture(R.mipmap.ic_launcher)//加载加载失败和加载中显示的图
                .setLoadingView(R.drawable.gif)//设置加载框的gif图
                .setCode(0)//网络请求成功返回的code数字，默认为1
                .setHttpCode("code")//网络请求返回的code字段名称，默认为code
                .setHttpMessage("message")//网络请求返回的message字段名称，默认为message
                .setHttpResult("response"));//网络请求返回的result字段名称，默认为result
    }
}

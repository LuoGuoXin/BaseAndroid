package luo.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import java.util.List;

import luo.library.base.base.BaseActivity;
import luo.library.base.base.BaseAndroid;
import luo.library.base.base.BaseDb;
import luo.library.base.base.BaseHttp;
import luo.library.base.base.BaseImage;
import luo.library.base.base.BaseWebViewActivity;
import luo.library.base.utils.GsonUtil;
import luo.library.base.widget.StatusBarCompat;
import luo.myapplication.model.PersonBean;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("BaseAndroid");
        hideBack();

    }

    @Event(R.id.btn_webview)
    private void webview(View view) {
        Intent intent = new Intent(this, BaseWebViewActivity.class);
        intent.putExtra(BaseWebViewActivity.URL, "http://www.baidu.com");
        startActivity(intent);
    }

    @Event(R.id.btn_loadingview)
    private void loadingView(View view) {
        openActivity(LoadingViewActivity.class);
    }

    @Event(R.id.btn_newsList)
    private void newsList(View view) {
        openActivity(NewsListActivity.class);
    }

    //常用操作
    void init() {
        //设置标题栏标题，记得先在布局上面添加标题栏布局： <include layout="@layout/titleview_layout"/>
        setTitleText("标题");

        //设置显示标题栏右边的按钮
        setRightButtonText("编辑", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //右边按钮点击事件
            }
        });

        //隐藏返回箭头
        hideBack();

        //弹出Toast
        showToast("Toast");

        //弹出加载窗口
        startProgressDialog();

        //弹出加载窗口，自定义提示
        startProgressDialog("loading...");

        //隐藏加载窗口
        stopProgressDialog();

        //设置透明状态栏
        StatusBarCompat.translucentStatusBar(this);

        //设置状态栏颜色
        StatusBarCompat.setStatusBarColor(this, Color.BLUE);

        //获取 TextView 和 EditView 的内容
        TextView textView = new TextView(this);
        getStr(textView);

        //判断字符、TextView和EditView的内容是否为空
        isNull(textView);

        //打开一个Activity
        //  openActivity(XX.class);

        //保存内容到 SharedPreferences，第一个参数为key，第二个参数为保存的内容
        putSp("key", "内容");

        //从SharedPreferences上获取数据，第一个参数为key，第二个参数为默认内容
        getSp("key", "");

        //清除SharedPreferences的数据
        clearSp();

        //版本更新
        BaseAndroid.checkUpdate(MainActivity.this,findViewById(R.id.activity_main), 2, "http://f5.market.mi-img.com/download/AppStore/0f4a347f5ce5a7e01315dda1ec35944fa56431d44/luo.footprint.apk", "更新了XXX\n修复OOO", false);

    }

    //图片显示操作
    void image() {
        //显示正常的图片，本地：new File("/ssd/base.jpg")；drawable：R.drawable.base
        //图片加载使用 Glide ，感觉还是挺流畅好用的
        ImageView imageView = new ImageView(this);
        BaseImage.getInstance().displayImage(MainActivity.this, "http://www.base.com/base.jpg", imageView);

        //显示圆角图片
        BaseImage.getInstance().displayRoundImage(MainActivity.this, "http://www.base.com/base.jpg", imageView);

        //显示圆形图片
        BaseImage.getInstance().displayCricleImage(MainActivity.this, "http://www.base.com/base.jpg", imageView);
    }

    //网络请求操作
    //封装了下，onSuccess里面返回的result是请求成功（code=1）里面的内容，onError是code不等于1时的内容，网络错误时，code为-1
    void http() {
        RequestParams params = new RequestParams("http://115.28.13.1/lovect/bannerAction");
        BaseHttp.get(params, new BaseHttp.BaseHttpCallback() {
            @Override
            public void onSuccess(String result) {
                LogUtil.i(result);
                //  PersonBean personBean = GsonUtil.GsonToBean(result, PersonBean.class);
                //  List<PersonBean> list = GsonUtil.GsonToList(result, PersonBean.class);
            }

            @Override
            public void onError(int code, String message) {
                showToast(message);
            }
        });
    }

    //数据库操作
    void db() {
        BaseDb.initDb();
        //添加数据
        for (int i = 0; i < 5; i++) {
            PersonBean personBean = new PersonBean();
            personBean.setName("小罗" + i);
            personBean.setAge(25 + i);
            BaseDb.add(personBean);
        }
        //查找数据
        List<PersonBean> list = BaseDb.find(PersonBean.class);
        LogUtil.i(GsonUtil.GsonString(list));

        //修改数据
        PersonBean bean = list.get(3);
        bean.setName("修改名字为小小罗");
        BaseDb.update(bean);

        //删除一条数据
        PersonBean personBean = list.get(2);
        BaseDb.delete(personBean);
    }
}

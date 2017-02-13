package luo.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.xutils.common.util.LogUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import luo.library.base.base.BaseActivity;
import luo.library.base.base.BaseAndroid;
import luo.library.base.base.BaseDb;
import luo.library.base.base.BaseHttp;
import luo.library.base.base.BaseImage;
import luo.library.base.utils.GsonUtil;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewInject(R.id.image)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("标题");

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

        //下载更新版本
        BaseAndroid.updateApk(MainActivity.this, "http://f5.market.mi-img.com/download/AppStore/0f4a347f5ce5a7e01315dda1ec35944fa56431d44/luo.footprint.apk");

    }

    //图片显示操作
    void image() {
        //显示正常的图片，本地：new File("/ssd/base.jpg")；drawable：R.drawable.base
        //图片加载使用 Glide ，感觉还是挺流畅好用的
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

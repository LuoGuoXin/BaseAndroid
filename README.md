# BaseAndroid
BaseAndroid 是一个app基本库，每次创建一个app项目，基本都会用到一些内容，所以我把它集合起来，做成一个库的形式，方便导入使用。第一版，没什么经验，希望大家多多指出错误，提些建议，大胆的提 Issues吧。
<br>目前版本的功能：
<br>1、标题栏操作
<br>2、BaseActivity BaseFramgent BaseFramgentActivity
<br>3、常用操作
<br>4、图片显示
<br>5、网络请求
<br>6、数据库操作
<br>该库会根据大家需求陆续更新，希望大家支持下
<br><br>集成进去的库目前有：<br>
 compile 'org.xutils:xutils:3.3.40'<br>
 compile 'com.github.bumptech.glide:glide:3.7.0'<br>
 compile 'com.google.code.gson:gson:2.8.0'<br>
 项目中如果有上面用的库不需要再导入
 
## 导入方法
 Step 1. Add it in your root build.gradle at the end of repositories.

```java
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
Step 2. Add the dependency

```java
dependencies {
    ...
   compile 'com.github.LuoGuoXin:BaseAndroid:1.0.3'
}
```

## 更新说明
<br>2017/02/15 增加加载框，增加BaseWebViewActivity
<br>2017/02/10 增加版本更新下载功能（通知栏显示进度条，下载完成提示安装）
<br>2017/02/05 增加网络请求的设置参数、修改标题栏背景色的设置方式（看下面初始化示例）

## 使用示例
在Application里面加上
```java
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
                .setCode(0)//网络请求成功返回的code数字，默认为1
                .setHttpCode("code")//网络请求返回的code字段名称，默认为code
                .setHttpMessage("msg")//网络请求返回的message字段名称，默认为message
                .setHttpResult("resp"));//网络请求返回的result字段名称，默认为result
    }
}
```
```java
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
	
	//打开网页，一个好用的webview
	  Intent intent = new Intent(this, BaseWebViewActivity.class);
        intent.putExtra(BaseWebViewActivity.URL, "http://www.baidu.com");
        startActivity(intent);
	
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

        //下载更新版本
        BaseAndroid.updateApk(MainActivity.this, "http://f5.market.mi-img.com/download/AppStore/0f4a347f5ce5a7e01315dda1ec35944fa56431d44/luo.footprint.apk");

    }

    //图片显示操作
    void image(){
        //显示正常的图片，本地：new File("/ssd/base.jpg")；drawable：R.drawable.base
        //图片加载使用 Glide ，感觉还是挺流畅好用的
        BaseImage.getInstance().displayImage(MainActivity.this, "http://www.base.com/base.jpg", imageView);

        //显示圆角图片
        BaseImage.getInstance().displayRoundImage(MainActivity.this,"http://www.base.com/base.jpg", imageView);

        //显示圆形图片
        BaseImage.getInstance().displayCricleImage(MainActivity.this,"http://www.base.com/base.jpg", imageView);
    }

    //网络请求操作
    //封装了下，onSuccess里面返回的result是请求成功（code=1）里面的内容，onError是code不等于1时的内容，网络错误时，code为-1
    void http() {
        RequestParams params = new RequestParams("http://www.base");
        BaseHttp.get(params, new BaseHttp.BaseHttpCallback() {
            @Override
            public void onSuccess(String result) {
                PersonBean personBean = GsonUtil.GsonToBean(result, PersonBean.class);
                List<PersonBean> list = GsonUtil.GsonToList(result, PersonBean.class);
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
```


 

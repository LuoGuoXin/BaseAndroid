package luo.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;

import luo.library.base.base.BaseActivity;

/**
 * 加载框使用示例
 */
@ContentView(R.layout.activity_loading_view)
public class LoadingViewActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("加载框使用");
    }

    @Event({R.id.btn_load, R.id.btn_loadText, R.id.btn_loadgif})
    private void loadView(View view) {
        switch (view.getId()) {
            case R.id.btn_load://打开默认的加载框
                startProgressDialog();
                close();
                break;
            case R.id.btn_loadText://打开自定义文字的加载框
                startProgressDialog("正在下载...");
                close();
                break;
            case R.id.btn_loadgif://设置一张gif图片为加载框的布局，gif文件尽量小
                startGifProgressDialog();
                close();
                break;
        }
    }

    void close() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                stopProgressDialog();
            }
        }, 5000);//5秒后关闭加载框
    }

}

package luo.myapplication;

import android.os.Bundle;
import android.widget.ImageView;

import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.List;

import luo.library.base.base.BaseActivity;
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
        setTitleText("示例");

        BaseImage.getInstance().displayImage(MainActivity.this, "", imageView);
    }

    //请求示例
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
}

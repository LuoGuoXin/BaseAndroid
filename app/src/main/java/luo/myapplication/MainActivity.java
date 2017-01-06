package luo.myapplication;

import android.os.Bundle;
import android.widget.ImageView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import luo.library.base.BaseActivity;
import utils.ImageLoader;

@ContentView(R.layout.activity_main)
public class MainActivity extends BaseActivity {

    @ViewInject(R.id.image)
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitleText("哈哈");

        ImageLoader.getInstance().displayImage(MainActivity.this, "", imageView);
    }
}

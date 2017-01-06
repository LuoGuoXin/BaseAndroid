package luo.myapplication;

import android.os.Bundle;

import luo.library.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitleText("哈哈");
    }
}

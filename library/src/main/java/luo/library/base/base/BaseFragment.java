package luo.library.base.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.x;

import luo.library.base.utils.SpUtils;
import luo.library.base.widget.LoadingDialog;


public class BaseFragment extends Fragment {

    private boolean injected = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        injected = true;
        return x.view().inject(this, inflater, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!injected) {
            x.view().inject(this, this.getView());
        }
    }

    /**
     * 开启浮动加载进度条
     */
    public void startProgressDialog() {
        LoadingDialog.showDialogForLoading(getActivity());
    }

    /**
     * 开启浮动加载进度条Gif
     */
    public void startGifProgressDialog() {
        LoadingDialog.showDialogForLoadingGif(getActivity());
    }

    /**
     * 开启浮动加载进度条
     *
     * @param msg
     */
    public void startProgressDialog(String msg) {
        LoadingDialog.showDialogForLoading(getActivity(), msg, true);
    }

    /**
     * 停止浮动加载进度条
     */
    public void stopProgressDialog() {
        LoadingDialog.cancelDialogForLoading();
    }

    /**
     * 弹出Toast
     */
    public void showToast(String string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_SHORT).show();
    }

    /**
     * 获取EditView的文字
     */
    public String getStr(EditText editText) {
        return editText.getText().toString().trim();
    }

    /**
     * 获取TextView的文字
     */
    public String getStr(TextView textView) {
        return textView.getText().toString();
    }

    /**
     * 获取string的文字
     */
    public String getStr(int id) {
        return getResources().getString(id);
    }

    /**
     * 检查字符串是否是空对象或空字符串
     */
    public boolean isNull(String str) {
        if (null == str || "".equals(str) || "null".equalsIgnoreCase(str)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查EditView是否是空对象或空字符串
     */
    public boolean isNull(EditText str) {
        if (null == str.getText().toString() || "".equals(str.getText().toString())
                || "null".equalsIgnoreCase(str.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查TextView是否是空对象或空字符串
     */
    public boolean isNull(TextView str) {
        if (null == str.getText().toString() || "".equals(str.getText().toString())
                || "null".equalsIgnoreCase(str.getText().toString())) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 启动Activity
     */
    public void openActivity(Class<?> cls) {
        Intent intent = new Intent(getActivity(), cls);
        startActivity(intent);
    }

    /**
     * 保存sp数据
     *
     * @param key
     * @param object
     */
    public void putSp(String key, Object object) {
        SpUtils.put(getActivity(), key, object);
    }

    /**
     * 清除Sp数据
     */
    public void clearSp() {
        SpUtils.clear(getActivity());
    }

    /**
     * 获取sp数据
     *
     * @param key
     * @param object
     * @return
     */
    public Object getSp(String key, Object object) {
        return SpUtils.get(getActivity(), key, object);
    }

}

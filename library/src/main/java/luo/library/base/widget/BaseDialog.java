package luo.library.base.widget;

import android.content.Context;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import luo.library.R;

/**
 * Created by WIN7 on 2017/3/9.
 */

public class BaseDialog {
    Context context;
    android.app.AlertDialog ad;
    TextView titleView;
    TextView messageView;
    TextView leftView;
    TextView rightView;

    public BaseDialog(Context context) {
        this.context = context;
        ad = new android.app.AlertDialog.Builder(context).create();
       ad.show();
        Window window = ad.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setContentView(R.layout.base_dialog);
        WindowManager m = window.getWindowManager();
        Display d = m.getDefaultDisplay(); // 获取屏幕宽、高用

        WindowManager.LayoutParams p = ad.getWindow().getAttributes(); // 获取对话框当前的参数值
        // p.height = (int) (d.getHeight() * 0.6); // 高度设置为屏幕的0.6
        p.width = (int) (d.getWidth() * 0.9); // 宽度设置为屏幕的0.95
        window.setAttributes(p);

        titleView = (TextView) window.findViewById(R.id.tv_title);
        messageView = (TextView) window.findViewById(R.id.tv_message);
        leftView = (TextView) window.findViewById(R.id.tv_left);
        rightView = (TextView) window.findViewById(R.id.tv_right);
    }

    public void setTitle(int resId) {
        titleView.setText(resId);
    }

    public void setTitle(String title) {
        titleView.setText(title);
    }

    public void setMessage(int resId) {
        messageView.setText(resId);
    }

    public void setMessage(String message) {
        messageView.setText(message);
    }

    public void setLeftText(int resId) {
        leftView.setText(resId);
    }

    public void setLeftText(String message) {
        leftView.setText(message);
    }

    public void setRightText(int resId) {
        rightView.setText(resId);
    }

    public void setRightText(String message) {
        rightView.setText(message);
    }


    /**
     * 设置左边按钮点击事件
     */
    public void setLeftClickListener(final View.OnClickListener listener) {
        leftView.setOnClickListener(listener);
    }


    /**
     * 设置右边按钮点击事件
     */
    public void setRightClickListener(final View.OnClickListener listener) {
        rightView.setOnClickListener(listener);
    }

    /**
     * 设置点击外部是否可以关闭
     */
    public void setCancelable(boolean cancelable){
        ad.setCancelable(cancelable);
    }

    /**
     * 关闭对话框
     */
    public void dismiss() {
        ad.dismiss();
    }

    /**
     * 显示对话框
     */
    public void show() {
        ad.show();
    }
}

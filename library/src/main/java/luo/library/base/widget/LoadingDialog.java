package luo.library.base.widget;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import luo.library.R;
import luo.library.base.base.BaseAndroid;


/**
 * 弹窗浮动加载进度条
 */
public class LoadingDialog {
    /**
     * 加载数据对话框
     */
    private static Dialog mLoadingDialog;

    /**
     * 显示加载对话框
     *
     * @param context    上下文
     * @param msg        对话框显示内容
     * @param cancelable 对话框是否可以取消
     */
    public static Dialog showDialogForLoading(Activity context, String msg, boolean cancelable) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        TextView loadingText = (TextView) view.findViewById(R.id.id_tv_loading_dialog_text);
        loadingText.setText(msg);

        mLoadingDialog = new Dialog(context, R.style.CustomProgressDialog);
        mLoadingDialog.setCancelable(cancelable);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mLoadingDialog.show();
        return mLoadingDialog;
    }

    public static Dialog showDialogForLoading(Activity context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        TextView loadingText = (TextView) view.findViewById(R.id.id_tv_loading_dialog_text);
        loadingText.setText("加载中...");
        mLoadingDialog = new Dialog(context, R.style.CustomProgressDialog);
        mLoadingDialog.setCancelable(true);
        mLoadingDialog.setCanceledOnTouchOutside(false);
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        mLoadingDialog.show();
        return mLoadingDialog;
    }

    public static Dialog showDialogForLoadingGif(Activity context) {
        if (BaseAndroid.getBaseConfig().getLoadingView() == 0) {
            Toast.makeText(context, "请先设置gif图片", Toast.LENGTH_SHORT).show();
            return null;
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.dialog_loadinggif, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.gif);
            Glide.with(context).load(BaseAndroid.getBaseConfig().getLoadingView())
                    .asGif().placeholder(BaseAndroid.getBaseConfig().getFailPicture()).into(imageView);
            mLoadingDialog = new Dialog(context, R.style.CustomProgressDialog);
            mLoadingDialog.setCancelable(true);
            mLoadingDialog.setCanceledOnTouchOutside(false);
            mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            mLoadingDialog.show();
            return mLoadingDialog;
        }
    }

    /**
     * 关闭加载对话框
     */
    public static void cancelDialogForLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.cancel();
        }
    }
}

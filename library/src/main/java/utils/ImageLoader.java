package utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.io.File;

import luo.library.R;

/**
 * 基本功能：图片加载工具
 */
public class ImageLoader {

    public static final String ANDROID_RESOURCE = "android.resource://";
    public static final String FOREWARD_SLASH = "/";

    private static class ImageLoaderHolder {
        private static final ImageLoader INSTANCE = new ImageLoader();

    }

    private ImageLoader() {
    }

    public static final ImageLoader getInstance() {
        return ImageLoaderHolder.INSTANCE;
    }


    //直接加载网络图片
    public void displayImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_empty)
                .error(R.drawable.ic_empty)
                .crossFade()
                .thumbnail(0.1f)
                .into(imageView);
    }

    //加载SD卡图片
    public void displayImage(Context context, File file, ImageView imageView) {
        Glide.with(context)
                .load(file)
                .centerCrop()
                .crossFade()
                .into(imageView);
    }

    //加载SD卡图片并设置大小
    public void displayImage(Context context, File file, ImageView imageView, int width, int height) {
        Glide.with(context)
                .load(file)
                .override(width, height)
                .centerCrop()
                .into(imageView);
    }

    //加载网络图片并设置大小
    public void displayImage(Context context, String url, ImageView imageView, int width, int height) {
        Glide.with(context)
                .load(url)
                .centerCrop()
                .thumbnail(0.1f)
                .override(width, height)
                .crossFade()
                .into(imageView);
    }

    //加载drawable图片
    public void displayImage(Context context, int resId, ImageView imageView) {
        Glide.with(context)
                .load(resourceIdToUri(context, resId))
                .crossFade()
                .into(imageView);
    }

    //加载drawable图片显示为圆形图片
    public void displayCricleImage(Context context, int resId, ImageView imageView) {
        Glide.with(context)
                .load(resourceIdToUri(context, resId))
                .crossFade()
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }

    //加载网络图片显示为圆形图片
    public void displayCricleImage(Context context, String url, ImageView imageView) {
        Glide.with(context)
                .load(url)
                //.centerCrop()//网友反馈，设置此属性可能不起作用,在有些设备上可能会不能显示为圆形。
                .thumbnail(0.1f)
                .transform(new GlideCircleTransform(context))
                .crossFade()
                .into(imageView);
    }

    //加载SD卡图片显示为圆形图片
    public void displayCricleImage(Context context, File file, ImageView imageView) {
        Glide.with(context)
                .load(file)
                //.centerCrop()
                .transform(new GlideCircleTransform(context))
                .into(imageView);
    }

    //将资源ID转为Uri
    public Uri resourceIdToUri(Context context, int resourceId) {
        return Uri.parse(ANDROID_RESOURCE + context.getPackageName() + FOREWARD_SLASH + resourceId);
    }

    //基本功能：Glide显示为圆形图片
    public class GlideCircleTransform extends BitmapTransformation {

        public GlideCircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }

        @Override
        public String getId() {
            return getClass().getName();
        }
    }

}

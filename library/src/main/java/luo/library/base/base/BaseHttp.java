package luo.library.base.base;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.LogUtil;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * xutils3 网络请求封装
 */

public class BaseHttp {

    private BaseHttp() {

    }

    public static void get(RequestParams params, BaseHttpCallback baseHttpCallback) {
        sendRequest(HttpMethod.GET, params, baseHttpCallback);
    }

    public static void get(Context context, RequestParams params, BaseHttpCallback baseHttpCallback) {
        sendRequest(context, HttpMethod.GET, params, baseHttpCallback);
    }

    public static void post(RequestParams params, BaseHttpCallback baseHttpCallback) {
        sendRequest(HttpMethod.POST, params, baseHttpCallback);
    }

    public static void post(Context context, RequestParams params, BaseHttpCallback baseHttpCallback) {
        sendRequest(context, HttpMethod.POST, params, baseHttpCallback);
    }

    public static void sendRequest(Context context, HttpMethod httpMethod, final RequestParams params, final BaseHttpCallback baseHttpCallback) {
        /** 判断https证书是否成功验证 */
        SSLContext sslContext = getSSLContext(context);
        if (null == sslContext) {
            Toast.makeText(context, "证书校验出错", Toast.LENGTH_LONG).show();
            return;
        }
        params.setSslSocketFactory(sslContext.getSocketFactory());
        sendRequest(httpMethod, params, baseHttpCallback);
    }

    public static void sendRequest(HttpMethod httpMethod, final RequestParams params, final BaseHttpCallback baseHttpCallback) {

        x.http().request(httpMethod, params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                String url[] = params.getUri().split("/");
                Log.i(url[url.length - 1], result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int code = jsonObject.getInt(BaseAndroid.getBaseConfig().getHttpCode());
                    if (code == BaseAndroid.getBaseConfig().getCode()) {
                        if (jsonObject.has(BaseAndroid.getBaseConfig().getHttpResult())) {
                            baseHttpCallback.onSuccess(jsonObject.getString(BaseAndroid.getBaseConfig().getHttpResult()));
                        } else {
                            baseHttpCallback.onSuccess("");
                        }
                    } else {
                        baseHttpCallback.onError(code, jsonObject.getString(BaseAndroid.getBaseConfig().getHttpMessage()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                baseHttpCallback.onError(-1, ex.getMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    public interface BaseHttpCallback {
        void onSuccess(String result);

        void onError(int code, String message);
    }

    /**
     * Https 证书验证对象
     */
    private static SSLContext s_sSLContext = null;

    /**
     * 获取Https的证书
     *
     * @param context Activity（fragment）的上下文
     * @return SSL的上下文对象
     */
    public static SSLContext getSSLContext(Context context) {
        CertificateFactory certificateFactory = null;
        InputStream inputStream = null;
        Certificate cer = null;
        KeyStore keystore = null;
        TrustManagerFactory trustManagerFactory = null;
        try {
            certificateFactory = CertificateFactory.getInstance("X.509");
            inputStream = context.getAssets().open("ssl.crt");//这里导入SSL证书文件
            try {
                //读取证书
                cer = certificateFactory.generateCertificate(inputStream);
                LogUtil.i(cer.getPublicKey().toString());
            } finally {
                inputStream.close();
            }
            //创建一个证书库，并将证书导入证书库
            keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(null, null); //双向验证时使用
            keystore.setCertificateEntry("trust", cer);

            // 实例化信任库
            trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            // 初始化信任库
            trustManagerFactory.init(keystore);

            s_sSLContext = SSLContext.getInstance("TLS");
            s_sSLContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

            //信任所有证书 （官方不推荐使用）
//         s_sSLContext.init(null, new TrustManager[]{new X509TrustManager() {
//
//              @Override
//              public X509Certificate[] getAcceptedIssuers() {
//                  return null;
//              }
//
//              @Override
//              public void checkServerTrusted(X509Certificate[] arg0, String arg1)
//                      throws CertificateException {
//
//              }
//
//              @Override
//              public void checkClientTrusted(X509Certificate[] arg0, String arg1)
//                      throws CertificateException {
//
//              }
//          }}, new SecureRandom());
            return s_sSLContext;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

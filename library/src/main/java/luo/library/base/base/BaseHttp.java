package luo.library.base.base;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.HttpMethod;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.InputStream;
import java.security.KeyStore;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
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
        params.setSslSocketFactory(getSSLCertifcation(context));
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


    private final static String CLIENT_PRI_KEY = "client.bks";
    private final static String TRUSTSTORE_PUB_KEY = "truststore.bks";
    private final static String CLIENT_BKS_PASSWORD = "123456";
    private final static String TRUSTSTORE_BKS_PASSWORD = "123456";
    private final static String KEYSTORE_TYPE = "BKS";
    private final static String PROTOCOL_TYPE = "TLS";
    private final static String CERTIFICATE_FORMAT = "X509";

    public static SSLSocketFactory getSSLCertifcation(Context context) {
        SSLSocketFactory sslSocketFactory = null;
        try {
            // 服务器端需要验证的客户端证书，其实就是客户端的keystore
            KeyStore keyStore = KeyStore.getInstance(KEYSTORE_TYPE);// 客户端信任的服务器端证书
            KeyStore trustStore = KeyStore.getInstance(KEYSTORE_TYPE);//读取证书
            InputStream ksIn = context.getAssets().open(CLIENT_PRI_KEY);
            InputStream tsIn = context.getAssets().open(TRUSTSTORE_PUB_KEY);//加载证书
            keyStore.load(ksIn, CLIENT_BKS_PASSWORD.toCharArray());
            trustStore.load(tsIn, TRUSTSTORE_BKS_PASSWORD.toCharArray());
            ksIn.close();
            tsIn.close();
            //初始化SSLContext
            SSLContext sslContext = SSLContext.getInstance(PROTOCOL_TYPE);
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(CERTIFICATE_FORMAT);
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(CERTIFICATE_FORMAT);
            trustManagerFactory.init(trustStore);
            keyManagerFactory.init(keyStore, CLIENT_BKS_PASSWORD.toCharArray());
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            sslSocketFactory = sslContext.getSocketFactory();
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return sslSocketFactory;
    }
}

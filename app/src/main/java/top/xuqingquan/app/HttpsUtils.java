package top.xuqingquan.app;

import android.app.Application;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * @author : 许清泉 xuqingquan1995@gmail.com
 * @since : 2021-10-15
 */
public class HttpsUtils {
    private static final String KEY_STORE_TYPE_BKS = "bks";
    private static final String KEY_STORE_TYPE_P12 = "PKCS12";
    public static final String KEY_STORE_PASSWORD = "xxx";//P12文件密码
    public static final String BKS_STORE_PASSWORD = "xxx";//BKS文件密码
    public static SSLSocketFactory sSLSocketFactory;
    public static X509TrustManager trustManager;

    /**
     * 双向校验中SSLSocketFactory X509TrustManager 参数的生成
     *
     * @param application
     */
    public static void initSslSocketFactory(Application application) {
        try {
            //客户端信任的服务器端证书流
            InputStream bksStream = application.getAssets().open("server.bks");
            //服务器需要验证的客户端证书流
            InputStream p12Stream = application.getAssets().open("client.p12");

            // 客户端信任的服务器端证书
            KeyStore trustStore = KeyStore.getInstance(KEY_STORE_TYPE_BKS);
            // 服务器端需要验证的客户端证书
            KeyStore keyStore = KeyStore.getInstance(KEY_STORE_TYPE_P12);

            try {
                //加载客户端信任的服务器证书
                trustStore.load(bksStream, BKS_STORE_PASSWORD.toCharArray());
                //加载服务器信任的客户端证书
                keyStore.load(p12Stream, KEY_STORE_PASSWORD.toCharArray());
            } catch (CertificateException | NoSuchAlgorithmException | IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    bksStream.close();
                    p12Stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);
            //生成用来校验服务器真实性的trustManager
            trustManager = chooseTrustManager(trustManagerFactory.getTrustManagers());


            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
            //生成服务器用来校验客户端真实性的KeyManager
            keyManagerFactory.init(keyStore, KEY_STORE_PASSWORD.toCharArray());
            //初始化SSLContext
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);
            //通过sslContext获取到SocketFactory
            sSLSocketFactory = sslContext.getSocketFactory();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static X509TrustManager chooseTrustManager(TrustManager[] trustManagers) {
        for (TrustManager trustManager : trustManagers) {
            if (trustManager instanceof X509TrustManager) {
                return (X509TrustManager) trustManager;
            }
        }
        return null;
    }
}


package org.xujin.moss.client.endpoint.dependency;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class MySSLSocketFactory {
    private static SSLSocketFactory VI_SSL_FACTORY = null;

    public static SSLSocketFactory getSSLSocketFactory()
        throws KeyManagementException, NoSuchProviderException, NoSuchAlgorithmException {

        if (VI_SSL_FACTORY == null) {
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new SecureRandom());
            VI_SSL_FACTORY = sslContext.getSocketFactory();
        }
        return VI_SSL_FACTORY;
    }
}

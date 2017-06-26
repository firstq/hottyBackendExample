package su.hotty.editor.util;

import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 *
 */
public class SslBeliever {

    private static final Logger log = LoggerFactory.getLogger(SslBeliever.class.getSimpleName());

    private SslBeliever() {}

    public static synchronized void trustAllCertificates(OkHttpClient.Builder clientBuilder) throws Exception {
        log.warn("Allow untrusted SSL connections.");

        X509TrustManager x509TrustManager = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                X509Certificate[] cArrr = new X509Certificate[0];
                return cArrr;
            }

            @Override
            public void checkServerTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(final X509Certificate[] chain,
                                           final String authType) throws CertificateException {
            }
        };

        TrustManager[] trustAllCerts = new TrustManager[]{x509TrustManager};

        SSLContext sslContext;

        try {
            sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            final String m = "Can't create SSL context.";
            log.error(m, e);
            throw new Exception(m, e);
        }

        clientBuilder.sslSocketFactory(sslContext.getSocketFactory(), x509TrustManager);

        HostnameVerifier hostnameVerifier = (hostname, session) -> {
            log.debug("Trust host: {}", hostname);
            return true;
        };

        clientBuilder.hostnameVerifier(hostnameVerifier);
    }

}

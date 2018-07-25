package com.global.test.testglobal.Services;

import android.content.Context;

import com.global.test.testglobal.R;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Arrays;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

/**
 * Created by Andrea on 25/07/2018.
 */

public class ClienteRetrofit extends OkHttpClient {

    public ClienteRetrofit() {
    }

    /**
     * Método que retorna el cliente OkHttp con el certificado de seguridad y host validado
     */
    public static OkHttpClient getSSLConfig(Context contex) {
        try {
            // Load CAs from an InputStream
            // (could be from a resource or ByteArrayInputStream or ...)

            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            //Lee el certificado de seguridad incluido en la carpeta de recursos /res/raw/my_cert22.cer

            InputStream caInput = contex.getResources().openRawResource(R.raw.my_cert);

            Certificate ca;
            try {
                ca = cf.generateCertificate(caInput);
                System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
            } finally {
                caInput.close();
            }

// Create a KeyStore containing our trusted CAs
            String keyStoreType = KeyStore.getDefaultType();
            KeyStore keyStore = KeyStore.getInstance(keyStoreType);
            keyStore.load(null, null);
            keyStore.setCertificateEntry("ca", ca);

// Create a TrustManager that trusts the CAs in our KeyStore
            String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
            tmf.init(keyStore);

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();
            if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
                throw new IllegalStateException("Unexpected default trust managers:" + Arrays.toString(trustManagers));
            }
            X509TrustManager trustManager = (X509TrustManager) trustManagers[0];
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{trustManager}, null);


            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();//protocols(Collections.singletonList(Protocol.HTTP_1_1))
            // Usa un Cliente okHttp para aceptar y verificar el host del certificado de seguridad
            OkHttpClient client = new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory, trustManager).hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            }).build();



            return client;

        } catch (CertificateException c) {
            System.out.println("Mensaje Error:" + c.getMessage());
        } catch (IOException io) {
            System.out.println("Mensaje Error:" + io.getMessage());
        } catch (KeyStoreException ks) {
            System.out.println("Mensaje Error:" + ks.getMessage());
        } catch (NoSuchAlgorithmException ns) {
            System.out.println("Mensaje Error:" + ns.getMessage());
        } catch (KeyManagementException km) {
            System.out.println("Mensaje Error:" + km.getMessage());
        }

        return null;
    }

}

package com.robocubs4205.cubscout.net;

import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.circle.android.api.OkHttp3Stack;
import com.robocubs4205.cubscout.R;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.Arrays;
import java.util.Collection;

import javax.inject.Singleton;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import butterknife.BindString;
import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

/**
 * Created by trevor on 12/31/16.
 */

@Module
public class NetModule {

    private final Context mContext;

    @BindString(R.string.cubscout_server_fqdn)
    String fqdn;

    public NetModule(Context context) {
        mContext = context;
    }

    @Provides
    Context provideContext() {
        return mContext;
    }

    @Provides
    @Singleton
    OkHttpClient provideOkHttpClient() {
        X509TrustManager trustManager;
        SSLSocketFactory sslSocketFactory;
        try {
            trustManager = trustManagerForCertificates(trustedCertificatesInputStream());
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            sslSocketFactory = sslContext.getSocketFactory();
        }
        catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException(
                    "exception while constructing OkhttpClient for Dagger Injection", e);
        }
        OkHttpClient httpClient = new OkHttpClient.Builder().sslSocketFactory(sslSocketFactory,
                                                                              trustManager).build();
        return httpClient;
    }

    private X509TrustManager trustManagerForCertificates(InputStream in)
            throws GeneralSecurityException, IOException {
        CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
        Collection<? extends Certificate> certificates = certificateFactory.generateCertificates(
                in);
        if (certificates.isEmpty()) {
            throw new IllegalArgumentException("expected non-empty set of trusted certificates");
        }

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);

        int index = 0;
        for (Certificate certificate : certificates) {
            String certificateAlias = Integer.toString(index++);
            keyStore.setCertificateEntry(certificateAlias, certificate);
        }

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
                KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, null);

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(
                TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        TrustManager[] trustManagers = trustManagerFactory.getTrustManagers();

        if (trustManagers.length != 1 || !(trustManagers[0] instanceof X509TrustManager)) {
            throw new IllegalStateException("Unexpected default trust managers:"
                                                    + Arrays.toString(trustManagers));
        }

        return (X509TrustManager) trustManagers[0];
    }

    private InputStream trustedCertificatesInputStream() {
        return mContext.getResources().openRawResource(R.raw.data_robocubs4205_com);
    }

    @Provides
    @Singleton
    RequestQueue provideVoleyRequestQueue(OkHttp3Stack stack) {
        return Volley.newRequestQueue(mContext, stack);
    }

    @Provides
    CubscoutAPI provideCubscoutApi(StagingCubscoutApi stagingCubscoutApi){
        return stagingCubscoutApi;
    }
}

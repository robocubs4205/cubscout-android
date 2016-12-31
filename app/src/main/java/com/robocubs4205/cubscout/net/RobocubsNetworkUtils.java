package com.robocubs4205.cubscout.net;

import android.content.Context;
import android.util.Log;

import com.robocubs4205.cubscout.R;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by trevor on 12/27/16.
 */

public class RobocubsNetworkUtils {
    public static SSLSocketFactory getRobocubsSSLSocketFactoryInstance(Context applicationContext) throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException {
        InputStream certStream = applicationContext.getResources().openRawResource(R.raw.data_robocubs4205_com);
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        Certificate cert;
        try {
            cert = cf.generateCertificate(certStream);
            System.out.println("ca=" + ((X509Certificate) cert).getSubjectDN());
        } finally {
            try
            {
                certStream.close();
            }
            catch (IOException e)
            {
                Log.e("UploadScorecard","Unable to close InputStream for server certificate");
            }

        }

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);
        keyStore.setCertificateEntry("cubscout",cert);

        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null,tmf.getTrustManagers(),null);

        return sslContext.getSocketFactory();
    }

    public static HttpsURLConnection SendPostData(URL url, String outputData, Context applicationContext) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        boolean redirectPost = false;
        boolean redirectGet = false;
        HttpsURLConnection connection;
        Log.d("SendPostData","sending via POST");
        do {
            Log.d("SendPostData","trying url "+url.toString());
            redirectPost = false;

            connection = (HttpsURLConnection) url.openConnection();

            connection.setSSLSocketFactory(getRobocubsSSLSocketFactoryInstance(applicationContext));

            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            connection.setInstanceFollowRedirects(false);

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Cache-Control", "no-cache");
            connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            connection.setRequestProperty("Accept", "application/json");

            OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
            writer.write(outputData);
            writer.flush();
            writer.close();

            int httpResult = connection.getResponseCode();
            Log.d("SendPostData","response code: "+httpResult);
            if(   httpResult == HttpURLConnection.HTTP_MOVED_PERM
                    || httpResult == HttpURLConnection.HTTP_MOVED_TEMP)
            {
                redirectPost = true;
                url = new URL(connection.getHeaderField("Location"));
            }
            else if(httpResult == HttpURLConnection.HTTP_SEE_OTHER)
            {
                redirectGet = true;
                url = new URL(connection.getHeaderField("Location"));
            }
        } while (redirectPost);
        if(redirectGet)
        {
            connection = RobocubsNetworkUtils.SendGetRequest(url,applicationContext);
        }
        return connection;
    }

    public static HttpsURLConnection SendGetRequest (URL url, Context applicationContext) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        Log.d("SendPostData","sending via POST");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

        connection.setSSLSocketFactory(getRobocubsSSLSocketFactoryInstance(applicationContext));

        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        connection.setInstanceFollowRedirects(true);

        connection.setRequestMethod("GET");
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setRequestProperty("Cache-Control", "no-cache");
        connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        connection.setRequestProperty("Accept", "application/json");
        int httpResult = connection.getResponseCode();
        Log.d("SendPostData","response code: "+httpResult);
        return connection;
    }

    /*public static JSONObject searchCubscout(String query){

    }*/

}

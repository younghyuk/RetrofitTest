package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import domain.Change;
import okhttp3.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import webservice.GerritService;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * Created by yyhyo on 2017-05-28.
 */
public class Controller implements Callback<List<Change>> {

    static final String BASE_URL = "https://git.eclipse.org/r/";

    public void start() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        GerritService gerritService = retrofit.create(GerritService.class);
        Call<List<Change>> call = gerritService.loadChange("status:open");
        call.enqueue(this);
    }

    private OkHttpClient createOkHttpClient() {

        // Proxy

        int proxyPort = 8080;
        String proxyHost = "127.0.0.1";
        final String username = "username";
        final String password = "password";

        Authenticator proxyAuthenticator = new Authenticator() {
            @Override
            public Request authenticate(Route route, okhttp3.Response response) throws IOException {
                String credential = Credentials.basic(username, password);
                return response.request().newBuilder()
                        .header("Proxy-Authorization", credential)
                        .build();
            }
        };

        Proxy proxyTest = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));

        // SSL. 모든 인증서 통과하는 셋팅. 다시 말하면 안전하지 않음.
        final TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {

                    @Override
                    public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        // Do Nothing
                    }

                    @Override
                    public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {
                        // Do Nothing
                    }

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[]{};
                    }
                }
        };

        SSLSocketFactory sslSocketFactory = null;
        try {
            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .proxy(proxyTest)
                .proxyAuthenticator(proxyAuthenticator);

        if (sslSocketFactory != null) {
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String s, SSLSession sslSession) {
                    return true;
                }
            });
        }

        return builder.build();
    }

    @Override
    public void onResponse(Call<List<Change>> call, Response<List<Change>> response) {
        if (response.isSuccessful()) {
            List<Change> changesList = response.body();
            if (changesList != null) {
                changesList.forEach(change -> System.out.println(change.getSubject()));
            }
        } else {
            System.out.println(response.errorBody());
        }
    }

    @Override
    public void onFailure(Call<List<Change>> call, Throwable t) {
        t.printStackTrace();
    }
}

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

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
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

        return new OkHttpClient.Builder()
                .proxy(proxyTest)
                .proxyAuthenticator(proxyAuthenticator)
                .build();
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

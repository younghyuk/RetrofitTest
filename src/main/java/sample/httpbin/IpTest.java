package sample.httpbin;

import okhttp3.ResponseBody;
import retrofit.webservice.HttpBinService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import sample.httpbin.domain.Ip;

import java.io.IOException;

import static sample.httpbin.HttpBin.BASE_URL;

/**
 * Created by yyhyo on 2017-05-29.
 */
public class IpTest {
    public static void main(String[] args) {

        ipToRawData();
        ipToJsonData();

    }

    private static void ipToRawData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .build();

        HttpBinService httpBinService = retrofit.create(HttpBinService.class);
        Call<ResponseBody> call = httpBinService.ipToRaw();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                System.out.println(response);
                try {
                    System.out.println(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private static void ipToJsonData() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HttpBinService httpBinService = retrofit.create(HttpBinService.class);
        Call<Ip> call = httpBinService.ipToJson();
        call.enqueue(new Callback<Ip>() {
            @Override
            public void onResponse(Call<Ip> call, Response<Ip> response) {
                System.out.println(response);
                Ip body = response.body();
                System.out.println(body);
            }

            @Override
            public void onFailure(Call<Ip> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}

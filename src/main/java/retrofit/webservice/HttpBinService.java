package retrofit.webservice;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.*;
import sample.httpbin.domain.Ip;

/**
 * 인터넷 테스트용 서비스.
 * http://httpbin.org/
 * <p>
 * Created by yyhyo on 2017-05-29.
 */
public interface HttpBinService {
    @GET("/")
    Call<ResponseBody> indexToRow();

    @GET("/ip")
    Call<ResponseBody> ipToRaw();

    @GET("/ip")
    Call<Ip> ipToJson();

    @GET("/")
    Call<ResponseBody> getToRaw();

    @POST("/")
    Call<ResponseBody> postToRaw();

    @PATCH("/")
    Call<ResponseBody> patchToRaw();

    @PUT("/")
    Call<ResponseBody> putToRaw();

    @DELETE("/")
    Call<ResponseBody> deleteToRaw();

    @GET("/encoding/utf8")
    Call<ResponseBody> encodingUtf8ToRow();

    @GET("/gzip")
    Call<ResponseBody> gzipToRaw();

    @GET("/deflate")
    Call<ResponseBody> deflateToRaw();
}

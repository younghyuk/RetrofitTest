package webservice;

import domain.Change;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

/**
 * GerritService
 * Created by yyhyo on 2017-05-28.
 */
public interface GerritService {

    @GET("changes/")
    Call<List<Change>> loadChange(@Query("q") String status);
}

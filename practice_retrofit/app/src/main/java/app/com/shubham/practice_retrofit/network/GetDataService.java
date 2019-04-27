package app.com.shubham.practice_retrofit.network;

import app.com.shubham.practice_retrofit.model.Example;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetDataService {

    @GET("/services/rest/?")
    Call<Example> getAllPhotos(
            @Query("method") String method,
            @Query("per_page") int per_page,
            @Query("page") int page,
            @Query("api_key") String api_key,
            @Query("format") String format,
            @Query("nojsoncallback") int nojsoncallback,
            @Query("extras") String extras
    );
}
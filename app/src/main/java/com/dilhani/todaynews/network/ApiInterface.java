package com.dilhani.todaynews.network;

import com.dilhani.todaynews.models.*;

import io.reactivex.rxjava3.core.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    //Old Endpoint.
    @GET("articles")
    Call<ArticleResponse> getCall(@Query("source") String source,
                                  @Query("sortBy") String sortBy,
                                  @Query("apiKey") String apiKey);

    //New Endpoint to fetch headlines.
    @GET("top-headlines")
    Call<NewsResponse> getHeadlines(@Query("sources") String sources,
                                    @Query("apiKey") String apiKey);

    //New Endpoint to fetch search results.
    @GET("everything")
    Single<NewsResponse> getSearchResults(@Query("q") String query,
                                          @Query("sortBy") String sortBy,
                                          @Query("category") String category,
                                          @Query("language") String language,
                                          @Query("apiKey") String apiKey);
}

package com.dilhani.todaynews.network;

import com.dilhani.todaynews.models.NewsResponse;
import com.dilhani.todaynews.models.SourceResponse;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top-headlines/sources")
    Single<SourceResponse> getSources(@Query("apiKey") String apiKey);

    @GET("top-headlines")
    Single<NewsResponse> getNewsByCategory(@Query("category") String category,
                                           @Query("country") String country,
                                           @Query("apiKey") String apiKey);

    @GET("everything")
    Single<NewsResponse> getSearchResults(@Query("q") String query,
                                          @Query("sortBy") String sortBy,
                                          @Query("pageSize") String pageSize,
                                          @Query("language") String language,
                                          @Query("apiKey") String apiKey);
}

package com.dilhani.todaynews.repository;

import android.content.Context;
import com.dilhani.todaynews.models.NewsResponse;
import com.dilhani.todaynews.network.ApiClient;
import com.dilhani.todaynews.network.ApiInterface;
import com.dilhani.todaynews.utils.ApplicationConstants;

import io.reactivex.rxjava3.core.Single;

public class AllNewsRepository {

    private ApiInterface apiInterface;
    private Context context;

    public AllNewsRepository(Context context) {
        apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
        this.context = context;
    }

    public Single<NewsResponse> getAllNews(){
        return apiInterface.getSearchResults(null, "popularity", "entertainment",null, ApplicationConstants.NEWS_API_KEY);
    }
}

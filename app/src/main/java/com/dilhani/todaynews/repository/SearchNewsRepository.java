package com.dilhani.todaynews.repository;

import android.content.Context;

import com.dilhani.todaynews.models.NewsResponse;
import com.dilhani.todaynews.network.ApiClient;
import com.dilhani.todaynews.network.ApiInterface;
import com.dilhani.todaynews.utils.ApplicationConstants;

import io.reactivex.rxjava3.core.Single;

public class SearchNewsRepository {

    private ApiInterface apiInterface;

    public SearchNewsRepository(Context context) {
        apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
    }

    public Single<NewsResponse> searchNews(String query, String language) {
        return apiInterface.getSearchResults(query, "relevancy", "100", language, ApplicationConstants.NEWS_API_KEY);
    }
}

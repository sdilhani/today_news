package com.dilhani.todaynews.repository;

import android.content.Context;

import com.dilhani.todaynews.models.SourceResponse;
import com.dilhani.todaynews.network.ApiClient;
import com.dilhani.todaynews.network.ApiInterface;
import com.dilhani.todaynews.utils.ApplicationConstants;

import io.reactivex.rxjava3.core.Single;

public class SourcesRepository {

    private final ApiInterface apiInterface;

    public SourcesRepository(Context context) {
        apiInterface = ApiClient.getClient(context).create(ApiInterface.class);
    }

    public Single<SourceResponse> getResources(){
        return apiInterface.getSources(ApplicationConstants.NEWS_API_KEY);
    }
}

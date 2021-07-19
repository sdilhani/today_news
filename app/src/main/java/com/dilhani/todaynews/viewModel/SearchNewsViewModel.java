package com.dilhani.todaynews.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.dilhani.todaynews.models.Article;
import com.dilhani.todaynews.models.NewsResponse;
import com.dilhani.todaynews.repository.SearchNewsRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SearchNewsViewModel extends AndroidViewModel {

    public NewsResponse newsResponse;
    public List<Article> articlesList = new ArrayList<Article>();
    public CompositeDisposable compositeDisposable;
    private SearchNewsRepository searchRepository;

    public SearchNewsViewModel(@NonNull Application application) {
        super(application);
        searchRepository = new SearchNewsRepository(application.getApplicationContext());
        compositeDisposable = new CompositeDisposable();
    }

    public Completable searchNews(String keyword, String language) {
        return Completable.create(emitter -> {
            compositeDisposable.add(searchRepository.searchNews(keyword, language).subscribeOn(Schedulers.io()).subscribe(
                    news -> {
                        this.newsResponse = news;
                        articlesList.clear();
                        articlesList.addAll(news.getArticles());
                        emitter.onComplete();
                    }, error -> {
                        error.printStackTrace();
                        emitter.onError(error);
                    })
            );
        });
    }

    public void disposeAll() {
        compositeDisposable.dispose();
    }
}

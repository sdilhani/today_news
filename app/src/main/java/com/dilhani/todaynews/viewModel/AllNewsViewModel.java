package com.dilhani.todaynews.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.dilhani.todaynews.models.NewsResponse;
import com.dilhani.todaynews.repository.AllNewsRepository;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class AllNewsViewModel extends AndroidViewModel {

    private AllNewsRepository allNewsRepository;
    public NewsResponse newsResponse;
    public CompositeDisposable compositeDisposable;

    public AllNewsViewModel(@NonNull Application application) {
        super(application);
        allNewsRepository = new AllNewsRepository(application.getApplicationContext());
        compositeDisposable = new CompositeDisposable();
    }

    public Completable getAllNews() {
        return Completable.create(emitter -> {
            compositeDisposable.add(allNewsRepository.getAllNews().subscribeOn(Schedulers.io()).subscribe(
                    news -> {
                        this.newsResponse = news;
                        emitter.onComplete();
                    }, error -> {
                        error.printStackTrace();
                        emitter.onError(error);
                    })
            );
        });
    }

    public void disposeAll(){
        compositeDisposable.dispose();
    }
}

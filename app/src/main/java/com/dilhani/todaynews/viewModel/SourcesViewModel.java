package com.dilhani.todaynews.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.dilhani.todaynews.models.Source;
import com.dilhani.todaynews.repository.SourcesRepository;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SourcesViewModel extends AndroidViewModel {

    private final SourcesRepository sourcesRepository;
    public List<Source> sourceList = new ArrayList<Source>();
    public CompositeDisposable compositeDisposable;

    public SourcesViewModel(@NonNull Application application) {
        super(application);
        sourcesRepository = new SourcesRepository(application.getApplicationContext());
        compositeDisposable = new CompositeDisposable();
    }

    public Completable getSources() {
        return Completable.create(emitter -> {
            compositeDisposable.add(sourcesRepository.getResources().subscribeOn(Schedulers.io()).subscribe(
                    sources -> {
                        sourceList.clear();
                        sourceList.addAll(sources.getSources());
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

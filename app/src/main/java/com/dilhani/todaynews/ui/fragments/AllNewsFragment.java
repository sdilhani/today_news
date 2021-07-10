package com.dilhani.todaynews.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.dilhani.todaynews.R;
import com.dilhani.todaynews.databinding.FragmentAllNewsBinding;
import com.dilhani.todaynews.viewModel.AllNewsViewModel;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class AllNewsFragment extends BaseFragment {

    private CompositeDisposable compositeDisposable;
    private AllNewsViewModel allNewsViewModel;
    private FragmentAllNewsBinding binding;

    public AllNewsFragment() {
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allNewsViewModel = new ViewModelProvider(this).get(AllNewsViewModel.class);
        getAllNews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
      //  return inflater.inflate(R.layout.fragment_all_news, container, false);
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_all_news,container, false);
        setupListeners();
        return  binding.getRoot();
    }

    private void setupListeners(){
        binding.btnGeneral.setSelected(false);
        binding.btnGeneral.setOnClickListener(view -> {
            Log.e(TAG, "setupListeners: clicked general");
            binding.btnGeneral.setSelected(!binding.btnGeneral.isSelected());
        });
    }

    private void getAllNews() {
        showLoader("Loading News...");
        compositeDisposable.add(allNewsViewModel.getAllNews().subscribeOn(Schedulers.io()).subscribe(() -> {
            getActivity().runOnUiThread(this::hideLoader);
            Log.e(TAG, "getAllNews: " + allNewsViewModel.newsResponse.getTotalResults());
        }, error -> {
            error.printStackTrace();
            getActivity().runOnUiThread( () -> {
                showLongToastMessage(error.getMessage());
            });
            getActivity().runOnUiThread(this::hideLoader);
        }));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        allNewsViewModel.disposeAll();
        compositeDisposable.dispose();
    }
}
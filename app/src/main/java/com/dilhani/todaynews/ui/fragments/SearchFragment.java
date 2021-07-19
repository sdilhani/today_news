package com.dilhani.todaynews.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.dilhani.todaynews.R;
import com.dilhani.todaynews.adapter.NewsAdapter;
import com.dilhani.todaynews.databinding.FragmentSearchBinding;
import com.dilhani.todaynews.ui.NewsSingle;
import com.dilhani.todaynews.utils.ApplicationConstants;
import com.dilhani.todaynews.viewModel.SearchNewsViewModel;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class SearchFragment extends BaseFragment {

    private FragmentSearchBinding binding;
    private NewsAdapter newsAdapter;
    private ProgressDialog progressDialog;
    private CompositeDisposable compositeDisposable;
    private SearchNewsViewModel searchNewsViewModel;
    private String language;

    public SearchFragment() { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_search,container,false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        compositeDisposable = new CompositeDisposable();
        searchNewsViewModel = new ViewModelProvider(this).get(SearchNewsViewModel.class);

        language = getSharedPreferences().getString(ApplicationConstants.LANGUAGE,"en");

        newsAdapter = new NewsAdapter(searchNewsViewModel.articlesList, article -> {
            Intent intent = new Intent(requireContext(), NewsSingle.class);
            intent.putExtra("article", article);
            startActivity(intent);
        });

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(),2);
        binding.newsRecycler.setLayoutManager(layoutManager);
        binding.newsRecycler.setAdapter(newsAdapter);

        binding.searchBtn.setOnClickListener(view -> {
            search();
        });

        super.onStart();
    }

    private void search(){
        if(binding.query.getText().toString().equals("")){
            showToastMessage("Enter a search keyword");
            return;
        }
        showLoader("Loading News...");
        language = getSharedPreferences().getString(ApplicationConstants.LANGUAGE,"en");
        compositeDisposable.add(searchNewsViewModel.searchNews(binding.query.getText().toString(), language).subscribeOn(Schedulers.io()).subscribe(() -> {
            requireActivity().runOnUiThread(() -> {
                newsAdapter.notifyDataSetChanged();
                hideLoader();

                if (searchNewsViewModel.articlesList.size() < 1){
                    showToastMessage("No results found!");
                }
            });
            Log.e(TAG, "getAllNews: " + searchNewsViewModel.newsResponse.getTotalResults());
        }, error -> {
            error.printStackTrace();
            requireActivity().runOnUiThread( () -> {
                showLongToastMessage(error.getMessage());
            });
            requireActivity().runOnUiThread(this::hideLoader);
        }));
    }

    public void showLoader(String message) {
        try {
            progressDialog = new ProgressDialog(this.requireContext());
            progressDialog.setMessage(message);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
            progressDialog.show();
        } catch (
                Exception ex) {
            ex.printStackTrace();
        }
    }

    public void hideLoader(){
        try {
            progressDialog.dismiss();
        }catch (Exception exception){
            exception.printStackTrace();
        }
    }
}
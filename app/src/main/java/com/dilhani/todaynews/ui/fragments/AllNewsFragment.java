package com.dilhani.todaynews.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.dilhani.todaynews.R;
import com.dilhani.todaynews.adapter.NewsAdapter;
import com.dilhani.todaynews.databinding.FragmentAllNewsBinding;
import com.dilhani.todaynews.ui.NewsSingle;
import com.dilhani.todaynews.utils.ApplicationConstants;
import com.dilhani.todaynews.viewModel.AllNewsViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

public class AllNewsFragment extends BaseFragment {

    private final ArrayList<Button> categoryButtons = new ArrayList<>();
    private CompositeDisposable compositeDisposable;
    private AllNewsViewModel allNewsViewModel;
    private FragmentAllNewsBinding binding;
    private String category = ApplicationConstants.NEWS_CATEGORY_GENERAL;
    private NewsAdapter newsAdapter;
    private ProgressDialog progressDialog;
    private String country;

    public AllNewsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_all_news, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        compositeDisposable = new CompositeDisposable();
        allNewsViewModel = new ViewModelProvider(this).get(AllNewsViewModel.class);

        country = getSharedPreferences().getString(ApplicationConstants.COUNTRY, "us");
        setupListeners();

        newsAdapter = new NewsAdapter(allNewsViewModel.articlesList, article -> {
            Intent intent = new Intent(requireContext(), NewsSingle.class);
            intent.putExtra("article", article);
            startActivity(intent);
        });

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        binding.newsRecycler.setLayoutManager(layoutManager);
        binding.newsRecycler.setAdapter(newsAdapter);
        //getAllNewsByCategory();

        setButtonState(categoryButtons.get(0));

        super.onStart();
    }

    private void setupListeners() {

        categoryButtons.add(binding.btnGeneral);
        categoryButtons.add(binding.btnBusiness);
        categoryButtons.add(binding.btnEnt);
        categoryButtons.add(binding.btnHealth);
        categoryButtons.add(binding.btnScience);
        categoryButtons.add(binding.btnSports);
        categoryButtons.add(binding.btnTech);

        binding.btnGeneral.setOnClickListener(view -> {
            category = ApplicationConstants.NEWS_CATEGORY_GENERAL;
            setButtonState((Button) view);
        });

        binding.btnTech.setOnClickListener(view -> {
            category = ApplicationConstants.NEWS_CATEGORY_TECHNO;
            setButtonState((Button) view);
        });

        binding.btnSports.setOnClickListener(view -> {
            category = ApplicationConstants.NEWS_CATEGORY_SPORTS;
            setButtonState((Button) view);
        });

        binding.btnScience.setOnClickListener(view -> {
            category = ApplicationConstants.NEWS_CATEGORY_SCIENCE;
            setButtonState((Button) view);
        });

        binding.btnHealth.setOnClickListener(view -> {
            category = ApplicationConstants.NEWS_CATEGORY_HEALTH;
            setButtonState((Button) view);
        });

        binding.btnBusiness.setOnClickListener(view -> {
            category = ApplicationConstants.NEWS_CATEGORY_BUSINESS;
            setButtonState((Button) view);
        });

        binding.btnEnt.setOnClickListener(view -> {
            category = ApplicationConstants.NEWS_CATEGORY_ENTERTAINMENT;
            setButtonState((Button) view);
        });
    }

    private void setButtonState(Button button) {
        for (Button categoryButton : categoryButtons) {
            categoryButton.setSelected(button == categoryButton);
        }
        getAllNewsByCategory();
    }

    private void getAllNewsByCategory() {
        showLoader("Loading News...");
        compositeDisposable.add(allNewsViewModel.getAllNews(this.category, country).subscribeOn(Schedulers.io()).subscribe(() -> {
            requireActivity().runOnUiThread(() -> {
                newsAdapter.notifyDataSetChanged();
                hideLoader();
            });
            Log.e(TAG, "getAllNews: " + allNewsViewModel.newsResponse.getTotalResults());
        }, error -> {
            error.printStackTrace();
            requireActivity().runOnUiThread(() -> {
                showLongToastMessage(error.getMessage());
            });
            requireActivity().runOnUiThread(this::hideLoader);
        }));
    }

    @Override
    public void onDestroy() {
        progressDialog.dismiss();
        allNewsViewModel.disposeAll();
        compositeDisposable.dispose();
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        progressDialog.dismiss();
        allNewsViewModel.disposeAll();
        compositeDisposable.dispose();
        super.onDetach();
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

    public void hideLoader() {
        try {
            progressDialog.dismiss();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
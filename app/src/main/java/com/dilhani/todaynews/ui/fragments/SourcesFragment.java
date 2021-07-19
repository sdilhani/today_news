package com.dilhani.todaynews.ui.fragments;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dilhani.todaynews.R;
import com.dilhani.todaynews.adapter.SourceAdapter;
import com.dilhani.todaynews.databinding.FragmentSourcesBinding;
import com.dilhani.todaynews.viewModel.SourcesViewModel;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class SourcesFragment extends Fragment {

    private SourceAdapter sourceAdapter;
    private ProgressDialog progressDialog;
    private CompositeDisposable compositeDisposable;
    private SourcesViewModel sourcesViewModel;
    private FragmentSourcesBinding binding;

    public SourcesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sources, container, false);
        return binding.getRoot();
    }

    @Override
    public void onStart() {

        compositeDisposable = new CompositeDisposable();
        sourcesViewModel = new ViewModelProvider(this).get(SourcesViewModel.class);

        sourceAdapter = new SourceAdapter(sourcesViewModel.sourceList, source -> {
            String url = source.getUrl();
            try {
                Intent i = new Intent("android.intent.action.MAIN");
                i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                i.addCategory("android.intent.category.LAUNCHER");
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (ActivityNotFoundException e) {
                // Chrome is not installed
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        binding.sourcesRecycle.setLayoutManager(layoutManager);
        binding.sourcesRecycle.setAdapter(sourceAdapter);

        getSources();

        super.onStart();
    }

    private void getSources() {
        showLoader("Loading Sources...");
        compositeDisposable.add(sourcesViewModel.getSources().subscribeOn(Schedulers.io()).subscribe(() -> {
            requireActivity().runOnUiThread(() -> {
                sourceAdapter.notifyDataSetChanged();
                hideLoader();
            });
        }, error -> {
            error.printStackTrace();
            requireActivity().runOnUiThread(() -> {
                // showLongToastMessage(error.getMessage());
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

    public void hideLoader() {
        try {
            progressDialog.dismiss();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
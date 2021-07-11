package com.dilhani.todaynews.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dilhani.todaynews.databinding.AdapterSourcesBinding;
import com.dilhani.todaynews.models.Source;

import java.util.List;

public class SourceAdapter extends RecyclerView.Adapter<SourceAdapter.SourceAdapterViewHolder> {

    private final OnSourceClick onNewsClickListener;
    private List<Source> sources;

    public SourceAdapter(List<Source> sources, OnSourceClick onNewsClickListener) {
        this.sources = sources;
        this.onNewsClickListener = onNewsClickListener;
    }

    @Override
    public SourceAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        AdapterSourcesBinding binding = AdapterSourcesBinding.inflate(layoutInflater, parent, false);
        return new SourceAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SourceAdapterViewHolder holder, int position) {
        ((SourceAdapterViewHolder) holder).bindTo(sources.get(position), position);
    }

    @Override
    public int getItemCount() {
        return sources.size();
    }

    public interface OnSourceClick {
        void onSourceClick(Source source);
    }

    public class SourceAdapterViewHolder extends RecyclerView.ViewHolder {

        AdapterSourcesBinding binding;

        public SourceAdapterViewHolder(AdapterSourcesBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(Source source, int position) {
            binding.cv.setOnClickListener(view -> {
                onNewsClickListener.onSourceClick(source);
            });
            binding.title.setText(source.getName());
            binding.textView.setText(source.getCategory());
            binding.description.setText(source.getDescription());
        }
    }
}

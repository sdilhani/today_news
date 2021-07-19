package com.dilhani.todaynews.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dilhani.todaynews.R;
import com.dilhani.todaynews.databinding.AdapterNewsBinding;
import com.dilhani.todaynews.models.Article;
import com.squareup.picasso.Picasso;

import java.util.List;

import static android.content.ContentValues.TAG;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsAdapterViewHolder> {

    private final OnNewsClickListener onNewsClickListener;
    private List<Article> articles;

    public NewsAdapter(List<Article> articles, OnNewsClickListener onNewsClickListener) {
        this.articles = articles;
        this.onNewsClickListener = onNewsClickListener;
    }

    @Override
    public NewsAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        AdapterNewsBinding binding = AdapterNewsBinding.inflate(layoutInflater, parent, false);
        return new NewsAdapterViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapterViewHolder holder, int position) {
        ((NewsAdapterViewHolder) holder).bindTo(articles.get(position), position);
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    public interface OnNewsClickListener {
        void onNewsClick(Article article);
    }

    public class NewsAdapterViewHolder extends RecyclerView.ViewHolder {

        AdapterNewsBinding binding;

        public NewsAdapterViewHolder(AdapterNewsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bindTo(Article article, int position) {
            binding.getRoot().setOnClickListener(view -> {
                onNewsClickListener.onNewsClick(article);
            });

            try {
                String imagePath = article.getUrlToImage();
                if (imagePath != null) {
                    imagePath = imagePath.equals("") ? null : article.getUrlToImage();
                }
                Log.e(TAG, "bindTo: image " + imagePath);
                Picasso.get().load(imagePath).placeholder(R.drawable.globe_long).error(R.drawable.globe_long).resize(200, 200).into(binding.imageView3);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            binding.title.setText(article.getTitle());
            binding.textView.setText(article.getSource().getName());
        }

    }
}

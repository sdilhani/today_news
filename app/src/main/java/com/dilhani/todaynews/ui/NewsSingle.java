package com.dilhani.todaynews.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.dilhani.todaynews.R;
import com.dilhani.todaynews.databinding.ActivityNewsSingleBinding;
import com.dilhani.todaynews.models.Article;
import com.squareup.picasso.Picasso;

import static android.content.ContentValues.TAG;

public class NewsSingle extends BaseActivity {

    ActivityNewsSingleBinding binding;
    Article article;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news_single);

        article = (Article) getIntent().getSerializableExtra("article");

        if (article == null){
            showToastMessage("Something went wrong!");
            finish();
        }

        try {
            String imagePath = article.getUrlToImage();
            if(imagePath != null) {
                imagePath = imagePath.equals("") ? null : article.getUrlToImage();
            }
            Log.e(TAG, "bindTo: image " + imagePath);
            Picasso.get().load(imagePath).placeholder(R.drawable.globe_long).error(R.drawable.globe_long).into(binding.imageView4);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        binding.titleTv.setText(article.getTitle());
        binding.desciptionTv.setText(article.getDescription());
        binding.tvAuthor.setText(article.getAuthor());
        binding.tvDate.setText(article.getPublishedAt());
        binding.tvContent.setText(article.getContent());

        binding.button2.setOnClickListener(view -> {
            String url = article.getUrl();
            try {
                Intent i = new Intent("android.intent.action.MAIN");
                i.setComponent(ComponentName.unflattenFromString("com.android.chrome/com.android.chrome.Main"));
                i.addCategory("android.intent.category.LAUNCHER");
                i.setData(Uri.parse(url));
                startActivity(i);
            }
            catch(ActivityNotFoundException e) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(i);
            }
        });

        binding.source.setText(article.getSource().getName());
    }
}
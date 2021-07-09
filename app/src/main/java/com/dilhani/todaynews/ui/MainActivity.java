package com.dilhani.todaynews.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import com.dilhani.todaynews.R;
import com.dilhani.todaynews.databinding.ActivityMainBinding;
import com.dilhani.todaynews.fragments.AllNewsFragment;
import com.dilhani.todaynews.fragments.CategoriesFragment;
import com.dilhani.todaynews.fragments.FragmentAdapter;
import com.dilhani.todaynews.fragments.SearchFragment;
import com.dilhani.todaynews.fragments.SourcesFragment;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FragmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        init();
    }

    private void init(){

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        FragmentManager fm = getSupportFragmentManager();
        adapter = new FragmentAdapter(fm,getLifecycle());
        binding.viewpager.setAdapter(adapter);

        binding.tabs.addTab(binding.tabs.newTab().setText("ALL")/*.setIcon(R.drawable.ic_all)*/);
        binding.tabs.addTab(binding.tabs.newTab().setText("SEARCH")/*.setIcon(R.drawable.ic_search)*/);
        binding.tabs.addTab(binding.tabs.newTab().setText("TOPICS")/*.setIcon(R.drawable.ic_topics)*/);
        binding.tabs.addTab(binding.tabs.newTab().setText("SOURCES")/*.setIcon(R.drawable.ic_sources)*/);

        binding.tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                binding.viewpager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        binding.viewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                binding.tabs.selectTab(binding.tabs.getTabAt(position));
            }
        });

    }
}
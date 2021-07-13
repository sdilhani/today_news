package com.dilhani.todaynews.ui;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.dilhani.todaynews.R;
import com.dilhani.todaynews.databinding.ActivitySettingsBinding;
import com.dilhani.todaynews.utils.ApplicationConstants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class SettingsActivity extends BaseActivity {

    private final Map<String, String> countries = new HashMap<String, String>();
    private final Map<String, String> languages = new HashMap<>();

    private ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings);

        init();
    }

    private void init(){

        countries.put("ae", "United Arab Emirates");
        countries.put("ar", "Argentina");
        countries.put("au", "Australia");
        countries.put("be", "Belgium");
        countries.put("ca", "Canada");
        countries.put("de", "Germany");
        countries.put("fr", "France");
        countries.put("in", "India");
        countries.put("mx", "Mexico");
        countries.put("ua", "Ukraine");
        countries.put("us", "United States of America");
        countries.put("za", "South Africa");

        ArrayAdapter<String> countyAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, new ArrayList(countries.values()));
        countyAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.countrySpinner.setAdapter(countyAdapter);

        languages.put("ar", "Arabic");
        languages.put("de", "German");
        languages.put("en", "English");
        languages.put("fr", "French");
        languages.put("it", "Italian");
        languages.put("pt", "Portuguese");
        languages.put("ru", "Russian");
        languages.put("zh", "Chinese");

        ArrayAdapter<String> languageAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, new ArrayList(languages.values()));
        languageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.languageSpinner.setAdapter(languageAdapter);

        binding.button.setOnClickListener(view -> {
            String country =  (String) countries.keySet().toArray()[binding.countrySpinner.getSelectedItemPosition()]; // [binding.countrySpinner.getSelectedItem()]
            String language =  (String) languages.keySet().toArray()[binding.languageSpinner.getSelectedItemPosition()]; // [binding.countrySpinner.getSelectedItem()]

            getSharedPreferences().edit().putString(ApplicationConstants.COUNTRY, country).apply();
            getSharedPreferences().edit().putString(ApplicationConstants.LANGUAGE, language).apply();

            Log.e(TAG, "init: LG : " + language + " ct :" + country);
            showLongToastMessage("Successfully saved the settings");
            finish();
        });

        try {
            String country = getSharedPreferences().getString(ApplicationConstants.COUNTRY, "us");
            String language = getSharedPreferences().getString(ApplicationConstants.LANGUAGE, "en");

            ArrayList<String> countryKeys = new ArrayList<>(countries.keySet());
            ArrayList<String> languageKeys = new ArrayList<>(languages.keySet());

            binding.languageSpinner.setSelection(languageKeys.indexOf(language), true);
            binding.countrySpinner.setSelection(countryKeys.indexOf(country), true);
        } catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
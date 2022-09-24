package com.example.obligwordgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Locale;

public class ChangeLanguageActivity extends AppCompatActivity {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); inflater.inflate(R.menu.menu, menu);
        return true;
    }
    protected  void saveLanguage(String lang){
        SharedPreferences.Editor editor = this.getSharedPreferences(this.getPackageName(), MODE_PRIVATE).edit();
        editor.putString("lang", lang).apply();

    }
    protected void changeLanguage(  String lang){
        saveLanguage(lang);
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;

        //Language settings is updated
        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());
        setContentView(R.layout.activity_changelanguage);
        setup();
    }
    protected void setup(){
        Switch switchLanguageLocale = (Switch) findViewById(R.id.switchLanguageLocale);
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        String lang = sharedPreferences.getString("lang","no");

        //Make sure switch shows current state
        if(lang.equals("en")){
            switchLanguageLocale.performClick();
        }

        switchLanguageLocale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getSharedPreferences(getPackageName(), MODE_PRIVATE).edit();
                //New game when language is changed
                editor.putInt("points", 0).apply();
                editor.putStringSet("solutionsFound",new HashSet<String>()).apply();

                //Change between languages, based on state of switch
                if(!switchLanguageLocale.isChecked()){

                    changeLanguage("no");

                }
                else{

                    changeLanguage("en");


                }



            }

        });
    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        String lang = sharedPreferences.getString("lang","no");

        changeLanguage(lang);





    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changelanguage);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        setup();

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.allsolutions:
                Intent allsolutionsIntent = new Intent(ChangeLanguageActivity.this, AllSolutionsActivity.class);
                startActivity(allsolutionsIntent);
                return true;
            case R.id.wordsfound:
                Intent wordsfoundIntent = new Intent(ChangeLanguageActivity.this, WordFoundActivity.class);
                startActivity(wordsfoundIntent);
                return true;
            case R.id.rules:
                Intent rulesIntent = new Intent(ChangeLanguageActivity.this, RulesActivity.class);
                startActivity(rulesIntent);
                return true;
            case R.id.play:
                Intent playIntent = new Intent(ChangeLanguageActivity.this,PlayActivity.class);
                startActivity(playIntent);
                return true;
            case R.id.home:
                Intent mainIntent = new Intent(ChangeLanguageActivity.this, MainActivity.class);
                startActivity(mainIntent);
                return true;
            case R.id.language:
                Intent languageIntent = new Intent(ChangeLanguageActivity.this,ChangeLanguageActivity.class);
                startActivity(languageIntent);
                return true;
            case android.R.id.home:
                Intent mainIntent2= new Intent(ChangeLanguageActivity.this, MainActivity.class);
                startActivity(mainIntent2);


        }
        return super.onOptionsItemSelected(item);
    }

}
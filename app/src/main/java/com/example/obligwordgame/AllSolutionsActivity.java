package com.example.obligwordgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AllSolutionsActivity extends AppCompatActivity {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    protected void setup(){
        setContentView(R.layout.activity_allsolutions);
        LinearLayout solutions =(LinearLayout) findViewById(R.id.viewList);
        Resources res = getResources();

        //All solutions
        List<String> solutionsText = Arrays.asList(res.getStringArray(R.array.solutions));

        //Add all solutions to the list, with proper color and all
        solutionsText.forEach((x)->{
            TextView text = new TextView(this);
            text.setText(x.toString());
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34);
            text.setTextColor(Color.parseColor("#FFFFFF"));
            text.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT
            ));
            solutions.addView(text);
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);


                setup();
                //Menu bar located inside different activites
                ActionBar actionBar = getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);


    }
    /**
     * Change language, and store this in prefrences
     * @param lang change language to this
     */
    protected void changeLanguage(  String lang){

        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;

        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());
        setContentView(R.layout.activity_allsolutions);

    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setContentView(R.layout.activity_allsolutions);

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        String lang = sharedPreferences.getString("lang","no");

        changeLanguage(lang);
        setup();




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Intent mainIntent = new Intent(AllSolutionsActivity.this, MainActivity.class);
                startActivity(mainIntent);
                return true;
            case R.id.allsolutions:

                return true;
            case R.id.wordsfound:
                Intent wordsfoundIntent = new Intent(AllSolutionsActivity.this,WordFoundActivity.class);
                startActivity(wordsfoundIntent);
                return true;
            case android.R.id.home:
                Intent mainIntent2= new Intent(AllSolutionsActivity.this, MainActivity.class);
                startActivity(mainIntent2);
            case R.id.changeDifficulty:
                Intent changedifficultyIntent = new Intent(AllSolutionsActivity.this,ChangeDifficultyActivity.class);
                startActivity(changedifficultyIntent);
                return true;
            case R.id.play:
                Intent playIntent = new Intent(AllSolutionsActivity.this,PlayActivity.class);
                startActivity(playIntent);
                return true;
            case R.id.rules:
                Intent rulesIntent = new Intent(AllSolutionsActivity.this,RulesActivity.class);
                startActivity(rulesIntent);
                return true;
            case R.id.language:
                Intent languageIntent = new Intent(AllSolutionsActivity.this,ChangeLanguageActivity.class);
                startActivity(languageIntent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


}
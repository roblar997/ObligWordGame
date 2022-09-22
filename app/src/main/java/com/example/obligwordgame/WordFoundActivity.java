package com.example.obligwordgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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

import java.util.HashSet;
import java.util.Set;

public class WordFoundActivity extends AppCompatActivity {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                setContentView(R.layout.activity_wordsfound);
                LinearLayout solutions =(LinearLayout) findViewById(R.id.viewList);
                SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                Set<String> solutionsFound = sharedPreferences.getStringSet("solutionsFound",new HashSet<String>());
                solutionsFound.forEach((x)->{
                    TextView text = new TextView(this);
                    text.setText(x);
                    text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 34);
                    text.setTextColor(Color.parseColor("#FFFFFF"));
                    text.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                    ));
                    solutions.addView(text);
                });
                ActionBar actionBar = getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);


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
                Intent homeIntent = new Intent(WordFoundActivity.this, MainActivity.class);
                startActivity(homeIntent);
                return true;
            case R.id.allsolutions:
                Intent allsolutionsIntent = new Intent(WordFoundActivity.this,AllSolutionsActivity.class);
                startActivity(allsolutionsIntent);
                return true;
            case R.id.rules:
                Intent rulesIntent = new Intent(WordFoundActivity.this,RulesActivity.class);
                startActivity(rulesIntent);
                return true;
            case R.id.play:
                Intent playIntent = new Intent(WordFoundActivity.this,PlayActivity.class);
                startActivity(playIntent);
                return true;
            case R.id.changeDifficulty:
                Intent changedifficultyIntent = new Intent(WordFoundActivity.this,ChangeDifficultyActivity.class);
                startActivity(changedifficultyIntent);
                return true;
            case R.id.language:
                Intent languageIntent = new Intent(WordFoundActivity.this,ChangeLanguageActivity.class);
                startActivity(languageIntent);
                return true;
            case R.id.wordsfound:
                return true;
            case android.R.id.home:
                homeIntent = new Intent(WordFoundActivity.this, MainActivity.class);
                startActivity(homeIntent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
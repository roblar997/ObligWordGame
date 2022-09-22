package com.example.obligwordgame;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class RulesActivity extends AppCompatActivity {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rulesactivity);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.allsolutions:
                Intent allsolutionsIntent = new Intent(RulesActivity.this, AllSolutionsActivity.class);
                startActivity(allsolutionsIntent);
                return true;
            case R.id.wordsfound:
                Intent wordsfoundIntent = new Intent(RulesActivity.this, WordFoundActivity.class);
                startActivity(wordsfoundIntent);
                return true;
            case R.id.changeDifficulty:
                Intent changedifficultyIntent = new Intent(RulesActivity.this, ChangeDifficultyActivity.class);
                startActivity(changedifficultyIntent);
                return true;
            case R.id.play:
                Intent playIntent = new Intent(RulesActivity.this,PlayActivity.class);
                startActivity(playIntent);
                return true;
            case R.id.language:
                Intent languageIntent = new Intent(RulesActivity.this,ChangeLanguageActivity.class);
                startActivity(languageIntent);
                return true;

            case R.id.home:
                Intent mainIntent = new Intent(RulesActivity.this, PlayActivity.class);
                startActivity(mainIntent);
                return true;
            case android.R.id.home:
                Intent mainIntent2= new Intent(RulesActivity.this, MainActivity.class);
                startActivity(mainIntent2);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    }
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_main);
        setup();

    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        String lang = sharedPreferences.getString("lang","no");

        changeLanguage(lang);





    }
    protected void setup(){
        Button continueKnapp    =(Button)findViewById(R.id.continueKnapp);
        Button newgameKnapp     =(Button)findViewById(R.id.newgameKnapp);
        Button rulesKnapp       =(Button)findViewById(R.id.rulesKnapp);
        Button allSolutionKnapp =(Button)findViewById(R.id.allsolutionsKnapp);
        Button wordsFoundKnapp   =(Button)findViewById(R.id.wordsfoundKnapp);
        Button changedifficultyKnapp =(Button)findViewById(R.id.changedifficultyKnapp);
        Button changelanguageKnapp =(Button)findViewById(R.id.changeLanguageKnapp);
        Button exitKnapp =(Button)findViewById(R.id.exitKnapp);

        continueKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent playIntent = new Intent(MainActivity.this,PlayActivity.class);
                startActivity(playIntent);
            }
        });
        newgameKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent playIntent = new Intent(MainActivity.this,PlayActivity.class);
                SharedPreferences.Editor editor = getSharedPreferences(getPackageName(), MODE_PRIVATE).edit();
                editor.putInt("points", 0).apply();
                editor.putStringSet("solutionsFound",new HashSet<String>()).apply();
                startActivity(playIntent);
            }
        });
        rulesKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent rulesIntent = new Intent(MainActivity.this,RulesActivity.class);
                startActivity(rulesIntent);
            }
        });

        allSolutionKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent allSolutionsIntent = new Intent(MainActivity.this,AllSolutionsActivity.class);
                startActivity(allSolutionsIntent);
            }
        });
        wordsFoundKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent wordsFoundIntent = new Intent(MainActivity.this,WordFoundActivity.class);
                startActivity(wordsFoundIntent);
            }
        });
        changedifficultyKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changedifficultyIntent = new Intent(MainActivity.this,ChangeDifficultyActivity.class);
                startActivity(changedifficultyIntent);
            }
        });

        changelanguageKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent changelanguageIntent = new Intent(MainActivity.this,ChangeLanguageActivity.class);
                startActivity(changelanguageIntent);
            }
        });
        exitKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                setContentView(R.layout.activity_main);

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

                return true;
            case R.id.allsolutions:
                Intent allSolutionsActivityIntent = new Intent(MainActivity.this,AllSolutionsActivity.class);
                startActivity(allSolutionsActivityIntent);
                return true;

            case R.id.wordsfound:
                Intent wordsfoundIntent = new Intent(MainActivity.this,WordFoundActivity.class);
                startActivity(wordsfoundIntent);
                return true;
            case R.id.language:
                Intent languageIntent = new Intent(MainActivity.this,ChangeLanguageActivity.class);
                startActivity(languageIntent);
                return true;
            case android.R.id.home:

                return true;
            case R.id.changeDifficulty:
                Intent changedifficultyIntent = new Intent(MainActivity.this,ChangeDifficultyActivity.class);
                startActivity(changedifficultyIntent);
                return true;
            case R.id.play:
                Intent playIntent = new Intent(MainActivity.this,PlayActivity.class);
                startActivity(playIntent);
                return true;
            case R.id.rules:
                Intent rulesIntent = new Intent(MainActivity.this,RulesActivity.class);
                startActivity(rulesIntent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }


}
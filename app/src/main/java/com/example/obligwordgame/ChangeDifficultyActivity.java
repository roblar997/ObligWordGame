package com.example.obligwordgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Locale;

public class ChangeDifficultyActivity extends AppCompatActivity {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater(); inflater.inflate(R.menu.menu, menu);
        return true;
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
        setContentView(R.layout.activity_changedifficulty);
        setup();

    }
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setContentView(R.layout.activity_changedifficulty);

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        String lang = sharedPreferences.getString("lang","no");

        changeLanguage(lang);

        setup();



    }
    protected void setup(){
        TextView maxCharsInput = findViewById(R.id.maxCharsInput);
        TextView nmbWordsInput = findViewById(R.id.nmbWordsInput);
        TextView feedbackText = findViewById(R.id.feedbackText);

        Button maxCharsKnapp =(Button)findViewById(R.id.maxCharsKnapp);
        Button nmbWordsKnapp =(Button)findViewById(R.id.nmbWordsKnapp);
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

        //Current difficulty is shown
        int nmbWords = sharedPreferences.getInt("nmbWords",10);
        int maxChars = sharedPreferences.getInt("maxChars",10);
        maxCharsInput.setText(String.valueOf(maxChars));
        nmbWordsInput.setText(String.valueOf(nmbWords));

        nmbWordsInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                feedbackText.setText("");
            }
        });
        maxCharsInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                feedbackText.setText("");
            }
        });
        maxCharsKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = getSharedPreferences(getPackageName(), MODE_PRIVATE).edit();
                if(maxCharsInput.getText().toString().length() == 0)
                    return;
                Resources res = getResources();

                //Different responses based on input
                if(Integer.valueOf(maxCharsInput.getText().toString()) <  4){
                    String response = res.getString(R.string.response1);

                    feedbackText.setTextColor(Color.parseColor ("#F44336"));
                    feedbackText.setText(response);
                    return;

                }
                else
                    feedbackText.setTextColor(Color.parseColor ("#01FF0B"));

                //Maxium number of characters allowed in an answer
                editor.putInt("maxChars",Integer.parseInt(maxCharsInput.getText().toString())).apply();

                String changeResponse = res.getString(R.string.changeResponseText);


                feedbackText.setText(changeResponse);

                //New game
                editor.putInt("points", 0).apply();
                editor.putStringSet("solutionsFound",new HashSet<String>()).apply();
            }
        });
        nmbWordsKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences(getPackageName(), MODE_PRIVATE).edit();

                //No input
                if(nmbWordsInput.getText().toString().length() == 0)
                    return;

                editor.putInt("nmbWords",Integer.parseInt(nmbWordsInput.getText().toString())).apply();
                Resources res = getResources();
                String changeResponse = res.getString(R.string.changeResponseText);

                feedbackText.setText(changeResponse);

                //Start a new game
                editor.putInt("points", 0).apply();
                editor.putStringSet("solutionsFound",new HashSet<String>()).apply();
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changedifficulty);

        //Menu bar inside activites
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        setup();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {

            case R.id.allsolutions:
                Intent allsolutionsIntent = new Intent(ChangeDifficultyActivity.this, AllSolutionsActivity.class);
                startActivity(allsolutionsIntent);
                return true;
            case R.id.wordsfound:
                Intent wordsfoundIntent = new Intent(ChangeDifficultyActivity.this, WordFoundActivity.class);
                startActivity(wordsfoundIntent);
                return true;
            case R.id.rules:
                Intent rulesIntent = new Intent(ChangeDifficultyActivity.this, RulesActivity.class);
                startActivity(rulesIntent);
                return true;
            case R.id.play:
                Intent playIntent = new Intent(ChangeDifficultyActivity.this,PlayActivity.class);
                startActivity(playIntent);
                return true;
            case R.id.home:
                Intent mainIntent = new Intent(ChangeDifficultyActivity.this, MainActivity.class);
                startActivity(mainIntent);
                return true;
            case android.R.id.home:
                Intent mainIntent2= new Intent(ChangeDifficultyActivity.this, MainActivity.class);
                startActivity(mainIntent2);
            case R.id.language:
                Intent languageIntent = new Intent(ChangeDifficultyActivity.this,ChangeLanguageActivity.class);
                startActivity(languageIntent);
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

}
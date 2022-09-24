package com.example.obligwordgame;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Locale;

public class GoalActivity extends AppCompatActivity {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

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
        setContentView(R.layout.activity_goal);
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
        Button newGameButton =(Button)findViewById(R.id.newgameButton);
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GoalActivity.this, PlayActivity.class);
                startActivity(intent);
            }
        });
        SharedPreferences.Editor editor = this.getSharedPreferences(this.getPackageName(), MODE_PRIVATE).edit();
        //New game
        editor.putInt("points", 0).apply();
        editor.putStringSet("solutionsFound",new HashSet<String>()).apply();
        editor.putString("currentSolution", "").apply();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                setContentView(R.layout.activity_goal);

                ActionBar actionBar = getSupportActionBar();
                actionBar.setDisplayHomeAsUpEnabled(true);
                setup();

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent mainIntent2= new Intent(GoalActivity.this, MainActivity.class);
                startActivity(mainIntent2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
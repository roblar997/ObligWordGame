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
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changedifficulty);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView nmbInput = findViewById(R.id.nmbWordsInput);
        TextView maxCharsInput = findViewById(R.id.maxCharsInput);
        TextView feedbackText = findViewById(R.id.feedbackText);

        Button nmbWordsKnapp =(Button)findViewById(R.id.nmbWordsKnapp);
        Button maxCharsKnapp =(Button)findViewById(R.id.maxCharsKnapp);
        nmbInput.setOnClickListener(new View.OnClickListener() {
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
                editor.putInt("maxChars",Integer.parseInt(maxCharsInput.getText().toString())).apply();
                feedbackText.setText("Endring utført");
            }
        });
        nmbWordsKnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = getSharedPreferences(getPackageName(), MODE_PRIVATE).edit();
                if(nmbInput.getText().toString().length() == 0)
                    return;
                editor.putInt("nmbWords",Integer.parseInt(nmbInput.getText().toString())).apply();
                feedbackText.setText("Endring utført");
            }
        });
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

            case R.id.home:
                Intent mainIntent = new Intent(ChangeDifficultyActivity.this, PlayActivity.class);
                startActivity(mainIntent);
                return true;
            case android.R.id.home:
                finish();

        }
        return super.onOptionsItemSelected(item);
    }

}
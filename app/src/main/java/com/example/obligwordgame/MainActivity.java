package com.example.obligwordgame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {
    private enum responseType {SUCCESS,WARNING,ERROR,INFO}

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


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

        this.getResources().updateConfiguration(config, this.getResources().getDisplayMetrics());
        setContentView(R.layout.activity_main);
        setup();
    }

    protected void setCharsFromPrefrences(List<Button> buttonList){
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

        buttonList.forEach((x)->{
            String text = sharedPreferences.getString(x.getId()+"Text","");
            x.setText(text);
        });
    }
    protected void getHint(List<String> solutions, TextView currentSolution, TextView hintText, String middleButtonChar,TextView responsText){
        String currentSolutionText = currentSolution.getText().toString();
        List<String> possibleSuffixes = solutions.stream().filter((x)->x.startsWith(currentSolutionText) && x.contains(middleButtonChar)).map((x)->x.replaceFirst(currentSolutionText,"")).collect(Collectors.toList());
        if(possibleSuffixes.contains(""))
            return;
        if(possibleSuffixes.isEmpty()){
            setResponse(responsText,"No solutions starting \nwith your current input", responseType.INFO.ordinal());
            return;
        }
        setResponse(responsText,"You have a partially \n a correct solution.", responseType.INFO.ordinal());
        Random rand = new Random();

        int randIndex = rand.nextInt(possibleSuffixes.size());
        hintText.setText( String.valueOf(possibleSuffixes.get(randIndex).toString().charAt(0))) ;
    }
    protected void randomChars(List<Button> buttonList){

        AtomicReference<String> gen;
        SharedPreferences.Editor editor = this.getSharedPreferences(this.getPackageName(), MODE_PRIVATE).edit();
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        String lang = sharedPreferences.getString("lang","no");


        if(!lang.equals("no"))
            gen = new AtomicReference<String>("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        else
            gen = new AtomicReference<String>("ABCDEFGHIJKLMNOPQRSTUVWXYZÆØÅ");

        Random rand = new Random();


        buttonList.forEach((x)->{
            int randVal = rand.nextInt(gen.get().length());

            x.setText(String.valueOf(gen.get().charAt(randVal)));
            gen.set(gen.get().replace(String.valueOf(gen.get().charAt(randVal)),""));
            editor.putString(String.valueOf(x.getId())+"Text", x.getText().toString()).apply();
        });

        }
    protected boolean addPoint(TextView pointView){

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

        int points = sharedPreferences.getInt("points",0) + 1;
        if(points == 10){
            Intent intent=new Intent(MainActivity.this,GoalActivity.class);
            startActivity(intent);


        }

        SharedPreferences.Editor editor = this.getSharedPreferences(this.getPackageName(), MODE_PRIVATE).edit();
        editor.putInt("points", points).apply();

        pointView.setText(String.valueOf(points) + " of 10");

        return false;
    }
    protected void saveCurrentSolution(String currentSolution){
        SharedPreferences.Editor editor = this.getSharedPreferences(this.getPackageName(), MODE_PRIVATE).edit();
        editor.putString("currentSolution", currentSolution).apply();
    }
    protected void setResponse(TextView placeholder, String message,int responseType){

        switch (MainActivity.responseType.values()[responseType]) {

            case SUCCESS:
                placeholder.setTypeface(null, Typeface.BOLD);
                placeholder.setTextColor(Color.parseColor ("#4CAF50"));
                break;
            case ERROR:
                placeholder.setTypeface(null, Typeface.BOLD);
                placeholder.setTextColor(Color.parseColor ("#F44336"));
                break;
            case WARNING:
                placeholder.setTypeface(null, Typeface.BOLD);
                placeholder.setTextColor(Color.parseColor ("#FFF599"));
                break;
            case INFO:
                placeholder.setTypeface(null, Typeface.BOLD);
                placeholder.setTextColor(Color.parseColor("#03A9F4"));
            default:
                break;
        }
        placeholder.setText(message);

    }
    protected void storeAnswer(String answer){
        SharedPreferences.Editor editor = this.getSharedPreferences(this.getPackageName(), MODE_PRIVATE).edit();
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        Set<String> solutionsFound = sharedPreferences.getStringSet("solutionsFound",new HashSet<String>());
        solutionsFound.add(answer);
        editor.putStringSet("solutionsFound", solutionsFound).apply();
    }
    protected void answer(TextView placeholder,TextView pointView, String answer, String middleChar,List<String> solutions){

        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        Set<String> solutionsFound = sharedPreferences.getStringSet("solutionsFound",new HashSet<String>());

        if(answer.length() < 4){
            setResponse(placeholder,"Each word needs at \nleast 4 characters", responseType.ERROR.ordinal());
        }
        else if(!answer.contains(middleChar)){
            setResponse(placeholder,"Answer must contain \nmiddle character", responseType.ERROR.ordinal());
        }
        else if(!solutions.contains(answer) && !solutionsFound.contains(answer)){

            setResponse(placeholder,"Inncorrect answer", responseType.ERROR.ordinal());
        }
        else {

            addPoint(pointView);
            setResponse(placeholder,"Correct answer", responseType.SUCCESS.ordinal());
            storeAnswer(answer);

        }

    }
    protected void sharedPreferencesSetup(Switch switchLanguageLocale, TextView responseText, TextView currentSolutionView){
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        String lang = sharedPreferences.getString("lang","no");


        if(!lang.equals("no")){
            switchLanguageLocale.performClick();

        }
        String response = sharedPreferences.getString("responseText","");
        responseText.setText(response);

        String currentSolution = sharedPreferences.getString("currentSolution","");
        currentSolutionView.setText(currentSolution);
    }
    protected void setup(){

        TextView currentSolution=(TextView) findViewById(R.id.currentSolution);
        TextView responseText =(TextView) findViewById(R.id.responseText);
        TextView pointView = (TextView) findViewById(R.id.pointView);
        TextView hintText = (TextView) findViewById(R.id.hintText);

        Button knapp1 =(Button)findViewById(R.id.knapp1);
        Button knapp2 =(Button)findViewById(R.id.knapp2);
        Button knapp3 =(Button)findViewById(R.id.knapp3);
        Button knapp4 =(Button)findViewById(R.id.knapp4);
        Button knapp5 =(Button)findViewById(R.id.knapp5);
        Button button5 =(Button)findViewById(R.id.button5);
        Button knapp6 =(Button)findViewById(R.id.knapp6);
        Button knapp7 =(Button)findViewById(R.id.knapp7);

        Button undoButton =(Button)findViewById(R.id.undoButton);
        Button hintButton =(Button)findViewById(R.id.hintButton);
        Button resetButton =(Button)findViewById(R.id.resetButton);
        Button answerButton =(Button)findViewById(R.id.answerButton);
        Button newGameButton =(Button)findViewById(R.id.newGameButton);

        ImageView randomButton =(ImageView)findViewById(R.id.randomButton);

        List<Button> buttonList = Arrays.asList(knapp1,knapp2,knapp3,knapp4,knapp5,knapp6,knapp7);
        List<Button> buttonListWithoutMiddle = Arrays.asList(knapp1,knapp2,knapp3,knapp4,knapp6,knapp7);

        Switch switchLanguageLocale = (Switch) findViewById(R.id.switchLanguageLocale);
        sharedPreferencesSetup(switchLanguageLocale,responseText,currentSolution);
        setCharsFromPrefrences(buttonList);

        Resources res = getResources();
        List<String> solutions = Arrays.asList(res.getStringArray(R.array.solutions));

        hintButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHint(solutions,currentSolution,hintText,knapp5.getText().toString(),responseText);
            }
        });
        newGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                randomChars(buttonList);
                hintText.setText("");
                SharedPreferences.Editor editor = getSharedPreferences(getPackageName(), MODE_PRIVATE).edit();
                editor.putInt("points", 0).apply();
                editor.putStringSet("solutionsFound",new HashSet<String>()).apply();
                pointView.setText(String.valueOf(0) + " of 10");
                resetButton.performClick();
            }
        });
        switchLanguageLocale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                newGameButton.performClick();
                if(!switchLanguageLocale.isChecked()){
                    SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                    String lang = sharedPreferences.getString("lang","no");
                    if(lang.equals("no")){
                        changeLanguage("en");

                    }
                    else{
                        changeLanguage("no");
                    }


                }
                else{
                    SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                    String lang = sharedPreferences.getString("lang","no");
                    if(lang.equals("no")){
                        changeLanguage("en");
                    }
                    else{
                        changeLanguage("no");
                    }

                }



            }

        });

        randomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hintText.setText("");

                randomChars(buttonListWithoutMiddle);
            }
        });
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hintText.setText("");
                answer(responseText,pointView,currentSolution.getText().toString(),knapp5.getText().toString(),solutions);
            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hintText.setText("");
                currentSolution.setText("");
                saveCurrentSolution(currentSolution.getText().toString());
            }
        });
        undoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hintText.setText("");
                if(currentSolution.length() != 0) {
                    currentSolution.setText(currentSolution.getText().subSequence(0,currentSolution.getText().length()-1));
                    saveCurrentSolution(currentSolution.getText().toString());
                }
            }
        });
        knapp1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResponse(responseText,"", 0);
                hintText.setText("");
                if(currentSolution.getText().length() == 12)
                    return;
                currentSolution.setText(currentSolution.getText()+""+knapp1.getText());
                saveCurrentSolution(currentSolution.getText().toString());
            }
        });
        knapp2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResponse(responseText,"", 0);
                hintText.setText("");
                if(currentSolution.getText().length() == 12)
                    return;
                currentSolution.setText(currentSolution.getText()+""+knapp2.getText());
                saveCurrentSolution(currentSolution.getText().toString());
            }
        });
        knapp3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResponse(responseText,"", 0);
                hintText.setText("");
                if(currentSolution.getText().length() == 12)
                    return;
                currentSolution.setText(currentSolution.getText()+""+knapp3.getText());
                saveCurrentSolution(currentSolution.getText().toString());
            }
        });
        knapp4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResponse(responseText,"", 0);
                hintText.setText("");
                if(currentSolution.getText().length() == 12)
                    return;
                currentSolution.setText(currentSolution.getText()+""+knapp4.getText());
                saveCurrentSolution(currentSolution.getText().toString());
            }
        });
        knapp5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResponse(responseText,"", 0);
                hintText.setText("");
                if(currentSolution.getText().length() == 12)
                    return;
                currentSolution.setText(currentSolution.getText()+""+knapp5.getText());
                saveCurrentSolution(currentSolution.getText().toString());
            }
        });
        knapp6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResponse(responseText,"", 0);
                hintText.setText("");
                if(currentSolution.getText().length() == 12)
                    return;
                currentSolution.setText(currentSolution.getText()+""+knapp6.getText());
                saveCurrentSolution(currentSolution.getText().toString());
            }
        });
        knapp7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResponse(responseText,"", 0);
                hintText.setText("");
                if(currentSolution.getText().length() == 12)
                    return;
                currentSolution.setText(currentSolution.getText()+""+knapp7.getText());
                saveCurrentSolution(currentSolution.getText().toString());
            }
        });

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,WordFoundActivity.class);

                startActivity(intent);
                setContentView(R.layout.activity_wordsfound);
            }
        });

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        setup();
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
            case R.id.allsolutions:
                Intent allsolutionsIntent = new Intent(MainActivity.this,AllSolutionsActivity.class);
                startActivity(allsolutionsIntent);
                return true;
            case R.id.wordsfound:
                Intent wordsfoundIntent = new Intent(MainActivity.this,WordFoundActivity.class);
                startActivity(wordsfoundIntent);
                return true;
            case android.R.id.home:
                finish();

        }
        return super.onOptionsItemSelected(item);
    }


}
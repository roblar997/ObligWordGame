package com.example.obligwordgame;

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
import android.widget.Switch;
import android.widget.TextView;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class PlayActivity extends AppCompatActivity {
    private enum responseType {SUCCESS,WARNING,ERROR,INFO}

    private Resources res;

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
        setContentView(R.layout.activity_play);
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
        SharedPreferences.Editor editor = this.getSharedPreferences(this.getPackageName(), MODE_PRIVATE).edit();
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        Set<String> solutionsFound = sharedPreferences.getStringSet("solutionsFound",new HashSet<String>());

        List<String> possibleSuffixes = solutions.stream().filter((x)->x.startsWith(currentSolutionText) && x.contains(middleButtonChar) && !solutionsFound.contains(x)).map((x)->x.replaceFirst(currentSolutionText,"")).collect(Collectors.toList());

        String response2 = res.getString(R.string.response2);
        String response6 = res.getString(R.string.response6);
        if(possibleSuffixes.contains(""))
            return;
        if(possibleSuffixes.isEmpty()){
            setResponse(responsText,response6, responseType.INFO.ordinal());
            return;
        }
        setResponse(responsText,response2, responseType.INFO.ordinal());
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
        int nmbWords = sharedPreferences.getInt("nmbWords",0);

        if(points == nmbWords){
            Intent intent=new Intent(PlayActivity.this,GoalActivity.class);
            startActivity(intent);


        }

        SharedPreferences.Editor editor = this.getSharedPreferences(this.getPackageName(), MODE_PRIVATE).edit();
        editor.putInt("points", points).apply();


        pointView.setText(String.valueOf(points) + " of " + String.valueOf(nmbWords));

        return false;
    }
    protected void saveCurrentSolution(String currentSolution){
        SharedPreferences.Editor editor = this.getSharedPreferences(this.getPackageName(), MODE_PRIVATE).edit();
        editor.putString("currentSolution", currentSolution).apply();
    }
    protected void setResponse(TextView placeholder, String message,int responseType){

        switch (PlayActivity.responseType.values()[responseType]) {

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
        int maxChars = sharedPreferences.getInt("maxChars",4);

        String response1 = res.getString(R.string.response1);
        String response2 = res.getString(R.string.response2);
        String response3 = res.getString(R.string.response3);
        String response4 = res.getString(R.string.response4);
        String response5 = res.getString(R.string.response5);
        String response7 = res.getString(R.string.response7);
        String response8 = res.getString(R.string.response8);
        if(answer.length() < 4){
            setResponse(placeholder,response1, responseType.ERROR.ordinal());
        }
        else if(answer.length() > maxChars){
            setResponse(placeholder, response8.replace("{}",String.valueOf(maxChars)), responseType.ERROR.ordinal());
        }
        else if(!answer.contains(middleChar)){
            setResponse(placeholder,response7, responseType.ERROR.ordinal());
        }
        else if(solutionsFound.contains(answer)){
            setResponse(placeholder,response3, responseType.ERROR.ordinal());
        }
        else if(!solutions.contains(answer)){

            setResponse(placeholder,response4, responseType.ERROR.ordinal());
        }
        else {

            addPoint(pointView);
            setResponse(placeholder,response5, responseType.SUCCESS.ordinal());

            storeAnswer(answer);

        }

    }
    protected void sharedPreferencesSetup(Switch switchLanguageLocale, TextView responseText, TextView currentSolutionView){
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        String lang = sharedPreferences.getString("lang","no");


        if(lang.equals("en")){
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
        int minLengthSolution = 4;
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

        this.res = getResources();
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
                SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                int nmbWords = sharedPreferences.getInt("nmbWords",0);
                editor.putInt("points", 0).apply();
                editor.putStringSet("solutionsFound",new HashSet<String>()).apply();
                pointView.setText(String.valueOf(0) + " of " + String.valueOf(nmbWords));
                resetButton.performClick();

                //Se etter løsninge som inneholder knappen i midten, og ignorer ugyldige løsninger som er mindre enn 4
                List<String> possibleSuffixes = solutions.stream().filter((x)->x.contains(knapp5.getText()) && x.length() >= minLengthSolution).collect(Collectors.toList());
                //Ikke løsbar, så lang et nytt spill
                if(possibleSuffixes.stream().count() < 3)
                    newGameButton.performClick();
            }
        });


        switchLanguageLocale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                newGameButton.performClick();
                SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
                if(!switchLanguageLocale.isChecked()){

                    String lang = sharedPreferences.getString("lang","no");
                    if(lang.isEmpty())
                        switchLanguageLocale.setText("te");
                    if(lang.equals("no")){
                        changeLanguage("en");

                    }
                    else{
                        changeLanguage("no");
                    }


                }
                else{

                    String lang = sharedPreferences.getString("lang","no");
                    if(lang.equals("no")){
                        changeLanguage("en");
                    }
                    else{
                        changeLanguage("no");
                    }

                }
                TextView pointView = (TextView) findViewById(R.id.pointView);
                int nmbWords = sharedPreferences.getInt("nmbWords",0);
                int points = sharedPreferences.getInt("points",0);
                pointView.setText(String.valueOf(points) + " of " + String.valueOf(nmbWords));



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
                Intent intent=new Intent(PlayActivity.this,WordFoundActivity.class);

                startActivity(intent);
                setContentView(R.layout.activity_wordsfound);
            }
        });

        if(knapp5.getText().length() ==0){
            newGameButton.performClick();
        }

    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        TextView pointView = (TextView) findViewById(R.id.pointView);
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        String lang = sharedPreferences.getString("lang","no");
        if(lang != null){
            changeLanguage(lang);
        }
        int points = sharedPreferences.getInt("points",0);
        int nmbWords = sharedPreferences.getInt("nmbWords",0);

        pointView.setText(String.valueOf(points) + " of " + String.valueOf(nmbWords));




    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play);
        Switch switchLanguageLocale = (Switch) findViewById(R.id.switchLanguageLocale);
        SharedPreferences sharedPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);

        ActionBar actionBar = getSupportActionBar();
        setup();
        int points = sharedPreferences.getInt("points",0);
        TextView pointView = (TextView) findViewById(R.id.pointView);
        int nmbWords = sharedPreferences.getInt("nmbWords",0);

        pointView.setText(String.valueOf(points) + " of " + String.valueOf(nmbWords));

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
                Intent allsolutionsIntent = new Intent(PlayActivity.this,AllSolutionsActivity.class);
                startActivity(allsolutionsIntent);
                return true;
            case R.id.wordsfound:
                Intent wordsfoundIntent = new Intent(PlayActivity.this,WordFoundActivity.class);
                startActivity(wordsfoundIntent);
                return true;
            case R.id.changeDifficulty:
                Intent changedifficultyIntent = new Intent(PlayActivity.this,ChangeDifficultyActivity.class);
                startActivity(changedifficultyIntent);
                return true;

            case R.id.rules:
                Intent rulesIntent = new Intent(PlayActivity.this,RulesActivity.class);
                startActivity(rulesIntent);
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
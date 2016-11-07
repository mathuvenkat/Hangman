package aditi.hangman;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
/**
 * Created by mvenkatesan on 11/4/16.
 */

public class VocabGame extends Activity  {

    private GoogleApiClient client;

    RadioButton choiceA;
    RadioButton choiceB;
    RadioButton choiceC;
    TextView questionTextView;
    RadioGroup grp;

    int cnt  = 0;
    String questionsFile = "questions.csv";
    String question;
    List<String> choices ;
    String answer;
    String selectedAnswer;
    private static final String PREF_cnt = "count";
    List<Data> questionsAndChoices = new ArrayList<Data>();


    private class Data{
        String question;
        String answer;
        List<String> choices = new ArrayList<String>();
        public Data(String q, String a,List<String> choices){
            this.answer=a;
            this.question=q;
            this.choices =choices;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocab);
        cnt = getPreferences(MODE_PRIVATE).getInt(
                PREF_cnt, 0);
        grp = (RadioGroup) this.findViewById(R.id.choices);
        choiceA = (RadioButton)grp.findViewById(R.id.choiceA);
        choiceB = (RadioButton)grp.findViewById(R.id.choiceB);
        choiceC = (RadioButton)grp.findViewById(R.id.choiceC);
        questionTextView = (TextView)this.findViewById(R.id.questionText);

        InputStream stream = getClass().getClassLoader().getResourceAsStream(questionsFile);
        BufferedReader br = null;
        String arr[];
        String line;
        try {
            if (stream != null) {
                br = new BufferedReader(new InputStreamReader(stream));
                while ((line = br.readLine()) != null) {
                    arr = line.split(";");
                    question = arr[0];
                    choices = new ArrayList<String>();
                    choices.add(arr[1]);
                    choices.add(arr[2]);
                    choices.add(arr[3]);
                    answer = arr[4];
                    Data data = new Data(question,answer , choices);
                    questionsAndChoices.add(data);
                }
            }
        } catch (Exception e) {
        } finally {
            try {
                br.close();
                stream.close();
            } catch (Exception e) {

            }
        }
        setData();
        setOnClickListeners();
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void playMusic(Boolean isCorrect){
        if(isCorrect){
            Music.play(this,R.raw.correct);
        } else{
            Music.play(this,R.raw.wrong);
        }

    }

    private void setData(){
        choiceA.setText(questionsAndChoices.get(cnt).choices.get(0));
        choiceB.setText(questionsAndChoices.get(cnt).choices.get(1));
        choiceC.setText(questionsAndChoices.get(cnt).choices.get(2));
        String questionString = questionsAndChoices.get(cnt).question;
        String formattedQuestion = questionString.replaceAll("#", " ______ ");

        questionTextView.setText(formattedQuestion);
        answer = questionsAndChoices.get(cnt).answer;
        //grp.clearCheck();
        choiceA.setChecked(false);
        choiceB.setChecked(false);
        choiceC.setChecked(false);
    }

    private void setOnClickListeners(){
        RadioGroup choices = (RadioGroup)this.findViewById(R.id.choices);
        choices.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                switch(checkedId){
                    case R.id.choiceA:
                        selectedAnswer = (String)choiceA.getText();
                        break;
                    case R.id.choiceB:
                        selectedAnswer = (String)choiceB.getText();
                        break;
                    case R.id.choiceC:
                        selectedAnswer = (String)choiceC.getText();
                        break;
                }

                if(answer.equals(selectedAnswer)){
                    //right sound
                    playMusic(true);
                    cnt++;

                    if( cnt < questionsAndChoices.size()) {
                        setData();
                    } else{
                        cnt = 0;
                        setData();
                    }
                } else{
                    playMusic(false);
                }


            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Game Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://aditi.hangman/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }


    @Override
    public void onPause(){
        getPreferences(MODE_PRIVATE).edit().putInt(PREF_cnt,
                cnt).commit();
        Music.stop(this);
        super.onPause();

    }


    @Override
    public void onStop() {

        getPreferences(MODE_PRIVATE).edit().putInt(PREF_cnt,
                cnt).commit();

        //Log.d(TAG, "onStop");
        super.onPause();
        //Log.d(TAG, "stopping music");
        Music.stop(this);

        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Game Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app URL is correct.
                Uri.parse("android-app://aditi.hangman/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }




}

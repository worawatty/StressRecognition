package com.example.worawat.stressrecognition;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.mobile.AWSMobileClient;
import com.amazonaws.models.nosql.SasScoreDO;
import com.database.UpdateSASScore;
import com.opencsv.CSVReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

//edit this activity for stress recognition


public class MainActivity extends AppCompatActivity implements QuestionFragment.OnFragmentInteractionListener, View.OnClickListener, UpdateSASScore.AsyncResponseScore {
    ArrayList<String[]> questions;
    ArrayList<int[]> answer;
    int questionCounter;
    Button a1;
    Button a2;
    Button a3;
    Button a4;
    Button a5;
    Button a6;
    Button backButton;
    TextView questionText;
    TextView questionNumber;
    SharedPreferences userSharedPreferences;
    SasScoreDO sasScoreDO;
    UpdateSASScore updateSASScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        updateSASScore = new UpdateSASScore(this);
        sasScoreDO = new SasScoreDO();
        questions = new ArrayList<>();
        answer=new ArrayList<>();
        questions =readCsv();
        questionCounter =0;
        int i=0;
        userSharedPreferences = getApplicationContext().getSharedPreferences(getResources().getString(R.string.share_pref_name_users), Context.MODE_PRIVATE);
        String q1= questions.get(0)[0];
        Toast.makeText(getApplicationContext(),q1,Toast.LENGTH_LONG).show();
        for (String []question :questions) {

            Log.i("question "+i,question[0]);
            i++;
        }
        setContentView(R.layout.activity_main);
        a1=(Button)findViewById(R.id.button);
        a2=(Button)findViewById(R.id.button2);
        a3=(Button)findViewById(R.id.button3);
        a4=(Button)findViewById(R.id.button4);
        a5=(Button)findViewById(R.id.button5);
        a6=(Button)findViewById(R.id.button6);
        backButton = (Button)findViewById(R.id.backButton);
        questionNumber = (TextView)findViewById(R.id.question_number);
        questionText = (TextView)findViewById(R.id.question);

    }

    @Override
    protected void onResume(){
        super.onResume();
        questionText.setText(questions.get(questionCounter)[0]);
        questionNumber.setText("Question Number "+(questionCounter+1));
        a1.setOnClickListener(this);
        a2.setOnClickListener(this);
        a3.setOnClickListener(this);
        a4.setOnClickListener(this);
        a5.setOnClickListener(this);
        a6.setOnClickListener(this);
        backButton.setOnClickListener(this);


    }


    public final ArrayList<String[]> readCsv() {
        ArrayList<String[]> questionList = new ArrayList<>();


        try {
            InputStream csvStream = getResources().openRawResource(R.raw.questions);
            InputStreamReader csvStreamReader = new InputStreamReader(csvStream);
            CSVReader csvReader = new CSVReader(csvStreamReader);
            String line[];

            // throw away the header
           // csvReader.readNext();

            while ((line = csvReader.readNext()) != null) {
                questionList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return questionList;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public void processAnswer(int choice){
        if(questionCounter!=32){
            if(answer.size()>=questionCounter+1){
                int[]score = {questionCounter,choice};
                answer.set(questionCounter,score);
            }else{
                int[]score = {questionCounter,choice};
                answer.add(questionCounter,score);
            }
            Log.i("Current Score","Question No.:"+answer.get(questionCounter)[0]+" score:"+answer.get(questionCounter)[1]);
            questionCounter++;
            questionText.setText(questions.get(questionCounter)[0]);
            questionNumber.setText("Question Number "+(questionCounter+1));
        }else{
            int[]score = {questionCounter,choice};
            answer.add(questionCounter,score);
            int sum=0;
            for(int [] totalScore:answer){
                Log.i("socre",totalScore[1]+"");
                sum+=totalScore[1];
            }
            sasScoreDO.setSasScore(""+sum);
            sasScoreDO.setScoreId(""+System.currentTimeMillis());
            sasScoreDO.setUserId(userSharedPreferences.getString(getString(R.string.user_email),"No Email"));
            AWSMobileClient.initializeMobileClientIfNecessary(getApplicationContext());
            updateSASScore.execute(sasScoreDO);
            Log.i("Total Score",""+sum);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button:
                processAnswer(1);
                break;
            case R.id.button2:
                processAnswer(2);
                break;
            case R.id.button3:
                processAnswer(3);
                break;
            case R.id.button4:
                processAnswer(4);
                break;
            case R.id.button5:
                processAnswer(5);
                break;
            case R.id.button6:
                processAnswer(6);
                break;
            case R.id.backButton:
                questionCounter--;
                questionText.setText(questions.get(questionCounter)[0]);
                questionNumber.setText("Question Number "+(questionCounter+1));
                break;
        }
    }

    @Override
    public void processFinish() {
        SharedPreferences sp = getApplicationContext().getSharedPreferences(getResources().getString(R.string.share_pref_name_users),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putLong(getString(R.string.start_date),System.currentTimeMillis());
        editor.commit();

        Intent i = new Intent(getApplicationContext(),StartService.class);
        startActivity(i);
    }
}

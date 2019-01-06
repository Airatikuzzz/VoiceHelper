package com.example.maira.voicehelper;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.maira.voicehelper.dataaccess.AppDatabase;
import com.example.maira.voicehelper.network.NetworkUtils;
import com.example.maira.voicehelper.storage.StorageUtils;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {


    @Inject
    NetworkUtils networkUtils;
    @Inject
    StorageUtils storageUtils;
    @Inject
    AppDatabase db;


    private static final int VR_REQUEST=999;
    private static final int MY_DATA_CHECK_CODE =9991 ;

    private final String LOG_TAG="SpeechRepeatActivity";

    private String said = "";
    private TextToSpeech repeatTTS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        App.getComponent().injectsMainActivity(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listenToSpeech();
            }
        });

        Button btn = findViewById(R.id.btn_speak);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                repeatTTS.speak(said,TextToSpeech.QUEUE_FLUSH, null);
            }
        });

        Button btnStart = findViewById(R.id.btn_start);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity_(TestActivity.class);
            }
        });
        Intent checkTTSIntent=new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);
    }

    private void startActivity_(Class<? extends AppCompatActivity> cls){
        Intent intent = new Intent(this,cls);
        startActivity(intent);
    }

    private void listenToSpeech() {
        Intent listenIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        listenIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                getClass().getPackage().getName());
        listenIntent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Say a word!");
        listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,10);

        startActivityForResult(listenIntent, VR_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode== VR_REQUEST && resultCode== RESULT_OK)
        {
            ArrayList<String> suggestedWords=
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            said = suggestedWords.get(0).toString();
            Toast.makeText(this, said, Toast.LENGTH_LONG).show();
            Log.d(LOG_TAG, suggestedWords.toString());
        }

        if(requestCode== MY_DATA_CHECK_CODE)
        {
            if(resultCode== TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
                repeatTTS=new TextToSpeech(this, this);
            else
            {
                Intent installTTSIntent=new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onInit(int i) {
        if(i== TextToSpeech.SUCCESS)
            repeatTTS.setLanguage(Locale.getDefault());//Язык
    }
}

package com.example.maira.voicehelper;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import androidx.annotation.NonNull;

import com.devlomi.record_view.RecordButton;
import com.devlomi.record_view.RecordView;
import com.example.maira.voicehelper.repository.Repository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.maira.voicehelper.components.DaggerAppComponent;
import com.example.maira.voicehelper.models.Animal;
import com.example.maira.voicehelper.models.Category;
import com.example.maira.voicehelper.models.Question;
import com.example.maira.voicehelper.modules.AppModule;
import com.example.maira.voicehelper.storage.StorageUtils;
import com.example.maira.voicehelper.voice.IVoiceEngine;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, RecognitionListener {
    @Inject
    StorageUtils storageUtils;

    FirebaseFirestore db;

    private static final int VR_REQUEST=999;
    private static final int MY_DATA_CHECK_CODE =9991 ;

    private final String LOG_TAG="SpeechRepeatActivity";

    private String said = "";
    private TextToSpeech repeatTTS;


    private static final int REQUEST_RECORD_PERMISSION = 100;
    private TextView returnedText;
    private ToggleButton toggleButton;
    private ProgressBar progressBar;
    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;
    private String LOG_TAG_ = "VoiceRecognitionActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();
        App.getComponent().injectsMainActivity(this);

        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Button btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(v -> {
                startActivity_(TestActivity.class);
        });

        /*Button btnDownload = findViewById(R.id.btn_download);
        btnDownload.setOnClickListener(view->{
            storageUtils.downloadMp3Files();
        });*/

    }

    private void filldata(){
        List<Category> categoryList = new ArrayList<>();
        List<Question> questionList = new ArrayList<>();
        categoryList.add(new Category(0,"Животные"));
        List<String> animalsForQuestions = new ArrayList<String>();
        List<String> animals_ = new ArrayList<String>();
        final List<Animal> animals__ = new ArrayList<Animal>();


        Collections.addAll(animals_, "bird","chicken","deer","dolphin","duck","frog","goose",
                "horse","lion","monkey","mouse","parrot","pig","turkey");
        int i=6;
        for (String s: animals_) {
            Animal animal = new Animal(i,s, 0, i, s);
            db.collection("animals").add(animal);
        }

    }

    private void startActivity_(Class<? extends AppCompatActivity> cls){
        Intent intent = new Intent(this,cls);
        startActivity(intent);
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_RECORD_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    speech.startListening(recognizerIntent);
                } else {
                    Toast.makeText(MainActivity.this, "Permission Denied!", Toast
                            .LENGTH_SHORT).show();
                }
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (speech != null) {
            speech.destroy();
            Log.i(LOG_TAG, "destroy");
        }
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(LOG_TAG, "onBeginningOfSpeech");
        progressBar.setIndeterminate(false);
        progressBar.setMax(10);
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(LOG_TAG, "onBufferReceived: " + buffer);
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(LOG_TAG, "onEndOfSpeech");
        progressBar.setIndeterminate(true);
        toggleButton.setChecked(false);
    }

    @Override
    public void onError(int errorCode) {
        String errorMessage = getErrorText(errorCode);
        Log.d(LOG_TAG, "FAILED " + errorMessage);
        returnedText.setText(errorMessage);
        toggleButton.setChecked(false);
    }

    @Override
    public void onEvent(int arg0, Bundle arg1) {
        Log.i(LOG_TAG, "onEvent");
    }

    @Override
    public void onPartialResults(Bundle arg0) {
        Log.i(LOG_TAG, "onPartialResults");
    }

    @Override
    public void onReadyForSpeech(Bundle arg0) {
        Log.i(LOG_TAG, "onReadyForSpeech");
    }

    @Override
    public void onResults(Bundle results) {
        Log.i(LOG_TAG, "onResults");
        ArrayList<String> matches = results
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        String text = "";
        for (String result : matches)
            text += result + "\n";

        returnedText.setText(text);
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(LOG_TAG, "onRmsChanged: " + rmsdB);
        progressBar.setProgress((int) rmsdB);
    }

    public static String getErrorText(int errorCode) {
        String message;
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        return message;
    }
}

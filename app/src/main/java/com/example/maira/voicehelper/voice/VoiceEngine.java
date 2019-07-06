package com.example.maira.voicehelper.voice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import com.example.maira.voicehelper.MainActivity;
import com.example.maira.voicehelper.contractviews.IGamesContractsView;

public class VoiceEngine implements IVoiceEngine, RecognitionListener {
    public static final int VR_REQUEST=999;
    public static final int INITIAL_CHECk =9991 ;

    private SpeechRecognizer speech = null;
    private Intent recognizerIntent;

    Activity activity;
    private TextToSpeech repeatTTS;
    private String LOG_TAG = "VoiceRecognitionActivity";


    public VoiceEngine(){
    }


    public void setView(IGamesContractsView view){
        activity = (Activity) view;
    }
    @Override
    public void init() {
        Intent checkTTSIntent=new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        activity.startActivityForResult(checkTTSIntent, INITIAL_CHECk);
        speech = SpeechRecognizer.createSpeechRecognizer(activity);
        Log.i(LOG_TAG, "isRecognitionAvailable: " + SpeechRecognizer.isRecognitionAvailable(activity));
        speech.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
    }

    @Override
    public void speak(String said) {
    }

    @Override
    public void listen() {
        Intent listenIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        listenIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                getClass().getPackage().getName());
        listenIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        listenIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS,10);
        activity.startActivityForResult(listenIntent, VR_REQUEST);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int error) {

    }

    @Override
    public void onResults(Bundle results) {

    }

    @Override
    public void onPartialResults(Bundle partialResults) {

    }

    @Override
    public void onEvent(int eventType, Bundle params) {

    }
}

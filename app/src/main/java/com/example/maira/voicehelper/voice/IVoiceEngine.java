package com.example.maira.voicehelper.voice;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.example.maira.voicehelper.contractviews.IGamesContractsView;

public interface IVoiceEngine {
    void init();
    void speak(String said);
    void listen();
    void setView(IGamesContractsView view);

}

package com.example.maira.voicehelper.modules;

import android.app.Activity;
import android.content.Context;

import com.example.maira.voicehelper.voice.IVoiceEngine;
import com.example.maira.voicehelper.voice.VoiceEngine;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class VoiceModule {

    @Provides
    IVoiceEngine provideVoiceEngine(){
        return new VoiceEngine();
    }
}

package com.example.maira.voicehelper.components;


import com.example.maira.voicehelper.MainActivity;
import com.example.maira.voicehelper.OneImageActivity;
import com.example.maira.voicehelper.QuartetActivity;
import com.example.maira.voicehelper.modules.AppModule;
import com.example.maira.voicehelper.modules.GameModule;
import com.example.maira.voicehelper.modules.RepositoryModule;
import com.example.maira.voicehelper.modules.StorageModule;
import com.example.maira.voicehelper.modules.VoiceModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by maira on 02.01.2019.
 */
@Singleton
@Component(modules = {AppModule.class, StorageModule.class, VoiceModule.class,
        RepositoryModule.class, GameModule.class})
public interface AppComponent {
    void injectsMainActivity(MainActivity mainActivity);
    void injectsOneImageActivity(OneImageActivity oneImageActivity);
    void injectsQuartetActivity(QuartetActivity activity);
}

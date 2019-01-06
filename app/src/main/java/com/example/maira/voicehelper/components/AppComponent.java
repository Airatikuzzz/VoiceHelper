package com.example.maira.voicehelper.components;

import com.example.maira.voicehelper.MainActivity;
import com.example.maira.voicehelper.modules.AppModule;
import com.example.maira.voicehelper.modules.DatabaseModule;
import com.example.maira.voicehelper.modules.NetworkModule;
import com.example.maira.voicehelper.modules.StorageModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by maira on 02.01.2019.
 */
@Singleton
@Component(modules = {AppModule.class, NetworkModule.class, DatabaseModule.class, StorageModule.class})
public interface AppComponent {
    void injectsMainActivity(MainActivity mainActivity);
}

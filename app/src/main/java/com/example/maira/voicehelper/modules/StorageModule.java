package com.example.maira.voicehelper.modules;


import com.example.maira.voicehelper.storage.StorageUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by maira on 02.01.2019.
 */
@Module
public class StorageModule {
    @Provides
    @Singleton
    StorageUtils provideStorageUtils(){
        return new StorageUtils();
    }
}

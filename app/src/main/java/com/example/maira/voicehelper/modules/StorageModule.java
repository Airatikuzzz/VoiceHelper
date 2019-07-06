package com.example.maira.voicehelper.modules;


import android.content.Context;

import com.example.maira.voicehelper.repository.Repository;
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
    StorageUtils provideStorageUtils(Repository repository, Context context){
        return new StorageUtils(repository, context);
    }
}

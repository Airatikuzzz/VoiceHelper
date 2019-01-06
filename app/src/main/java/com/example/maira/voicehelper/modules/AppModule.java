package com.example.maira.voicehelper.modules;

import android.content.Context;
import android.support.annotation.NonNull;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by maira on 02.01.2019.
 */
@Module
public class AppModule {

    Context mContext;

    public AppModule(@NonNull Context context){
        mContext = context;
    }
    @Provides
    @Singleton
    Context provideContext(){
        return mContext;
    }
}

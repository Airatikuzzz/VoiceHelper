package com.example.maira.voicehelper.modules;

import android.arch.persistence.room.Room;
import android.content.Context;

import com.example.maira.voicehelper.dataaccess.AppDatabase;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by maira on 02.01.2019.
 */

@Module
public class DatabaseModule {

    @Provides
    @Singleton
    AppDatabase provideDatabase(Context context){
        return Room.databaseBuilder(context, AppDatabase.class, "database").build();
    }
}

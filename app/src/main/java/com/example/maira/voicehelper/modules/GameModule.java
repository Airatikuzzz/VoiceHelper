package com.example.maira.voicehelper.modules;

import android.content.Context;

import com.example.maira.voicehelper.game.IOneImageGameEngine;
import com.example.maira.voicehelper.game.IQuartetGameEngine;
import com.example.maira.voicehelper.game.OneImageGameEngine;
import com.example.maira.voicehelper.game.QuartetGameEngine;
import com.example.maira.voicehelper.repository.Repository;
import com.example.maira.voicehelper.storage.StorageUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GameModule {

    @Provides
    IOneImageGameEngine provideGame(Repository repository, StorageUtils utils){
        return new OneImageGameEngine(repository, utils);
    }

    @Provides
    IQuartetGameEngine provideGameQ(Repository repository, StorageUtils utils){
        return new QuartetGameEngine(repository, utils);
    }
}

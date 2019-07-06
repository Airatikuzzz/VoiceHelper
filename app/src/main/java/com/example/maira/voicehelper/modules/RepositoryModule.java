package com.example.maira.voicehelper.modules;

import com.example.maira.voicehelper.repository.Repository;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RepositoryModule {

    @Provides
    @Singleton
    Repository provideRepository(){
        return new Repository();
    }

}

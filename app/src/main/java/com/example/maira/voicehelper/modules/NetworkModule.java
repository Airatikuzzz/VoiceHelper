package com.example.maira.voicehelper.modules;

import com.example.maira.voicehelper.network.NetworkUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by maira on 02.01.2019.
 */
@Module
public class NetworkModule {

    @Provides
    @Singleton
    NetworkUtils provideNetworkUtils(){
        return new NetworkUtils();
    }
}

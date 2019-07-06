package com.example.maira.voicehelper;

import android.app.Application;

import com.example.maira.voicehelper.components.AppComponent;
import com.example.maira.voicehelper.components.DaggerAppComponent;
import com.example.maira.voicehelper.modules.AppModule;

/**
 * Created by maira on 02.01.2019.
 */

public class App extends Application {
    private static AppComponent component;

   @Override
    public void onCreate() {
        super.onCreate();
        component = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }

    public static AppComponent getComponent() {
        return component;
    }
}

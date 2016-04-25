package ru.yandex.music.musicartists.database;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

public class DBApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FlowManager.init(this);
    }
}

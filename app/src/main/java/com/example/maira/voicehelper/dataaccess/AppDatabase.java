package com.example.maira.voicehelper.dataaccess;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by maira on 05.01.2019.
 */
@Database(entities = {}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

}

package com.swhackathon.bpass.db;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Visit.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract VisitDao visitDao();
}

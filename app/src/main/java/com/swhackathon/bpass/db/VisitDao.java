package com.swhackathon.bpass.db;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface VisitDao {
    @Query("SELECT * FROM Visit")
    List<Visit> getAll();

    @Insert
    void insert(Visit visit);

    @Update
    void update(Visit visit);

    @Delete
    void delete(Visit visit);
}

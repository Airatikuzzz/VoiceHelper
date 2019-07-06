package com.example.maira.voicehelper.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Category {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String Description;

    public Category() {
    }

    public Category(long id, String description) {
        this.id = id;
        Description = description;
    }
}

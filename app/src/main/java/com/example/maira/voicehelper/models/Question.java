package com.example.maira.voicehelper.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

/**
 * Created by maira on 13.01.2019.
 */
@Entity(foreignKeys = @ForeignKey(entity = Category.class, parentColumns = "id", childColumns = "CategoryID"))
public class Question {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String text;
    public long CategoryID;

    public Question() {
    }

    public Question(long id, String text, long categoryID) {
        this.id = id;
        this.text = text;
        CategoryID = categoryID;
    }
}

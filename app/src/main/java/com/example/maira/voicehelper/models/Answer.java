package com.example.maira.voicehelper.models;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Created by maira on 13.01.2019.
 */
@Entity(foreignKeys = {@ForeignKey(entity = Question.class, parentColumns = "id", childColumns = "questionId"),
                        @ForeignKey(entity = Category.class, parentColumns = "id", childColumns = "CategoryID")})
public class Answer {
    @PrimaryKey(autoGenerate = true)
    public long id;;
    public String text;
    public long questionId;
    public long CategoryID;
    public String Image;

    public Answer() {
    }

    public Answer(long id, String text, long questionId, long categoryID, String image) {
        this.id = id;
        this.text = text;
        this.questionId = questionId;
        CategoryID = categoryID;
        Image = image;
    }
}


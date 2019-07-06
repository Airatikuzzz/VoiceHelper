package com.example.maira.voicehelper.models;

public class Animal {
    public int id;
    public String Description;
    public long CategoryID;
    public long QuestionID;

    public Animal(int id, String description, long categoryID, long questionID, String image) {
        this.id = id;
        Description = description;
        CategoryID = categoryID;
        QuestionID = questionID;
        Image = image;
    }

    public Animal() {
    }

    public String Image;
}

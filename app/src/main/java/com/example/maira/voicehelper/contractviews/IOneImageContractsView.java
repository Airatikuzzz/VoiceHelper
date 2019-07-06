package com.example.maira.voicehelper.contractviews;

import com.example.maira.voicehelper.models.Animal;

public interface IOneImageContractsView extends IGamesContractsView{
    void setCurrent(Animal animal);
    void speak(String text);
    void setTitleOfChips(String[] chips);
}

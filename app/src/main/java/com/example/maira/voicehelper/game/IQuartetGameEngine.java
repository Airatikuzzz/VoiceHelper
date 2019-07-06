package com.example.maira.voicehelper.game;

import com.example.maira.voicehelper.models.Animal;

import java.util.List;

public interface IQuartetGameEngine extends IGameEngine {
    List<Animal> getNextPortionAnimals(int from, int to);
    void updateAnimals(List<Animal> animals);
    void clickImage(int position);
}

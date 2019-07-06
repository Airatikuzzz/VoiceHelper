package com.example.maira.voicehelper.contractviews;

import com.example.maira.voicehelper.models.Animal;

import java.util.List;

public interface IQuartetContractsView extends IGamesContractsView{
    void updateImagesGrid(List<Animal> animalList);
}

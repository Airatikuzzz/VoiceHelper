package com.example.maira.voicehelper.game;

import com.example.maira.voicehelper.contractviews.IGamesContractsView;
import com.example.maira.voicehelper.models.Animal;
import com.google.firebase.storage.StorageReference;

public interface IGameEngine {
    void next();
    void answerFromUser(String answer);
    StorageReference getImageReferenceFor(Animal animal);
    void startGame();
    void setView(IGamesContractsView view_);
    void preStart();
    void onStop();
}

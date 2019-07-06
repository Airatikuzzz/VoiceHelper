package com.example.maira.voicehelper.game;

import android.app.Activity;

import com.example.maira.voicehelper.contractviews.IGamesContractsView;
import com.example.maira.voicehelper.contractviews.IQuartetContractsView;
import com.example.maira.voicehelper.models.Animal;
import com.example.maira.voicehelper.repository.Repository;
import com.example.maira.voicehelper.storage.StorageUtils;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public class QuartetGameEngine implements IQuartetGameEngine {

    @Inject
    Repository rep;
    @Inject
    StorageUtils storageUtils;

    Animal currentAnimal;
    List<Animal> animals=new ArrayList<>();
    List<Animal> currentPortionAnimals=new ArrayList<>();
    boolean isGameStarted =false;
    boolean isDataDownloaded = false;
    boolean isProgressVisible = false;
    boolean isFirstStart = true;

    int startFrom=0, startTo=4,currentFrom=startFrom, currentTo=startTo;
    IQuartetContractsView view;

    @Inject
    public QuartetGameEngine(Repository repository, StorageUtils storageUtils_){
        rep = repository;
        storageUtils = storageUtils_;
    }

    public void startGame() {
        view.hideProgress();
        updateAnimals(getNextPortionAnimals(startFrom, startTo));
    }

    @Override
    public void setView(IGamesContractsView view_) {
        view = (IQuartetContractsView) view_;
    }

    @Override
    public void preStart() {
        rep.getAnimals()
                .subscribeWith(new DisposableObserver<Animal>() {
                    @Override public void onNext(Animal animal) {
                        animals.add(animal);
                        if (animals.size()>=4 && !isGameStarted) {
                            startGame();
                            isGameStarted = true;
                        }
                    }
                    @Override public void onComplete() {
                        isDataDownloaded = true;
                    }
                    @Override public void onError(Throwable t) { /* crash or show */ }
                });
        view.showProgress();
    }

    @Override
    public List<Animal> getNextPortionAnimals(int from, int to) {
        return animals.subList(from, to);
    }

    @Override
    public void updateAnimals(List<Animal> animals) {
        currentPortionAnimals = new ArrayList<>(animals);
        List<Animal> animals_ = new ArrayList<>(animals);
        view.updateImagesGrid(animals_);
        Random rnd = new Random();
        setupCurrentAnimal(animals_.get(rnd.nextInt(animals_.size())));

    }

    public void setupCurrentAnimal(Animal animal){
        currentAnimal = animal;
        if(storageUtils.AnimalVoiceExists(currentAnimal)) {
            storageUtils.playAnimalVoice(currentAnimal);
        }
        else{
            view.showProgressDialog();
            downloadVoices(animals);
        }
    }

    @Override
    public void clickImage(int position) {
        if(currentAnimal == currentPortionAnimals.get(position)) {
            next();
        }
    }

    @Override
    public void onStop() {
        isGameStarted = false;
        isDataDownloaded = false;
        storageUtils.stopPlaying();
    }

    @Override
    public void next() {
        if(isDataDownloaded){
            Random rnd = new Random();
            List<Animal> s = new ArrayList<>(animals);
            List<Animal> result = new ArrayList<>();
            for (int i=0;i<4;i++){
                int randomIndex = rnd.nextInt(s.size());
                result.add(s.remove(randomIndex));
            }
            updateAnimals(result);
        }
        else{
            currentFrom+=4;
            currentTo+=4;
            updateAnimals(getNextPortionAnimals(currentFrom, currentTo));
        }

    }

    public void downloadVoices(List<Animal> animalList){
        for (Animal animal: animalList) {
            if(storageUtils.AnimalVoiceExists(animal))
                continue;
            storageUtils.downloadMp3(animal).subscribeWith(new DisposableObserver<Animal>() {
                @Override
                public void onNext(Animal animal) {
                    if(animal==currentAnimal){
                        setupCurrentAnimal(currentAnimal);
                        view.hideProgressDialog();
                    }
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            });
        }
    }
    @Override
    public void answerFromUser(String answer) {

    }

    @Override
    public StorageReference getImageReferenceFor(Animal animal) {
        return rep.getImagePathFor(animal);
    }
}

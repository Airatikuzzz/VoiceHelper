package com.example.maira.voicehelper.game;

import com.example.maira.voicehelper.OneImageActivity;
import com.example.maira.voicehelper.contractviews.IGamesContractsView;
import com.example.maira.voicehelper.contractviews.IOneImageContractsView;
import com.example.maira.voicehelper.models.Animal;
import com.example.maira.voicehelper.repository.Repository;
import com.example.maira.voicehelper.storage.StorageUtils;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import io.reactivex.observers.DisposableObserver;

public class OneImageGameEngine implements IOneImageGameEngine{

    Repository rep;

    List<Animal> animals=new ArrayList<>();
    Animal[] currentAnimals=new Animal[2];

    int currentIndex;
    boolean isGameStarted = false;
    Animal currentAnimal;

    IOneImageContractsView view;
    StorageUtils storageUtils;

    @Inject
    public OneImageGameEngine(Repository repository, StorageUtils storageUtils_){
        rep = repository;
        storageUtils = storageUtils_;
    }
    public void setView(IGamesContractsView view_){
        view = (IOneImageContractsView) view_;
    }

    @Override
    public void preStart() {
        rep.getAnimals()
                .subscribeWith(new DisposableObserver<Animal>() {
                    @Override public void onNext(Animal animal) {
                        animals.add(animal);
                        currentAnimal = animals.get(currentIndex);
                        if (!isGameStarted) {
                            startGame();
                            isGameStarted = true;
                            currentIndex = 0;
                        }
                    }
                    @Override public void onComplete() {
                        GenerateChips();
                    }
                    @Override public void onError(Throwable t) { /* crash or show */ }
                });
    }

    @Override
    public void onStop() {
        isGameStarted = false;
        storageUtils.stopPlaying();
    }

    public StorageReference getImageReferenceFor(Animal animal){
        return rep.getImagePathFor(animal);
    }

    public void startGame(){
        view.hideProgress();
        view.setCurrent(currentAnimal);
        view.speak("Угадай животного по фотографии");
    }

    public void GenerateChips(){
        List<Animal> s = new ArrayList<>(animals);
        s.remove(currentAnimal);
        Random r = new Random();
        int trueAnswer = r.nextInt(2);
        for(int i=0;i<2;i++){
            if(i==trueAnswer) {
                currentAnimals[i] = currentAnimal;
                continue;
            }
            int index = r.nextInt(s.size());
            currentAnimals[i] = s.remove(index);
        }
        view.setTitleOfChips(new String[]{currentAnimals[0].Description, currentAnimals[1].Description});
    }
    @Override
    public void next() {
        if(currentIndex==animals.size()-1)
            currentIndex=0;
        currentAnimal = animals.get(++currentIndex);
        view.setCurrent(currentAnimal);
        GenerateChips();
        storageUtils.stopPlaying();
    }

    @Override
    public void answerFromUser(String answer) {
        if(answer.equalsIgnoreCase(currentAnimal.Description)){
            view.speak("Правильно");
            next();
        }
        else{
            view.speak("Попробуй ответить еще раз");
        }
    }

    @Override
    public void onClickVoiceAnimal(String animalDescr) {
        Animal animal = null;
        for (Animal an: animals) {
            if(animalDescr.equalsIgnoreCase(an.Description))
                animal = an;
        }
        if(storageUtils.AnimalVoiceExists(animal)) {
            storageUtils.playAnimalVoice(animal);
        }
        else{
            view.showProgressDialog();
            downloadVoices(currentAnimals, animal);
        }
    }

    public void downloadVoices(Animal[] animalList, Animal animal){
        for (Animal animald: animalList) {
            if(storageUtils.AnimalVoiceExists(animald))
                continue;
            storageUtils.downloadMp3(animald).subscribeWith(new DisposableObserver<Animal>() {
                @Override
                public void onNext(Animal animal_) {
                    if(animal_==animal){
                        onClickVoiceAnimal(animal.Description);
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
}

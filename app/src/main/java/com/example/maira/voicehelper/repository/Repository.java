package com.example.maira.voicehelper.repository;

import android.content.Context;
import android.util.Log;

import com.example.maira.voicehelper.App;
import com.example.maira.voicehelper.components.DaggerAppComponent;
import com.example.maira.voicehelper.models.Animal;
import com.example.maira.voicehelper.modules.AppModule;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class Repository implements IRepository {

    FirebaseStorage storage;
    FirebaseFirestore db;

    public Repository(){
        storage = FirebaseStorage.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    public Observable<Animal> getAnimals() {
        return Observable.create(source ->
                db.collection("animals").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Animal> list = new ArrayList<>();
                        for (DocumentSnapshot document : task.getResult()) {
                            Animal animal = document.toObject(Animal.class);
                            source.onNext(animal);
                        }
                        source.onComplete();
                    }
                })
        );
    }

    public StorageReference getImagePathFor(Animal animal){
        return storage.getReference().child(String.format("animals/%s.png", animal.Image));
    }

}

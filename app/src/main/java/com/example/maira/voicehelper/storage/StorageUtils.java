package com.example.maira.voicehelper.storage;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

import com.example.maira.voicehelper.models.Animal;
import com.example.maira.voicehelper.repository.Repository;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import io.reactivex.Observable;
import io.reactivex.observers.DisposableObserver;

/**
 * Created by maira on 02.01.2019.
 */

public class StorageUtils {
    FirebaseStorage storage;

    Context mContext;

    Repository rep;

    MediaPlayer mediaPlayer;

    boolean isDownloaded = false;

    @Inject
    public StorageUtils(Repository repository, Context context){
        mContext = context;
        rep = repository;
        storage = FirebaseStorage.getInstance();
        mediaPlayer = new MediaPlayer();
    }

    public void playAnimalVoice(Animal animal) {
        stopPlaying();
        try {
            File path = new File(mContext.getFilesDir(), animal.Image + ".mp3");
            if(!path.exists())
                return;
            mediaPlayer.setDataSource(path.getPath());

            mediaPlayer.prepare();
            mediaPlayer.start();
        }
        catch (IOException ex) {
            String s = ex.toString();
            ex.printStackTrace();
        }
    }

    public void stopPlaying(){
        if(mediaPlayer.isPlaying())
            mediaPlayer.stop();
        mediaPlayer.reset();
    }


    public Observable<Animal> downloadMp3(Animal animal) {
        return Observable.create(source ->
                storage.getReference()
                        .child(String.format("animal_voices/%s.mp3", animal.Image))
                        .getBytes(1024 * 1024)
                        .addOnSuccessListener(bytes -> {
                            try {
                                FileOutputStream fos = mContext.openFileOutput(animal.Image + ".mp3", Context.MODE_PRIVATE);
                                fos.write(bytes);
                                fos.close();
                                source.onNext(animal);
                            } catch (IOException ex) {
                                String s = ex.toString();
                                ex.printStackTrace();
                            }
                        })
                        .addOnFailureListener(exception -> {
                            // Handle any errors
                            Log.d("kek", exception.getMessage());

                        }));
    }

    public boolean AnimalVoiceExists(Animal animal){
        try {
            File file = new File(mContext.getFilesDir(), animal.Image + ".mp3");
            if (file.exists())
                return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }



}

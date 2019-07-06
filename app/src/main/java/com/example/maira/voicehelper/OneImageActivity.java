package com.example.maira.voicehelper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.example.maira.voicehelper.components.DaggerAppComponent;
import com.example.maira.voicehelper.contractviews.IOneImageContractsView;
import com.example.maira.voicehelper.game.IOneImageGameEngine;
import com.example.maira.voicehelper.game.OneImageGameEngine;
import com.example.maira.voicehelper.models.Animal;
import com.example.maira.voicehelper.modules.AppModule;
import com.example.maira.voicehelper.repository.Repository;
import com.google.android.material.chip.Chip;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.maira.voicehelper.voice.IVoiceEngine;

import java.util.ArrayList;
import java.util.Locale;

import javax.inject.Inject;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OneImageActivity extends AppCompatActivity implements IOneImageContractsView, TextToSpeech.OnInitListener {

    @BindView(R.id.one_activity_image_view)
    ImageView oneImageView;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.fab_next)
    FloatingActionButton fab_next;
    @BindView(R.id.chip1)
    Chip chip1;
    @BindView(R.id.chip2)
    Chip chip2;
    @BindView(R.id.content_one_image)
    LinearLayoutCompat layout;
    ProgressDialog dialog;


    @Inject
    IVoiceEngine voiceEngine;
    @Inject
    IOneImageGameEngine game;

    private static final int VR_REQUEST = 999;
    private static final int MY_DATA_CHECK_CODE = 9991;

    private final String LOG_TAG = "SpeechRepeatActivity";

    private String said = "";
    private TextToSpeech repeatTTS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_image);
        App.getComponent().injectsOneImageActivity(this);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ButterKnife.bind(this);

        chip1.setOnCloseIconClickListener(v->
                game.onClickVoiceAnimal(chip1.getText().toString())
        );
        chip2.setOnCloseIconClickListener(v->
                game.onClickVoiceAnimal(chip2.getText().toString())
        );
        voiceEngine.setView(this);
        voiceEngine.init();
        game.setView(this);
        game.preStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VR_REQUEST && resultCode == RESULT_OK) {
            ArrayList<String> suggestedWords =
                    data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            said = suggestedWords.get(0);
            game.answerFromUser(said);
            Log.d(LOG_TAG, suggestedWords.toString());
        }
        if(requestCode== MY_DATA_CHECK_CODE)
        {
            if(resultCode== TextToSpeech.Engine.CHECK_VOICE_DATA_PASS)
                repeatTTS=new TextToSpeech(this, this);
            else
            {
                Intent installTTSIntent=new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick(R.id.fab)
    public void onClick() {
        voiceEngine.listen();
    }

    @Override
    public void setCurrent(Animal animal) {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(this);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        DrawableCrossFadeFactory factory =
                new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
        Glide.with(this)
                .load(game.getImageReferenceFor(animal))
                .placeholder(circularProgressDrawable)
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .into(oneImageView);
    }

    @Override
    public void speak(String text) {
        if(repeatTTS!=null)
            repeatTTS.speak(text,TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    public void showProgress() {
    }

    @Override
    public void hideProgress() {
        layout.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgressDialog() {
        dialog = new ProgressDialog(this);
        dialog.setTitle("Пожалуйста, подождите");
        dialog.setMessage("Загрузка аудиозвука...");
        dialog.setCancelable(false);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setIndeterminate(false);
        if(!dialog.isShowing())
            dialog.show();
    }

    @Override
    public void hideProgressDialog() {
        if(dialog.isShowing())
            dialog.dismiss();
    }


    @Override
    protected void onStop() {
        super.onStop();
        game.onStop();
    }

    @Override
    public void setTitleOfChips(String[] chips) {
        chip1.setText(chips[0]);
        chip2.setText(chips[1]);
    }
    @OnClick({R.id.chip1,R.id.chip2})
    public void OnChipClick(Chip chip){
        game.answerFromUser(chip.getText().toString());
    }

    @OnClick(R.id.fab_next)
    public void OnNext(){
        game.next();
    }

    @Override
    public void onInit(int i) {
        if(i== TextToSpeech.SUCCESS)
            repeatTTS.setLanguage(Locale.getDefault());//Язык
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

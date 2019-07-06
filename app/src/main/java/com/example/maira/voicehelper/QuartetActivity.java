package com.example.maira.voicehelper;

import android.app.ProgressDialog;
import android.os.Bundle;

import com.example.maira.voicehelper.adapters.QuartetAdapter;
import com.example.maira.voicehelper.contractviews.IQuartetContractsView;
import com.example.maira.voicehelper.game.IGameEngine;
import com.example.maira.voicehelper.game.IQuartetGameEngine;
import com.example.maira.voicehelper.game.QuartetGameEngine;
import com.example.maira.voicehelper.models.Animal;
import com.example.maira.voicehelper.repository.Repository;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.RelativeLayout;

import java.util.List;

import javax.inject.Inject;

public class QuartetActivity extends AppCompatActivity implements IQuartetContractsView {

    @BindView(R.id.fab_refresh)
    FloatingActionButton fabRefresh;
    @BindView(R.id.grid_view)
    GridView gridView;
    @BindView(R.id.rel)
    RelativeLayout progress;

    ProgressDialog dialog;
    QuartetAdapter gridAdapter;

    @Inject
    IQuartetGameEngine gameEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quartet);
        ButterKnife.bind(this);
        App.getComponent().injectsQuartetActivity(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gameEngine.setView(this);
        gameEngine.preStart();

        gridAdapter = new QuartetAdapter(this, gameEngine);

    }

    @Override
    public void showProgress() {
        progress.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progress.setVisibility(View.INVISIBLE);
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
    public void updateImagesGrid(List<Animal> animalList) {
        if(gridView.getAdapter()==null){
            gridAdapter.setAnimals(animalList);
            gridView.setAdapter(gridAdapter);
            return;
        }
        gridAdapter.setAnimals(animalList);
        gridAdapter.notifyDataSetChanged();
    }

    @OnClick(R.id.fab_refresh)
    public void fabClick(){
        gameEngine.next();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        gameEngine.onStop();
    }
}

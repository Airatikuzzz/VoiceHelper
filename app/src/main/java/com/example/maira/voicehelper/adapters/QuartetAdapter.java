package com.example.maira.voicehelper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.maira.voicehelper.R;
import com.example.maira.voicehelper.game.IGameEngine;
import com.example.maira.voicehelper.game.IQuartetGameEngine;
import com.example.maira.voicehelper.models.Animal;

import java.util.ArrayList;
import java.util.List;

import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

public class QuartetAdapter extends BaseAdapter {
    private Context context;
    private IQuartetGameEngine gameEngine;

    private List<Animal> animals = new ArrayList<>();

    public QuartetAdapter(Context context, IQuartetGameEngine gameEngine) {
        this.context = context;
        this.gameEngine = gameEngine;
    }

    public void setAnimals(List<Animal> animals_){
        animals = animals_;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView = inflater.inflate(R.layout.quartet_part_card, null);
        ImageView imageView = convertView
                .findViewById(R.id.quartet_grid_image);
        imageView.setOnClickListener(view->{
            gameEngine.clickImage(position);
        });
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setCenterRadius(30f);
        circularProgressDrawable.start();

        Glide.with(context)
                .load(gameEngine.getImageReferenceFor(animals.get(position)))
                .placeholder(circularProgressDrawable)
                .into(imageView);

        return convertView;
    }

    @Override
    public int getCount() {
        return animals.size();
    }

    @Override
    public Object getItem(int position) {
        return animals.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}

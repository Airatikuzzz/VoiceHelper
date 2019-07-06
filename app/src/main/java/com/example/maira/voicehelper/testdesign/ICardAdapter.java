package com.example.maira.voicehelper.testdesign;

/**
 * Created by maira on 05.01.2019.
 */
import androidx.cardview.widget.CardView;

public interface ICardAdapter {

    int MAX_ELEVATION_FACTOR = 8;

    float getBaseElevation();

    CardView getCardViewAt(int position);

    int getCount();
}


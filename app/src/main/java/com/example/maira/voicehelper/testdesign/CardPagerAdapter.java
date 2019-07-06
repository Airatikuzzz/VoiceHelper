package com.example.maira.voicehelper.testdesign;

/**
 * Created by maira on 05.01.2019.
 */
import androidx.viewpager.widget.PagerAdapter;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maira.voicehelper.OneImageActivity;
import com.example.maira.voicehelper.QuartetActivity;
import com.example.maira.voicehelper.R;

import java.util.ArrayList;
import java.util.List;

public class CardPagerAdapter extends PagerAdapter implements ICardAdapter {

    private static final int ONE_IMAGE_GAME_ACTIVITY=0;
    private static final int QUARTET_GAME_ACTIVITY=1;
    private List<CardView> mViews;
    private List<CardItem> mData;
    private float mBaseElevation;

    public CardPagerAdapter() {
        mData = new ArrayList<>();
        mViews = new ArrayList<>();
    }

    public void addCardItem(CardItem item) {
        mViews.add(null);
        mData.add(item);
    }

    public float getBaseElevation() {
        return mBaseElevation;
    }

    @Override
    public CardView getCardViewAt(int position) {
        return mViews.get(position);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext())
                .inflate(R.layout.adapter, container, false);
        container.addView(view);
        bind(container.getContext(), mData.get(position), view, position);
        CardView cardView = (CardView) view.findViewById(R.id.cardView);

        if (mBaseElevation == 0) {
            mBaseElevation = cardView.getCardElevation();
        }

        cardView.setMaxCardElevation(mBaseElevation * MAX_ELEVATION_FACTOR);
        mViews.set(position, cardView);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        mViews.set(position, null);
    }

    private void bind(Context context, CardItem item, View view, int position) {
        TextView titleTextView = view.findViewById(R.id.titleTextView);
        TextView contentTextView =  view.findViewById(R.id.contentTextView);
        ImageView imageView = view.findViewById(R.id.iconImgView);
        Button btn = view.findViewById(R.id.btn_start);
        btn.setTag(position);
        btn.setOnClickListener(v ->  {
                if(Integer.parseInt(v.getTag().toString())==ONE_IMAGE_GAME_ACTIVITY) {
                    Intent intent = new Intent(context, OneImageActivity.class);
                    context.startActivity(intent);
                }
                else if(Integer.parseInt(v.getTag().toString())==QUARTET_GAME_ACTIVITY){
                    Intent intent = new Intent(context, QuartetActivity.class);
                    context.startActivity(intent);
                }
            }
        );
        imageView.setImageResource(item.getIcon());
        titleTextView.setText(item.getTitle());
        contentTextView.setText(item.getText());
    }

}
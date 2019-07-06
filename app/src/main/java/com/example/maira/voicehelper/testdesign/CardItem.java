package com.example.maira.voicehelper.testdesign;

/**
 * Created by maira on 05.01.2019.
 */

public class CardItem {

    private int mTextResource;
    private int mTitleResource;
    private int mIconResource;

    public CardItem(int title, int text, int icon) {
        mTitleResource = title;
        mTextResource = text;
        mIconResource = icon;
    }

    public int getText() {
        return mTextResource;
    }

    public int getTitle() {
        return mTitleResource;
    }
    public int getIcon() {
        return mIconResource;
    }
}

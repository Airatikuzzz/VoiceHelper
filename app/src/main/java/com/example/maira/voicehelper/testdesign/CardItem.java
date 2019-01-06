package com.example.maira.voicehelper.testdesign;

/**
 * Created by maira on 05.01.2019.
 */

public class CardItem {

    private int mTextResource;
    private int mTitleResource;

    public CardItem(int title, int text) {
        mTitleResource = title;
        mTextResource = text;
    }

    public int getText() {
        return mTextResource;
    }

    public int getTitle() {
        return mTitleResource;
    }
}

package com.example.maira.voicehelper;

import android.content.Intent;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NavUtils;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.viewpager.widget.ViewPager;
import android.view.MenuItem;
import android.widget.Button;

import com.example.maira.voicehelper.testdesign.CardItem;
import com.example.maira.voicehelper.testdesign.CardPagerAdapter;
import com.example.maira.voicehelper.testdesign.ShadowTransformer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TestActivity extends AppCompatActivity  {

    private ViewPager mViewPager;

    private CardPagerAdapter mCardAdapter;
    private ShadowTransformer mCardShadowTransformer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mViewPager = findViewById(R.id.viewPager);

        mCardAdapter = new CardPagerAdapter();
        mCardAdapter.addCardItem(new CardItem(R.string.title_1, R.string.text1,R.drawable.ic_cat));
        mCardAdapter.addCardItem(new CardItem(R.string.title_2, R.string.text2, R.drawable.ic_library_music));
        //mCardAdapter.addCardItem(new CardItem(R.string.title_3, R.string.text_1));
        //mCardAdapter.addCardItem(new CardItem(R.string.title_4, R.string.text_1));

        mCardShadowTransformer = new ShadowTransformer(mViewPager, mCardAdapter);
        mCardShadowTransformer.enableScaling(true);

        mViewPager.setAdapter(mCardAdapter);
        mViewPager.setPageTransformer(false, mCardShadowTransformer);
        mViewPager.setOffscreenPageLimit(3);
    }
    @Override
    public void onBackPressed() {
        if(mViewPager.getCurrentItem()>0) {
            mViewPager.setCurrentItem(0, true);
        } else {
            super.onBackPressed(); // This will pop the Activity from the stack.
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}

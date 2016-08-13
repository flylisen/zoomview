package com.lisen.android.tupianchulidemo.activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.lisen.android.tupianchulidemo.R;
import com.lisen.android.tupianchulidemo.view.MyImageView;

public class MainActivity extends AppCompatActivity {


    private ViewPager mContainer;
    private int[] mImages = new int[] {R.drawable.key,
    R.drawable.tree, R.drawable.girl, R.drawable.flower};
    private ImageView[] mImageViews = new ImageView[mImages.length];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContainer = (ViewPager) findViewById(R.id.vp_main_activity);
        mContainer.setAdapter(new PagerAdapter() {
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                MyImageView myImageView = new MyImageView(getApplicationContext());
                myImageView.setImageResource(mImages[position]);
                mContainer.addView(myImageView);
                mImageViews[position] = myImageView;
                return myImageView;
            }

            @Override
            public int getCount() {
                return mImageViews.length;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                mContainer.removeView(mImageViews[position]);
            }
        });
    }
}

package com.app.manojapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.manojapp.customcomponents.ParallaxPageTransformer;
import com.app.manojapp.customcomponents.ParallaxPageTransformer.ParallaxTransformInformation;

import java.util.ArrayList;


public class SelectCat extends Activity {
    TextView dustbin,toilet;
    Context context;
    ViewPager viewpager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.select_cat);
        EditText editText = (EditText) findViewById(R.id.search_bar);
        toilet = (TextView) findViewById(R.id.toilet);
        dustbin  = (TextView) findViewById(R.id.dustbin);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        context=this;
        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,SearchLocality.class);
                i.putExtra("selected",viewpager.getCurrentItem());
                startActivity(i);
                finish();
            }
        });
        toilet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dustbin.setTypeface(null, Typeface.NORMAL);
                toilet.setTypeface(null, Typeface.BOLD);
                if(viewpager.getCurrentItem()!=0)
                {
                    viewpager.setCurrentItem(0);
                }
            }
        });
        dustbin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toilet.setTypeface(null, Typeface.NORMAL);
                dustbin.setTypeface(null, Typeface.BOLD);
                if(viewpager.getCurrentItem()!=1)
                {
                    viewpager.setCurrentItem(1);
                }
            }
        });
        ArrayList<String> list_item=new ArrayList<>();
        list_item.add("1");
        list_item.add("2");
        PhotoLowerAdapter photo = new PhotoLowerAdapter(context,list_item);
        viewpager.setAdapter(photo);
        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        dustbin.setTypeface(null, Typeface.NORMAL);
                        toilet.setTypeface(null, Typeface.BOLD);
                        break;
                    case 1:
                        toilet.setTypeface(null, Typeface.NORMAL);
                        dustbin.setTypeface(null, Typeface.BOLD);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        /*ParallaxPageTransformer pageTransformer = new ParallaxPageTransformer()
                .addViewToParallax(new ParallaxTransformInformation(R.id.pic, 2, 2))
                .addViewToParallax(new ParallaxTransformInformation(R.id.pic, -0.65f,ParallaxTransformInformation.PARALLAX_EFFECT_DEFAULT));
        */
        ParallaxPageTransformer pageTransformer = new ParallaxPageTransformer()
                .addViewToParallax(new ParallaxTransformInformation(R.id.pic, 2, 2))
                .addViewToParallax(new ParallaxTransformInformation(R.id.pic, 2,2));

        viewpager.setPageTransformer(true, pageTransformer);

    }


    private class PhotoLowerAdapter extends PagerAdapter {
        Context mContext;
        ArrayList<String> mResources =null;

        public PhotoLowerAdapter(Context context,ArrayList<String> resultp) {
            mContext = context;
            this.mResources = resultp;
        }

        @Override
        public Object instantiateItem(ViewGroup container,int position) {
            View page=getLayoutInflater().inflate(R.layout.viewpager_image, container, false);
            ImageView imageView = (ImageView) page.findViewById(R.id.pic);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            switch (position)
                    {
                        case 0 : imageView.setImageResource(R.drawable.toilet);
                            break;
                        case 1 : imageView.setImageResource(R.drawable.dustbin);
                            break;
                    }
            container.addView(page);
            return(page);
        }

        @Override
        public void destroyItem(ViewGroup container, int position,
                                Object object) {
            container.removeView((View)object);
        }

        @Override
        public int getCount() {
            return mResources.size();
        }

        @Override
        public float getPageWidth(int position) {
            return(1f);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return(view == object);
        }
    }
}

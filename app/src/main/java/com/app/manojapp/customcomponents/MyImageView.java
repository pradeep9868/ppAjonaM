package com.app.manojapp.customcomponents;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.*;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;

public class MyImageView extends NetworkImageView {

    private static final int FADE_IN_TIME_MS = 250;
    private static ColorDrawable cd;

    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        cd = new ColorDrawable(getResources().getColor(android.R.color.transparent));
        TransitionDrawable td = new TransitionDrawable(new Drawable[]{
                cd,
                new BitmapDrawable(getContext().getResources(), bm)
        });

        setImageDrawable(td);
        td.startTransition(FADE_IN_TIME_MS);
    }
}
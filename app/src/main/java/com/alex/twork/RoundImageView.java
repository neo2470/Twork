package com.alex.twork;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;

import com.alex.graphics.CircleDrawable;
import com.alex.graphics.RoundDrawable;

/**
 * Created by alex on 15-11-16.
 * 圆角的ImageView
 */
public class RoundImageView extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.round_imageview);

        ImageView imgViewRound = (ImageView) findViewById(R.id.imgViewRound);
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        RoundDrawable drawable1 = new RoundDrawable(bitmap1);
        drawable1.setRound(50);
        imgViewRound.setImageDrawable(drawable1);

        ImageView imgViewCircle = (ImageView) findViewById(R.id.imgViewCircle);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        Drawable drawable2 = new CircleDrawable(bitmap2);
        imgViewCircle.setImageDrawable(drawable2);
    }
}

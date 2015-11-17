package com.alex.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alex.graphics.CircleDrawable;
import com.alex.graphics.RoundDrawable;
import com.alex.twork.R;

/**
 * Created by alex on 15-11-17.
 * Demo : Round Image & Circle Image
 */
public class RoundImageFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.round_image_fragment, container, false);

        ImageView imgViewRound = (ImageView) view.findViewById(R.id.imgViewRound);
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        RoundDrawable drawable1 = new RoundDrawable(bitmap1);
        drawable1.setRound(50);
        imgViewRound.setImageDrawable(drawable1);

        ImageView imgViewCircle = (ImageView) view.findViewById(R.id.imgViewCircle);
        Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        Drawable drawable2 = new CircleDrawable(bitmap2);
        imgViewCircle.setImageDrawable(drawable2);

        return view;
    }
}

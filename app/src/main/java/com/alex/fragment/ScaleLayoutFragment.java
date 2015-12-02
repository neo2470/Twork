package com.alex.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.alex.graphics.CircleDrawable;
import com.alex.twork.R;
import com.alex.util.DisplayUtil;
import com.alex.widget.ScalableSeekBar;

import org.w3c.dom.Text;

/**
 * Created by alex on 15-11-18.
 * Scale layout to support different devices
 */
public class ScaleLayoutFragment extends BaseFragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scale_layout_fragment, container, false);

        Button scaleBtn1 = (Button) view.findViewById(R.id.scaleBtn1);
        Button scaleBtn2 = (Button) view.findViewById(R.id.scaleBtn2);
        ToggleButton scaleBtn5 = (ToggleButton) view.findViewById(R.id.scaleBtn5);
        scaleBtn1.setOnClickListener(this);
        scaleBtn2.setOnClickListener(this);
        scaleBtn5.setOnCheckedChangeListener(this);

        ImageView scaleImageView = (ImageView) view.findViewById(R.id.scaleImageView);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
        CircleDrawable drawable = new CircleDrawable(bitmap);
        scaleImageView.setImageDrawable(drawable);

        final TextView scalableSeekBarProgress = (TextView) view.findViewById(R.id.scalableSeekBarProgress);
        ScalableSeekBar scalableSeekBar = (ScalableSeekBar) view.findViewById(R.id.scalableSeekBar);
        scalableSeekBar.setOnScalableSeekBarChangeListener(new ScalableSeekBar.OnScalableSeekBarChangeListener() {
            @Override
            public void onProgressChanged(ScalableSeekBar scalableSeekBar, int progress) {
                scalableSeekBarProgress.setText(progress+"");
            }
        });

        DisplayUtil.scaleView(view, R.id.mainLayout);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.scaleBtn1: {
                getActivity().onBackPressed();
                break;
            }
            case R.id.scaleBtn2: {
                Toast.makeText(getActivity(), R.string.scale_btn_clicked, Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (R.id.scaleBtn5 == buttonView.getId()) {
            int resId;
            if (isChecked) {
                resId = R.string.scale_btn_checked_true;
            } else {
                resId = R.string.scale_btn_checked_false;
            }
            Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
        }
    }
}

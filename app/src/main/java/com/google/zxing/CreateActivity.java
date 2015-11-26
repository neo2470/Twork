package com.google.zxing;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.alex.twork.R;

/**
 * Created by alex on 15-11-26.
 * An activity to generate a QR code Image
 */
public class CreateActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_activity);

        final EditText content = (EditText) findViewById(R.id.content);
        final ImageView qrImg = (ImageView) findViewById(R.id.qrImg);
        Button genBtn = (Button) findViewById(R.id.genQRBtn);
        genBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

            }
        });
    }
}

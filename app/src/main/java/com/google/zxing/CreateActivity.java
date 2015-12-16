package com.google.zxing;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alex.twork.R;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

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
                final String data = content.getText().toString();
                Bitmap bitmap = createQRBitmap(data, 512, 512);

                if(null != bitmap) {
                    qrImg.setImageBitmap(bitmap);
                } else {
                    Toast.makeText(CreateActivity.this, R.string.qr_create_failure, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Bitmap createQRBitmap(String data, int width, int height) {

        if(null == data || "".equals(data) || data.length() < 1) {
            return null;
        }

        Hashtable<EncodeHintType, String> hints = new Hashtable<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        Bitmap bitmap = null;

        try {
            BitMatrix bitMatrix = new QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];

            for(int y=0; y<height; ++y) {
                for (int x=0; x<width; ++x) {
                    if(bitMatrix.get(x, y)) {
                        pixels[y*width + x] = 0xFF000000;
                    } else {
                        pixels[y*width + x] = 0xFFFFFFFF;
                    }
                }
            }

            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        } catch (WriterException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}

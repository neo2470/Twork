package com.alex.twork;

import android.os.Bundle;

/**
 * Created by alex on 15-11-20.
 * A QR Scanner based on zxing library
 */
public class QRScanner extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_scanner);
    }
}

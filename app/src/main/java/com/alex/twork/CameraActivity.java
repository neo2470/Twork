package com.alex.twork;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.alex.widget.CameraPreview;

/**
 * Created by alex on 16-3-13.
 * A custom Camera app
 */
public class CameraActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);

        // Create an instance of Camera
        mCamera = getCameraInstance();

        // Create our preview view and set it as the content of our activity
        mPreview = new CameraPreview(this, mCamera);

        FrameLayout cameraPreview = (FrameLayout) findViewById(R.id.cameraPreview);
        cameraPreview.addView(mPreview);
    }

    @Override
    protected void onPause() {
        super.onPause();

        releaseCamera();
    }

    /**
     * check if this device has a camera
     */
    private boolean checkCameraHandware() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        }
        return false;
    }

    /**
     * A safe way to get an instance of the Camera object
     */
    private Camera getCameraInstance() {
        Camera c = null;
        if (checkCameraHandware()) {
            try {
                Camera.open();// attempt to get a Camera instance
            } catch (Exception e) {
                // Camera is not available(in use or does not exist)
                Log.d("Debug-" + TAG, "Camera is not available");
            }
        } else {
            Toast.makeText(this, "No camera on the device", Toast.LENGTH_SHORT).show();
        }

        return c;
    }

    private void releaseCamera() {
        if(null != mCamera) {
            mCamera.release();
            mCamera = null;
        }
    }

    private Camera mCamera;
    private CameraPreview mPreview;

    private final String TAG = "CameraActivity";
}

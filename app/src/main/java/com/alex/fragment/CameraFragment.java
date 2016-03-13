package com.alex.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.alex.twork.R;

/**
 * Created by alex on 16-3-13.
 */
public class CameraFragment extends BaseFragment implements View.OnClickListener {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.camera_fragment, container, false);

        // 为按钮绑定监听事件
        view.findViewById(R.id.cameraThird).setOnClickListener(this);

        mImageView = (ImageView) view.findViewById(R.id.imageView);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.cameraThird :
                dispatchTakePictureIntent();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(REQUEST_IMAGE_CAPTURE == requestCode && resultCode == Activity.RESULT_OK) {

            // 取得缩略图，这里的bitmap并不是全尺寸的图片
            Bundle extra = data.getExtras();
            Bitmap bitmap = (Bitmap) extra.get("data");
            mImageView.setImageBitmap(bitmap);
        }
    }

    private void dispatchTakePictureIntent() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(null != intent.resolveActivity(getActivity().getPackageManager())) {

            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);

            // 保存全尺寸照片到指定目录,这样将不会触发Fragment中的onActivityResult()
//            String fileName = "JPEG_" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
//            try {
//                File image = File.createTempFile(fileName, ".jpg", getActivity().getCacheDir());
//                if(null != image) {
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(image));
//                    startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
//                }
//
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
    }

    private final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView mImageView;
}

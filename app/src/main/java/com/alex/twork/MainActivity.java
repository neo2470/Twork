package com.alex.twork;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.WindowManager;

import com.alex.fragment.MainFragment;
import com.alex.util.DisplayUtil;

/**
 * Created by alex on 15-11-16.
 */
public class MainActivity extends BaseActivity implements MainFragment.OnTopicSelectedListener {

    public enum TopicType {
        Activity, Service
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null == savedInstanceState) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, new MainFragment())
                    .commit();
        }

        DisplayUtil.init(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onTopicSelected(Object topic) {

        if (topic instanceof Fragment) {
            Fragment fragment = (Fragment) topic;
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, fragment)
                    .addToBackStack(null)
                    .commit();
        }

        if (topic instanceof Intent) {
            Intent intent = (Intent) topic;
            int ord = intent.getIntExtra("type", 0);
            switch (ord) {
                case 0: {
                    startActivity(intent);
                    break;
                }
            }
        }
    }
}
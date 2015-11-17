package com.alex.twork;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.alex.fragment.MainFragment;

/**
 * Created by alex on 15-11-16.
 */
public class MainActivity extends BaseActivity implements MainFragment.OnTopicSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (null == savedInstanceState) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(android.R.id.content, new MainFragment())
                    .commit();
        }
    }

    @Override
    public void onTopicSelected(Fragment fragmnet) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, fragmnet)
                .addToBackStack(null)
                .commit();
    }
}
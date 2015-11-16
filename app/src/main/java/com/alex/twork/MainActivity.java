package com.alex.twork;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by alex on 15-11-16.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        String[] mTopics = new String[] {
                getString(R.string.topic_round_imageview)
        };

        ListView topicsList = (ListView) findViewById(R.id.topics);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mTopics);
        topicsList.setAdapter(adapter);

        topicsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class<?> target = null;
                switch (position) {
                    case 0:{
                        target = RoundImageView.class;
                        break;
                    }
                }

                Intent intent = new Intent(MainActivity.this, target);
                startActivity(intent);
            }
        });
    }
}
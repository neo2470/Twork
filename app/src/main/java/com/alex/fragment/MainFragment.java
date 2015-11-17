package com.alex.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.alex.twork.R;

/**
 * Created by alex on 15-11-17.
 * List all topics
 */
public class MainFragment extends BaseFragment {

    public interface OnTopicSelectedListener {
        void onTopicSelected(Fragment fragmnet);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mTopicListener = (OnTopicSelectedListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);

        String[] mTopics = new String[] {
                getString(R.string.topic_sign_in_layout),
                getString(R.string.topic_sign_up_layout),
                getString(R.string.topic_round_image)
        };

        ListView topicsList = (ListView) view.findViewById(R.id.topics);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mTopics);
        topicsList.setAdapter(adapter);

        topicsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Fragment target = null;
                switch (position) {
                    case 0:{
                        target = new SignInFragment();
                        break;
                    }
                    case 1:{
                        target = null;
                        break;
                    }
                    case 2:{
                        target = new RoundImageFragment();
                        break;
                    }
                }

                if(null != target) {
                    mTopicListener.onTopicSelected(target);
                }
            }
        });

        return view;
    }

    private OnTopicSelectedListener mTopicListener;
}

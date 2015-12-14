package com.alex.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.alex.twork.R;
import com.alex.widget.RelatedListView;

/**
 * Created by alex on 15-12-4.
 * A test for RelatedListView
 */
public class RelatedListViewFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.related_listview_fragment, container, false);

        RelatedListView leftListView = (RelatedListView) view.findViewById(R.id.leftListView);
        RelatedListView rightListView = (RelatedListView) view.findViewById(R.id.rightListView);

        leftListView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, leftData));
        rightListView.setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, rightData));

        leftListView.setRelatedListView(rightListView);
        rightListView.setRelatedListView(leftListView);

        return view;
    }

    private String[] leftData = {
            "Left 1",
            "Left 2",
            "Left 3",
            "Left 4",
            "Left 5",
            "Left 6",
            "Left 7",
            "Left 8",
            "Left 9",
            "Left 10",
            "Left 11",
            "Left 12",
            "Left 13",
            "Left 14",
            "Left 15",
            "Left 16",
            "Left 17",
            "Left 18",
            "Left 19",
            "Left 20",
            "Left 21",
            "Left 22",
            "Left 23",
            "Left 24",
            "Left 25",
            "Left 26",
            "Left 27",
            "Left 28",
            "Left 29",
            "Left 30",
            "Left 31",
            "Left 32",
            "Left 33",
            "Left 34",
            "Left 35",
            "Left 36",
            "Left 37",
            "Left 38",
            "Left 39",
            "Left 40",
            "Left 41",
            "Left 42",
            "Left 43",
            "Left 44",
            "Left 45",
            "Left 46",
            "Left 47",
            "Left 48",
            "Left 49",
            "Left 50"
    };

    private String[] rightData = {
            "Right 1",
            "Right 2",
            "Right 3",
            "Right 4",
            "Right 5",
            "Right 6",
            "Right 7",
            "Right 8",
            "Right 9",
            "Right 10",
            "Right 11",
            "Right 12",
            "Right 13",
            "Right 14",
            "Right 15",
            "Right 16",
            "Right 17",
            "Right 18",
            "Right 19",
            "Right 20",
            "Right 21",
            "Right 22",
            "Right 23",
            "Right 24",
            "Right 25",
            "Right 26",
            "Right 27",
            "Right 28",
            "Right 29",
            "Right 30",
            "Right 31",
            "Right 32",
            "Right 33",
            "Right 34",
            "Right 35",
            "Right 36",
            "Right 37",
            "Right 38",
            "Right 39",
            "Right 40",
            "Right 41",
            "Right 42",
            "Right 43",
            "Right 44",
            "Right 45",
            "Right 46",
            "Right 47",
            "Right 48",
            "Right 49",
            "Right 50"
    };
}

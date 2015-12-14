package com.alex.fragment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alex.twork.R;
import com.alex.widget.MeasuredListView;
import com.alex.widget.RelatedHorizontalScrollView;

/**
 * Created by alex on 15-12-7.
 * A scrollable sheet layout
 */
public class ScrollableSheetFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.scrollable_sheet_fragment, container, false);

        RelatedHorizontalScrollView titleScrollView = (RelatedHorizontalScrollView) view.findViewById(R.id.titleScrollView);
        RelatedHorizontalScrollView contentScrollView = (RelatedHorizontalScrollView) view.findViewById(R.id.contentScrollView);
        titleScrollView.setRelatedScrollView(contentScrollView);
        contentScrollView.setRelatedScrollView(titleScrollView);

        LinearLayout rightTitleContainer = (LinearLayout) view.findViewById(R.id.rightTitleContainer);

        fillLinearLayoutWithTextView(rightTitleContainer, true);

        String[] data = initSheetRows();

        MeasuredListView leftListView = (MeasuredListView) view.findViewById(R.id.leftListView);
        leftListView.setAdapter(new LeftListViewAdapter(data));

        MeasuredListView rightListView = (MeasuredListView) view.findViewById(R.id.rightListView);
        rightListView.setAdapter(new RightListViewAdapter(data));

        leftListView.setRelatedListView(rightListView);
        rightListView.setRelatedListView(leftListView);

        return view;
    }

    private String[] initSheetRows() {
        String[] data = new String[SHEET_ROWS];
        String rowText = getString(R.string.sheet_row_text);

        for (int i = 0; i < data.length; ++i) {
            data[i] = String.format(rowText, i + 1);
        }

        return data;
    }

    private TextView[] fillLinearLayoutWithTextView(LinearLayout layout, boolean isTitle) {
        int width = (int) getActivity().getResources().getDimension(R.dimen.sheet_row_width);
        int height = (int) getActivity().getResources().getDimension(R.dimen.sheet_row_height);
        return fillLinearLayoutWithTextView(layout, width, height, isTitle);
    }

    private TextView[] fillLinearLayoutWithTextView(LinearLayout layout, int width, int height, boolean isTitle) {
        TextView[] textViews = new TextView[SHEET_COLUMNS];

        for (int i = 0; i < textViews.length; ++i) {
            textViews[i] = new TextView(getActivity());
            textViews[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, getActivity().getResources().getDimension(R.dimen.sheet_text_size));
            textViews[i].setGravity(Gravity.CENTER);

            String columnText = getString(R.string.sheet_column_text);
            if(isTitle) {
                columnText = String.format(columnText, i + 1);
            } else {
                columnText = getString(R.string.sheet_text_default);
            }

            textViews[i].setText(columnText);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
            textViews[i].setLayoutParams(params);
            layout.addView(textViews[i]);
        }

        return textViews;
    }

    private class LeftListViewAdapter extends BaseAdapter {

        public LeftListViewAdapter(String[] data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int position) {
            return data[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LeftHolder holder;

            if (null == convertView) {
                holder = new LeftHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.left_list_item, null);
                holder.mTextView = (TextView) convertView.findViewById(R.id.leftTextView);
                convertView.setTag(holder);
            } else {
                holder = (LeftHolder) convertView.getTag();
            }

            int colorRes = isOdd(position) ? R.color.sheet_row_bg_0 : R.color.sheet_row_bg_1;
            convertView.setBackgroundColor(getResources().getColor(colorRes));

            holder.mTextView.setText(data[position]);

            return convertView;
        }

        private String[] data;
    }

    private static class LeftHolder {
        TextView mTextView;
    }

    private class RightListViewAdapter extends BaseAdapter {

        public RightListViewAdapter(String[] data) {
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.length;
        }

        @Override
        public Object getItem(int position) {
            return data[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RightHolder holder;

            if (null == convertView) {
                holder = new RightHolder();
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.right_list_item, null);
                holder.mTextViews = fillLinearLayoutWithTextView((LinearLayout) convertView, false);
                convertView.setTag(holder);
            } else {
                holder = (RightHolder) convertView.getTag();
            }

            int colorRes = isOdd(position) ? R.color.sheet_row_bg_0 : R.color.sheet_row_bg_1;
            convertView.setBackgroundColor(getResources().getColor(colorRes));

            return convertView;
        }

        private String[] data;
    }

    private boolean isOdd(int n) {
        return 1 == (n & 1);
    }

    private static class RightHolder {
        TextView[] mTextViews;
    }

    private final int SHEET_ROWS = 60;
    private final int SHEET_COLUMNS = 10;
}

package com.alex.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by alex on 15-12-7.
 */
public class MeasuredListView extends ListView {

    public MeasuredListView(Context context) {
        super(context);
    }

    public MeasuredListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if(null != mListView) {
            mListView.onTouch(ev);
        }

        return super.onTouchEvent(ev);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    private void onTouch(MotionEvent ev) {
        super.onTouchEvent(ev);
    }

    public void setRelatedListView(MeasuredListView mListView) {
        this.mListView = mListView;
    }

    private MeasuredListView mListView;
}

package com.alex.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * Created by alex on 15-12-4.
 *
 */
public class RelatedListView extends ListView {

    public RelatedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if(null != mListView) {
            mListView.onTouch(ev);
        }

        return super.onTouchEvent(ev);
    }

    public void setRelatedListView(RelatedListView mListView) {
        this.mListView = mListView;
    }

    private void onTouch(MotionEvent ev) {
        super.onTouchEvent(ev);
    }

    private RelatedListView mListView;
}

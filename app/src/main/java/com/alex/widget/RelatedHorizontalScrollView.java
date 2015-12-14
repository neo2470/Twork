package com.alex.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;

/**
 * Created by alex on 15-12-7.
 */
public class RelatedHorizontalScrollView extends HorizontalScrollView {

    public RelatedHorizontalScrollView(Context context) {
        super(context);
    }

    public RelatedHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setRelatedScrollView(View mView) {
        this.mView = mView;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if(null != mView) {
            mView.scrollTo(l, t);
        }
    }

    private View mView;
}

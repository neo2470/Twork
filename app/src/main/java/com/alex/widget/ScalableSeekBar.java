package com.alex.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.alex.twork.R;

/**
 * Created by alex on 15-11-30.
 * A scalable SeekBar
 */
public final class ScalableSeekBar extends View {

    public interface OnScalableSeekBarChangeListener {
        void onProgressChanged(ScalableSeekBar scalableSeekBar, int progress);
    }

    public ScalableSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mProgressRectSrc = new Rect();
        mProgressRectDst = new Rect();

        // 解析自定义xml属性
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ScalableSeekBar);

        mThumb = new Tile(a.getDrawable(R.styleable.ScalableSeekBar_thumb));
        mProgress = new Tile(a.getDrawable(R.styleable.ScalableSeekBar_progressDrawable));
        mBackground = new Tile(a.getDrawable(R.styleable.ScalableSeekBar_backgroundDrawable));

        max = a.getInt(R.styleable.ScalableSeekBar_max, 100);
        progress = a.getInt(R.styleable.ScalableSeekBar_progress, 0);

        HALF_OF_THUMB_WIDTH = mThumb.bitmap.getWidth() >> 1;

        minWidth = mBackground.bitmap.getWidth() + mThumb.bitmap.getWidth();
        minHeight = mThumb.bitmap.getHeight();

        a.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int ow, int oh) {

        mBackground.location.x = mProgress.location.x = mThumb.location.x = mProgressRectDst.left = (w - mBackground.bitmap.getWidth()) >> 1;
        mBackground.location.y = mProgress.location.y = mThumb.location.y = mProgressRectDst.top = (h - mBackground.bitmap.getHeight()) >> 1;

        mProgressRectSrc.bottom = mProgress.bitmap.getHeight();
        mProgressRectDst.bottom = mProgress.getBottom();


        mThumb.location.x -= HALF_OF_THUMB_WIDTH;
        mThumb.location.y -= (mThumb.bitmap.getHeight() - mBackground.bitmap.getHeight()) >> 1;

        if(0 < progress) {
            refreshProgressAndThumb();
        }

        super.onSizeChanged(w, h, ow, oh);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN: {

                if (isTileAreaHit(mBackground, event)) {
                    moveThumb((int) (event.getX() - HALF_OF_THUMB_WIDTH));
                }

                initX = mThumb.location.x;
                offsetX = (int) event.getX();
                moveThumb = isTileAreaHit(mThumb, event);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                if (isInView(event) && moveThumb) {
                    moveThumb((int) (initX + event.getX() - offsetX));
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
        }
        invalidate();
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBackground.bitmap, mBackground.location.x, mBackground.location.y, mPaint);
        canvas.drawBitmap(mProgress.bitmap, mProgressRectSrc, mProgressRectDst, mPaint);
        canvas.drawBitmap(mThumb.bitmap, mThumb.location.x, mThumb.location.y, mPaint);
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {

        // begin boilerplate code so parent classes can restore state
        if(!(state instanceof SavedState)) {
            super.onRestoreInstanceState(state);
            return;
        }

        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        // end

        progress = savedState.progressToSave;
    }

    @Override
    protected Parcelable onSaveInstanceState() {

        // begin boilerplate code so parent classes can restore state
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        // end

        savedState.progressToSave = progress;

        return savedState;
    }

    public void setOnScalableSeekBarChangeListener(OnScalableSeekBarChangeListener onScalableSeekBarChangeListener) {
        mListener = onScalableSeekBarChangeListener;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        refreshProgressAndThumb();
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    /**
     * 移动游标Tile
     * @param locationX 游标水平中心的坐标
     */
    private void moveThumb(int locationX) {

        final int left = mBackground.location.x - HALF_OF_THUMB_WIDTH;
        final int right = mBackground.getRight() - HALF_OF_THUMB_WIDTH;

        if (left <= mThumb.location.x && right >= mThumb.location.x) {

            locationX = locationX < left ? left : locationX;
            locationX = locationX > right ? right : locationX;

            mThumb.location.x = locationX;

            mProgressRectSrc.right = locationX + HALF_OF_THUMB_WIDTH - mBackground.location.x;
            mProgressRectDst.right = locationX + HALF_OF_THUMB_WIDTH;

            progress = (int) ((locationX + HALF_OF_THUMB_WIDTH - mBackground.location.x) / (mBackground.bitmap.getWidth() * 1.0f) * max);

            if (null != mListener) {
                mListener.onProgressChanged(this, progress);
            }

            Log.d("Debug-Thumb-Src", mProgressRectSrc.left + ", " + mProgressRectSrc.top + ", " + mProgressRectSrc.right + ", " + mProgressRectSrc.bottom + ", " + mProgressRectSrc.width() + ", " + mProgressRectSrc.height());
            Log.d("Debug-Thumb-Dst", mProgressRectDst.left + ", " + mProgressRectDst.top + ", " + mProgressRectDst.right + ", " + mProgressRectDst.bottom + ", " + mProgressRectDst.width() + ", " + mProgressRectDst.height());
        }
    }

    /**
     * 测量View的宽度
     * @param measureSpec
     * @return
     */
    private int measureWidth(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = minWidth;
        }

        result = result < minWidth ? minWidth : result;
        result = MeasureSpec.makeMeasureSpec(result, MeasureSpec.EXACTLY);

        return result;
    }

    /**
     * 测量View的高度
     * @param measureSpec
     * @return
     */
    private int measureHeight(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = minHeight;
        }

        result = result < minHeight ? minHeight : result;
        result = MeasureSpec.makeMeasureSpec(result, MeasureSpec.EXACTLY);

        return result;
    }

    /**
     * 检查触点是否在Tile范围内
     * @param tile Tile object
     * @param event MotionEvent object
     * @return 在Tile内，true；在Tile外，false
     */
    private boolean isTileAreaHit(Tile tile, MotionEvent event) {
        if (event.getX() < tile.location.x || event.getX() > tile.location.x + tile.bitmap.getWidth()) {
            return false;
        }

        if (event.getY() < tile.location.y || event.getY() > tile.location.y + tile.bitmap.getHeight()) {
            return false;
        }

        return true;
    }

    /**
     * 检查触点是否在View的范围内
     * @param event MotionEvent object
     * @return 在View内，true；在View外，false
     */
    private boolean isInView(MotionEvent event) {
        if (event.getX() < 0 || event.getX() > getWidth()) {
            return false;
        }

        if (event.getY() < 0 || event.getY() > getHeight()) {
            return false;
        }

        return true;
    }

    /**
     * 更新进度
     */
    private void refreshProgressAndThumb() {
        mThumb.location.x = mBackground.location.x + (int) (progress / (max * 1.0f) * mBackground.bitmap.getWidth() - HALF_OF_THUMB_WIDTH);

        mProgressRectSrc.right = mThumb.location.x + HALF_OF_THUMB_WIDTH - mBackground.location.x;
        mProgressRectDst.right = mThumb.location.x + HALF_OF_THUMB_WIDTH;
    }

    private Paint mPaint;

    private int initX;
    private int offsetX;

    private int minWidth;// View的最小宽度
    private int minHeight;// View的最小高度

    private int max;// 进度最大值
    private int progress;// 进度

    private final int HALF_OF_THUMB_WIDTH;// 游标宽度的一半
    private boolean moveThumb;// 是否可以移动游标

    private Tile mThumb;// 游标
    private Tile mProgress;// 前景
    private Tile mBackground;// 背景
    private Rect mProgressRectSrc;
    private Rect mProgressRectDst;

    private OnScalableSeekBarChangeListener mListener;
}

class SavedState extends View.BaseSavedState {

    public SavedState(Parcelable superState) {
        super(superState);
    }

    private SavedState(Parcel in) {
        super(in);
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(progressToSave);
    }

    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {

        @Override
        public SavedState createFromParcel(Parcel source) {
            return new SavedState(source);
        }

        @Override
        public SavedState[] newArray(int size) {
            return new SavedState[size];
        }
    };

    protected int progressToSave;
}

class Tile {

    protected Tile(Drawable drawable) {

        if (null != drawable) {
            BitmapDrawable bmpDrawable = (BitmapDrawable) drawable;
            bitmap = bmpDrawable.getBitmap();
        }

        location = new Point();
    }

    protected int getRight() {
        return location.x + bitmap.getWidth();
    }

    protected int getBottom() {
        return location.y + bitmap.getHeight();
    }

    protected Point location;// Tile在View中被绘制的位置
    protected Bitmap bitmap;// Tile对应的Bitmap
}

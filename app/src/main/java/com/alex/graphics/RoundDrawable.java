package com.alex.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by alex on 15-11-16.
 * 使用自定义Drawable制作圆角图片
 */
public class RoundDrawable extends Drawable {

    public RoundDrawable(Bitmap bitmap) {

        mBmp = bitmap;
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(mRect, mRound, mRound, mPaint);
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        super.setBounds(left, top, right, bottom);
        mRect = new RectF(left, top, right, bottom);
    }

    @Override
    public int getIntrinsicWidth() {
        return mBmp.getWidth();
    }

    @Override
    public int getIntrinsicHeight() {
        return mBmp.getHeight();
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    /**
     * 设置圆角的半径
     * @param mRound
     */
    public void setRound(float mRound) {
        this.mRound = mRound;
    }

    private float mRound;
    private RectF mRect;
    private Bitmap mBmp;
    private Paint mPaint;
}

package com.lisen.android.tupianchulidemo.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/8/13.
 */
public class MyImageView extends ImageView implements ViewTreeObserver.OnGlobalLayoutListener, ScaleGestureDetector.OnScaleGestureListener, View.OnTouchListener {

    private boolean mOnce = false;
    /**
     * 初始化时的缩放比例值
     */
    private float mInitScale;
    /**
     * 双击放大的值
     */
    private float mMidScale;

    /**
     * 双击放大的最大值
     */
    private float mMaxScale;

    /**
     * 用于实现缩放的matrix
     */
    private Matrix mScaleMatrix;

    /**
     * 缩放手势检测
     */
    private ScaleGestureDetector mScaleGestureDetector;

    public MyImageView(Context context) {
        this(context, null);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化操作
     */
    private void init(Context context) {
        mScaleMatrix = new Matrix();
        setScaleType(ScaleType.MATRIX);
        mScaleGestureDetector = new ScaleGestureDetector(context, this);
        //添加触摸监听接口
        setOnTouchListener(this);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //添加接口
        getViewTreeObserver().addOnGlobalLayoutListener(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        //移除接口
        getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    /**
     * 添加图片加载完成后的监听接口，以获得图片的宽高，并根据情况对其进行压缩或放大
     */
    @Override
    public void onGlobalLayout() {
        if (!mOnce) {
            //控件宽度
            int width = getWidth();
            //控件高度
            int height = getHeight();

            Drawable d = getDrawable();
            if (d == null) {
                return;
            }
            //drawable的宽度
            int dw = d.getIntrinsicWidth();
            //drawable的高度
            int dh = d.getIntrinsicHeight();

            // 缩放比例
            float scale = 1.0f;

            //drawable的宽度大于控件的宽度，高度小于控件的高度，将其缩小
            if (dw > width && dh < height) {
                scale = width * 1.0f / dw;
            }

            //drawable的宽度小于控件的宽度，高度大于控件的高度，将其缩小
            if (dw < width && dh > height) {
                scale = height * 1.0f / dh;
            }

            //drawable的宽度小于控件的宽度，高度小于控件的高度,将图片进行放大，取放大倍数的最小值
            if (dw < width && dh < height) {
                scale = Math.min((width * 1.0f / dw), (height * 1.0f / dh));
            }

            //drawable的宽度大于控件的宽度，高度大于控件的高度,将其缩小
            if (dw > width && dh > height) {
                scale = Math.min((width * 1.0f / dw), (height * 1.0f / dh));
            }

            mInitScale = scale;
            //设为2倍
            mMidScale = 2.0f * mInitScale;
            //设为4倍
            mMaxScale = 4.0f * mInitScale;

            //把图片移动控件的中心位置
            int dx = getWidth() / 2 - dw / 2;
            int dy = getHeight() / 2 - dh / 2;
            //移动到控件的中心
            mScaleMatrix.postTranslate(dx, dy);
            //缩放
            mScaleMatrix.postScale(mInitScale, mInitScale, getWidth() / 2, getHeight() / 2);
            //执行
            setImageMatrix(mScaleMatrix);
            mOnce = true;
        }
    }


    @Override
    public boolean onScale(ScaleGestureDetector detector) {
        //当前图片缩放值
        float scale = getScale();
        //检测手势想要的缩放值
        float scaleFactor = detector.getScaleFactor();
        //缩放区间合理的话则进行对应缩放
        if ((scale < mMaxScale && scaleFactor > 1.0f) || (scale > mInitScale && scaleFactor < 1.0f)) {

            //当放大到最大值时不能再放大
            if (scale * scaleFactor > mMaxScale) {
                scaleFactor = mMaxScale / scale;
            }

            //当缩小到最小值时不能再缩小
            if (scale * scaleFactor < mInitScale) {
                scaleFactor = mInitScale / scale;
            }
            mScaleMatrix.postScale(scaleFactor, scaleFactor, getWidth() / 2, getHeight() / 2);
            setImageMatrix(mScaleMatrix);
        }
        return true;
    }

    @Override
    public boolean onScaleBegin(ScaleGestureDetector detector) {
        return true;
    }

    @Override
    public void onScaleEnd(ScaleGestureDetector detector) {

    }

    /**
     * 得到当前图片的缩放值
     *
     * @return
     */
    private float getScale() {
        float[] values = new float[9];
        mScaleMatrix.getValues(values);
        return values[Matrix.MSCALE_X];

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        //将事件传递到手势缩放检测
        mScaleGestureDetector.onTouchEvent(event);
        return true;
    }
}

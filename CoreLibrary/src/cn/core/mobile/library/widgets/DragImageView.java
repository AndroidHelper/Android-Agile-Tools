/**
 * project name: CoreLibrary
 * file name: DragImageView.java
 * description：自定义手势缩小放大图片
 * creation time: 2014年9月24日
 * @Author：Will Wu
 * @Modifier：Will Wu
 */
package cn.core.mobile.library.widgets;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

public class DragImageView extends ImageView implements OnTouchListener {
    private static final int     NONE        = 0;
    /** 拖拉照片模式 */
    private static final int     MODE_DRAG   = 1;
    /** 放大缩小照片模式 */
    private static final int     MODE_ZOOM   = 2;
    /** 记录是拖拉照片模式还是放大缩小照片模式 */
    private int                  mMode       = NONE;

    static final int             CLICK       = 3;

    /** 用于记录开始时候的坐标位置 */
    private PointF               mStartPoint = new PointF();
    /** 用于记录结束时候的坐标位置 */
    private PointF               mLastPoint  = new PointF();
    /** 用于记录拖拉图片移动的坐标位置 */
    private Matrix               mMatrix     = new Matrix();

    private float                mSaveScale  = 1f;
    /** 最大缩放比例 */
    private static final float   MAX_SCALE   = 4f;
    /** 最小缩放比例 */
    private float                mMinScale   = 1f;
    /** 最大缩放比例 */
    private float                mMaxScale   = MAX_SCALE;
    /** */
    private float[]              mMatrixVal  = new float[9];

    private ScaleGestureDetector mScaleDetector;

    protected float              mOrigWidth;
    protected float              mOrigHeight;

    private int                  mViewWidth;
    private int                  mViewHeight;

    private int                  mOldMeasuredWidth;
    private int                  mOldMeasuredHeight;

    public DragImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        setOnTouchListener(this);
        mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
        setScaleType(ScaleType.MATRIX);
        setImageMatrix(mMatrix);
    }

    public DragImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        // TODO Auto-generated constructor stub
    }

    public DragImageView(Context context) {
        this(context, null, 0);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        // TODO Auto-generated method stub
        super.setImageDrawable(drawable);

        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        init();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // TODO Auto-generated method stub
        mScaleDetector.onTouchEvent(event);
        // 当前坐标
        PointF currPoint = new PointF(event.getX(), event.getY());
        switch (event.getAction()) {
        // 手指压下屏幕
            case MotionEvent.ACTION_DOWN:
                mMode = MODE_DRAG;
                mStartPoint.set(currPoint);
                mLastPoint.set(currPoint);
                break;

            // 手指在屏幕上移动，该事件会被不断触发
            case MotionEvent.ACTION_MOVE:
                if (mMode == MODE_DRAG) {
                    float deltaX = currPoint.x - mLastPoint.x;
                    float deltaY = currPoint.y - mLastPoint.y;
                    float fixTransX = getFixDragTrans(deltaX, mViewWidth, mOrigWidth * mSaveScale);
                    float fixTransY = getFixDragTrans(deltaY, mViewHeight, mOrigHeight * mSaveScale);
                    mMatrix.postTranslate(fixTransX, fixTransY);
                    fixTrans();
                    mLastPoint.set(currPoint.x, currPoint.y);
                }
                break;

            // 手指离开屏幕
            case MotionEvent.ACTION_UP:
                mMode = NONE;
                int xDiff = (int) Math.abs(currPoint.x - mStartPoint.x);
                int yDiff = (int) Math.abs(currPoint.y - mStartPoint.y);
                if (xDiff < CLICK && yDiff < CLICK)
                    performClick();
                break;

            // 当触点离开屏幕，但是屏幕上还有触点(手指)
            case MotionEvent.ACTION_POINTER_UP:
                mMode = NONE;
                break;
        }
        setImageMatrix(mMatrix);
        invalidate();
        return true;
    }

    public void setMaxZoom(float x) {
        this.mMaxScale = x;
    }

    private void init() {
        mViewWidth = getWidth();
        mViewHeight = getHeight();

        if (mOldMeasuredWidth == mViewWidth && mOldMeasuredHeight == mViewHeight || mViewWidth == 0 || mViewHeight == 0)
            return;

        if (mSaveScale == 1) {
            float scale;
            Drawable drawable = getDrawable();
            if (drawable == null || drawable.getIntrinsicWidth() == 0 || drawable.getIntrinsicHeight() == 0)
                return;

            mOldMeasuredHeight = mViewHeight;
            mOldMeasuredWidth = mViewWidth;

            int bmWidth = drawable.getIntrinsicWidth();
            int bmHeight = drawable.getIntrinsicHeight();

            Log.d("bmSize", "bmWidth: " + bmWidth + " bmHeight : " + bmHeight);

            // 初始化缩放比例。
            float scaleX = (float) mViewWidth / (float) bmWidth;
            float scaleY = (float) mViewHeight / (float) bmHeight;
            scale = Math.min(scaleX, scaleY);
            if (scale > 1.0f) {
                scale = 1.0f;
            }
            mMatrix.setScale(scale, scale);

            // Center the image
            float redundantYSpace = (float) mViewHeight - (scale * (float) bmHeight);
            float redundantXSpace = (float) mViewWidth - (scale * (float) bmWidth);
            redundantYSpace /= (float) 2;
            redundantXSpace /= (float) 2;
            mMatrix.postTranslate(redundantXSpace, redundantYSpace);

            mOrigWidth = mViewWidth - 2 * redundantXSpace;
            mOrigHeight = mViewHeight - 2 * redundantYSpace;
            setImageMatrix(mMatrix);
        }
        fixTrans();
    }

    private void fixTrans() {
        mMatrix.getValues(mMatrixVal);
        float transX = mMatrixVal[Matrix.MTRANS_X];
        float transY = mMatrixVal[Matrix.MTRANS_Y];

        float fixTransX = getFixTrans(transX, mViewWidth, mOrigWidth * mSaveScale);
        float fixTransY = getFixTrans(transY, mViewHeight, mOrigHeight * mSaveScale);

        if (fixTransX != 0 || fixTransY != 0)
            mMatrix.postTranslate(fixTransX, fixTransY);
    }

    private float getFixTrans(float trans, float viewSize, float contentSize) {
        float minTrans, maxTrans;

        if (contentSize <= viewSize) {
            minTrans = 0;
            maxTrans = viewSize - contentSize;
        } else {
            minTrans = viewSize - contentSize;
            maxTrans = 0;
        }

        if (trans < minTrans)
            return -trans + minTrans;
        if (trans > maxTrans)
            return -trans + maxTrans;
        return 0;
    }

    private float getFixDragTrans(float delta, float viewSize, float contentSize) {
        if (contentSize <= viewSize) {
            return 0;
        }
        return delta;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            mMode = MODE_ZOOM;
            return true;
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float mScaleFactor = detector.getScaleFactor();
            float origScale = mSaveScale;
            mSaveScale *= mScaleFactor;
            if (mSaveScale > mMaxScale) {
                mSaveScale = mMaxScale;
                mScaleFactor = mMaxScale / origScale;
            } else if (mSaveScale < mMinScale) {
                mSaveScale = mMinScale;
                mScaleFactor = mMinScale / origScale;
            }

            if (mOrigWidth * mSaveScale <= mViewWidth || mOrigHeight * mSaveScale <= mViewHeight)
                mMatrix.postScale(mScaleFactor, mScaleFactor, mViewWidth / 2, mViewHeight / 2);
            else
                mMatrix.postScale(mScaleFactor, mScaleFactor, detector.getFocusX(), detector.getFocusY());

            fixTrans();
            return true;
        }
    }

}

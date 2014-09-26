/**
 * 项目名称: CoreLibrary
 * 文件名称: ViewfinderViewX.java
 * 创建时间: 2014年3月17日
 * Copyright: 2014 www.fantasee.cn Inc. All rights reserved. 
 */

package zxing.view;

import com.google.zxing.ResultPoint;

import cn.core.library.mobile.R;
import zxing.camera.CameraManager;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Will.Wu </br> Create at 2014年3月17日 下午1:46:19
 * @version 1.0
 */
public class ViewfinderView extends SurfaceView implements SurfaceHolder.Callback {
    /**
     * 绘图线程
     */
    private DrawThread mDrawThread;
    /**
     * 扫描区域四个角图片
     */
    private List<Bitmap> mScanCorner = new ArrayList<Bitmap>();
    /**
     * 扫描线图片
     */
    private Bitmap mScanLine = null;
    /**
     * 扫描区域外的遮罩颜色
     */
    private int mMaskColor;
    private Paint mPaint;
    /**
     * 扫描线图片的Y坐标
     */
    private float mScanLineY = 0;

    private Rect mFrameRect;

    //最大帧数 (1000 / 30)
    private static final int DRAW_INTERVAL = 30;

    public ViewfinderView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        SurfaceHolder holder = this.getHolder();
        // 设置透明色
        holder.setFormat(PixelFormat.TRANSPARENT);
        holder.addCallback(this);

        setZOrderOnTop(true);

        initResources();

        // 创建一个绘图线程
        mDrawThread = new DrawThread(holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        mFrameRect = CameraManager.get().getFramingRect();
        mScanLineY = mFrameRect.top;

        mDrawThread.isRun = true;
        mDrawThread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        mDrawThread.isRun = false;
    }

    /**
     * 初始资源
     */
    private void initResources() {
        mPaint = new Paint();
        Resources resources = getResources();
        mMaskColor = resources.getColor(R.color.viewfinder_mask);

        mScanCorner.add(BitmapFactory.decodeResource(getResources(), R.drawable.scanqr1));
        mScanCorner.add(BitmapFactory.decodeResource(getResources(), R.drawable.scanqr2));
        mScanCorner.add(BitmapFactory.decodeResource(getResources(), R.drawable.scanqr3));
        mScanCorner.add(BitmapFactory.decodeResource(getResources(), R.drawable.scanqr4));
        mScanLine = BitmapFactory.decodeResource(getResources(), R.drawable.qrcode_scan_line);
    }

    public void drawViewfinder() {

    }

    public void addPossibleResultPoint(ResultPoint point) {
    }

    private void doDraw(Canvas canvas) {
        // 清除屏幕
        canvas.drawColor(Color.TRANSPARENT, Mode.CLEAR);
        drawMaskColor(canvas);
        drawScanCorner(canvas);
        mScanLineY += 5;
        if (mScanLineY > mFrameRect.bottom - mScanLine.getHeight() - 10) {
            mScanLineY = mFrameRect.top + mScanLine.getHeight();
        }
        int x = (mFrameRect.width() - mScanLine.getWidth()) / 2 + mFrameRect.left;
        drawLaser(canvas, x, (int) mScanLineY);
    }

    /**
     * 绘制预览框外侧的半透明遮罩
     *
     * @param canvas canvas
     */
    private void drawMaskColor(Canvas canvas) {
        Rect frame = new Rect(mFrameRect.left - 2, mFrameRect.top - 2, mFrameRect.right + 2, mFrameRect.bottom + 2);
        int width = canvas.getWidth();
        int height = canvas.getHeight();

        mPaint.setColor(mMaskColor);
        canvas.drawRect(0, 0, width, frame.top, mPaint);
        canvas.drawRect(0, frame.top, frame.left, frame.bottom, mPaint);
        canvas.drawRect(frame.right, frame.top, width, frame.bottom, mPaint);
        canvas.drawRect(0, frame.bottom, width, height, mPaint);

        Rect lineRect = new Rect(mFrameRect.left - 1, mFrameRect.top - 1, mFrameRect.right, mFrameRect.bottom);
        mPaint.setColor(getResources().getColor(R.color.white));
        canvas.drawLine(lineRect.left, lineRect.top, lineRect.right, lineRect.top, mPaint);
        canvas.drawLine(lineRect.left, lineRect.top, lineRect.left, lineRect.bottom, mPaint);
        canvas.drawLine(lineRect.right, lineRect.top, lineRect.right, lineRect.bottom, mPaint);
        canvas.drawLine(lineRect.left, lineRect.bottom, lineRect.right, lineRect.bottom, mPaint);
    }

    /**
     * 绘制预览框的四个角图片
     *
     * @param canvas canvas
     */
    private void drawScanCorner(Canvas canvas) {
        final Rect frame = new Rect(mFrameRect);
        int scanWidth = mScanCorner.get(0).getWidth();
        int scanHeight = mScanCorner.get(0).getHeight();
        canvas.drawBitmap(mScanCorner.get(0), frame.left, frame.top, null);
        canvas.drawBitmap(mScanCorner.get(1), frame.right - scanWidth, frame.top, null);
        canvas.drawBitmap(mScanCorner.get(2), frame.left, frame.bottom - scanHeight,
                null);
        canvas.drawBitmap(mScanCorner.get(3), frame.right - scanWidth, frame.bottom
                - scanHeight, null);
    }

    /**
     * 绘制预览框内的扫描线
     *
     * @param canvas  canvas
     * @param offsetX 扫描线的X坐标
     * @param offsetY 扫描线的Y坐标
     */
    private void drawLaser(Canvas canvas, int offsetX, int offsetY) {
        canvas.drawBitmap(mScanLine, offsetX, offsetY, null);
    }

    /**
     * 绘图的内部线程类
     *
     * @author Will.Wu </br> Create at 2014年3月17日 下午1:47:42
     * @version 1.0
     */
    class DrawThread extends Thread {
        private final SurfaceHolder holder;
        public boolean isRun;

        public DrawThread(SurfaceHolder holder) {
            this.holder = holder;
            isRun = true;
        }

        @Override
        public void run() {
            long deltaTime = 0;
            long tickTime = 0;
            tickTime = System.currentTimeMillis();
            while (isRun) {
                Canvas canvas = null;
                synchronized (this.holder) {
                    canvas = this.holder.lockCanvas(null);
                    doDraw(canvas);
                    this.holder.unlockCanvasAndPost(canvas);

                    deltaTime = System.currentTimeMillis() - tickTime;
                    if (deltaTime < DRAW_INTERVAL) {
                        try {
                            Thread.sleep(DRAW_INTERVAL - deltaTime);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    tickTime = System.currentTimeMillis();
                }
            }
        }

    }

}

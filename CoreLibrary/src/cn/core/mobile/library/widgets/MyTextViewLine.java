/**
 * project name: CoreLibrary
 * file name: MyTextViewLine.java
 * description：自定义带中线的textview
 * creation time: 2014年9月24日
 * @Author：Will Wu
 * @Modifier：Will Wu
 */
package cn.core.mobile.library.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.widget.TextView;

public  class MyTextViewLine extends TextView {
	public MyTextViewLine(Context context) {
        super(context);
    }

    public MyTextViewLine(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextViewLine(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


	@SuppressLint("DrawAllocation") protected  void onDraw(Canvas canvas) {
		Paint paint = new Paint();
		Style style = Style.FILL_AND_STROKE;
		paint.setStyle(style);
		paint.setColor(Color.RED);
		paint.setStrokeWidth(3);
		canvas.drawLine(200, 20, 500, 20, paint);
		super.onDraw(canvas);
	}
}

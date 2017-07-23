package com.iqiyi.maxiaowu.bazierdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by maxiaowu on 2017/7/23.
 */

public class QuadBazier extends View {

    private  Path mPath;
    private  Context mContext;

    private Paint mPaint;

    private int centerX,centerY;

    private int startX,startY;

    private int endX,endY;

    private float controlX,controlY;

    public QuadBazier(Context context) {
        this(context,null);
    }

    public QuadBazier(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public QuadBazier(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mPath = new Path();

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        centerX = w/2;
        centerY = h/2;

        startX = centerX-200;
        startY = centerY;

        endX = centerX+200;
        endY = centerY;

        controlX = centerX;
        controlY = centerY-100;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        controlY = event.getY();
        controlX = event.getX();
        invalidate();
        return true;//TODO:点击事件的传递，为什么返回的是父类方法时,控制点只能点一次移动一下；返回true后，就可以跟随手指移动而移动了
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setColor(Color.GRAY);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(20);
        canvas.drawPoint(startX,startY,mPaint);
        canvas.drawPoint(endX,endY,mPaint);
        canvas.drawPoint(controlX,controlY,mPaint);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(4);
        canvas.drawLine(startX,startY,controlX,controlY,mPaint);
        canvas.drawLine(endX,endY,controlX,controlY,mPaint);

        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(8);

//        mPath = new Path();//为什么值new一次Path对象后,之前画的path不消失
        mPath.rewind();
        mPath.moveTo(startX,startY);
        mPath.quadTo(controlX,controlY,endX,endY);


        canvas.drawPath(mPath,mPaint);

    }
}

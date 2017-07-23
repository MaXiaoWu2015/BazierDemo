package com.iqiyi.maxiaowu.bazierdemo.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by maxiaowu on 2017/7/23.
 */

public class HeartCubicBazier extends View {

    private static final float C = (float) (4/3 * Math.tan(Math.PI/8));//8 = 2 * n;（n为4个数据点）

    private static final float RADIUS = 200;

    private Path mPath;

    private Paint mPaint;

    private Context mContext;

    private float[] mData = new float[8];//顺时针记录绘制圆形的四个数据点

    private float[] mCtrl = new float[16];//顺时针记录绘制圆形的八个控制点

    private float mCtrlLineLength = RADIUS * C;

    private int mCenterX,mCenterY;

    private float mDuration = 1000;                     // 变化总时长

    private float mCurrent = 0;                         // 当前已进行时长

    private float mCount = 100;                         // 将时长总共划分多少份

    private float mPiece = mDuration/mCount;            // 每一份的时长

    public HeartCubicBazier(Context context) {
        this(context,null);
    }

    public HeartCubicBazier(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public HeartCubicBazier(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mPaint = new Paint();
        mPath = new Path();

        mData[0] = 0;
        mData[1] = RADIUS;

        mData[2] = RADIUS;
        mData[3] = 0;

        mData[4] = 0;
        mData[5] = -RADIUS;

        mData[6] = -RADIUS;
        mData[7] = 0;

        mCtrl[0]  = mData[0]+mCtrlLineLength;
        mCtrl[1]  = mData[1];

        mCtrl[2]  = mData[2];
        mCtrl[3]  = mData[3]+mCtrlLineLength;

        mCtrl[4]  = mData[2];
        mCtrl[5]  = mData[3]-mCtrlLineLength;

        mCtrl[6]  = mData[4]+mCtrlLineLength;
        mCtrl[7]  = mData[5];

        mCtrl[8]  = mData[4]-mCtrlLineLength;
        mCtrl[9]  = mData[5];

        mCtrl[10] = mData[6];
        mCtrl[11] = mData[7]-mCtrlLineLength;

        mCtrl[12] = mData[6];
        mCtrl[13] = mData[7]+mCtrlLineLength;

        mCtrl[14] = mData[0]-mCtrlLineLength;
        mCtrl[15] = mData[1];



    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCenterX = w/2;
        mCenterY = h/2;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCoordinateSystem(canvas);

        canvas.translate(mCenterX,mCenterY);
        canvas.scale(1,-1);
        drawAuxiliaryLine(canvas);

        mPaint.setColor(Color.BLUE);
        mPaint.setStrokeWidth(4);
        mPaint.setStyle(Paint.Style.STROKE);
        mPath.rewind();
        mPath.moveTo(mData[0],mData[1]);

        mPath.cubicTo(mCtrl[0],mCtrl[1],mCtrl[2],mCtrl[3],mData[2],mData[3]);
        mPath.cubicTo(mCtrl[4],  mCtrl[5],  mCtrl[6],  mCtrl[7],     mData[4], mData[5]);
        mPath.cubicTo(mCtrl[8],  mCtrl[9],  mCtrl[10], mCtrl[11],    mData[6], mData[7]);
        mPath.cubicTo(mCtrl[12], mCtrl[13], mCtrl[14], mCtrl[15],    mData[0], mData[1]);

        canvas.drawPath(mPath,mPaint);

        mCurrent += mPiece;
        if (mCurrent < mDuration){

            mData[1] -= 120/mCount;
            mCtrl[7] += 80/mCount;
            mCtrl[9] += 80/mCount;

            mCtrl[4] -= 20/mCount;
            mCtrl[10] += 20/mCount;

            postInvalidateDelayed((long) mPiece);
        }

    }

    // 绘制辅助线
    private void drawAuxiliaryLine(Canvas canvas) {
        // 绘制数据点和控制点
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(20);

        for (int i=0; i<8; i+=2){
            canvas.drawPoint(mData[i],mData[i+1], mPaint);
        }

        for (int i=0; i<16; i+=2){
            canvas.drawPoint(mCtrl[i], mCtrl[i+1], mPaint);
        }


        // 绘制辅助线
        mPaint.setStrokeWidth(4);

        for (int i=2, j=2; i<8; i+=2, j+=4){
            canvas.drawLine(mData[i],mData[i+1],mCtrl[j],mCtrl[j+1],mPaint);
            canvas.drawLine(mData[i],mData[i+1],mCtrl[j+2],mCtrl[j+3],mPaint);
        }
        canvas.drawLine(mData[0],mData[1],mCtrl[0],mCtrl[1],mPaint);
        canvas.drawLine(mData[0],mData[1],mCtrl[14],mCtrl[15],mPaint);
    }


    private void drawCoordinateSystem(Canvas canvas) {
        canvas.save();

        canvas.translate(mCenterX,mCenterY);//将坐标系已到画布中央
        canvas.scale(1,-1);//翻转Y轴

        Paint fuzhuPaint = new Paint();
        fuzhuPaint.setColor(Color.RED);
        fuzhuPaint.setStrokeWidth(5);
        fuzhuPaint.setStyle(Paint.Style.STROKE);

        canvas.drawLine(-2000,0,2000,0,fuzhuPaint);
        canvas.drawLine(0,-2000,0,2000,fuzhuPaint);

        canvas.restore();
    }
}

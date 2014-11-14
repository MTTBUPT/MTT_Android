package com.mtt.customview;

import com.mtt.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

public class CompassView extends View{
	
	private Paint mPaint = new Paint();
	
	/** 父控件的宽度和长度*/
	private int mWidth,mHeight;
	/** 圆心坐标*/
	private float mPointX,mPointY;
	/** view半径*/
	private float mRadius;
	/** 第二个环半径*/
	private float mSecondRadius;
	/** 第三个环半径*/
	private float mThirdRadius;
	
	/** 旋转角度*/
	private float arc = 0;
	
	// 读取InputStream并得到位图
	BitmapDrawable bmpDraw_compass_north=(BitmapDrawable)getResources().getDrawable(R.drawable.compass_north);
	Bitmap bmp_compass_north=bmpDraw_compass_north.getBitmap();
	BitmapDrawable bmpDraw_compass_south=(BitmapDrawable)getResources().getDrawable(R.drawable.compass_south);
	Bitmap bmp_compass_south=bmpDraw_compass_south.getBitmap();	
	BitmapDrawable bmpDraw_compass_west=(BitmapDrawable)getResources().getDrawable(R.drawable.compass_west);
	Bitmap bmp_compass_west=bmpDraw_compass_west.getBitmap();	
	BitmapDrawable bmpDraw_compass_east=(BitmapDrawable)getResources().getDrawable(R.drawable.compass_east);
	Bitmap bmp_compass_east=bmpDraw_compass_east.getBitmap();	
	/** 位图的宽度*/
	private int bmp_width = bmp_compass_north.getWidth();
	
	public CompassView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	      
		mWidth = getMeasuredWidth();
		mHeight = getMeasuredHeight();
		mRadius = mHeight*0.39f;
		mSecondRadius = mHeight*0.33f;
		mThirdRadius = mHeight*0.28f;
		
		mPointX = mRadius + bmp_width/2;
		mPointY = mPointX;
		
	    setMeasuredDimension((int)(mRadius*2+bmp_width), (int)(mRadius*2+bmp_width));  
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		canvas.drawARGB(0, 0, 0, 0);
		// 去锯齿
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL);
		
		// 绘制外环
		mPaint.setARGB(255, 25, 33, 43);
		canvas.drawCircle(mPointX, mPointY, mRadius, mPaint);
		
		// 绘制第二个环
		mPaint.setARGB(255, 44, 54, 66);
		canvas.drawCircle(mPointX, mPointY, mSecondRadius, mPaint);
		
		// 绘制第三个环
		mPaint.setARGB(255, 34, 44, 54);
		canvas.drawCircle(mPointX, mPointY, mThirdRadius, mPaint);

		double myarc = changeArc(arc);
		float x1 = mPointX+(float)(mSecondRadius*Math.sin(myarc));
		float y1 = mPointY-(float)(mSecondRadius*Math.cos(myarc)); 
		float x2 = mPointX+(float)(mSecondRadius*Math.cos(myarc));
		float y2 = mPointY+(float)(mSecondRadius*Math.sin(myarc));
		float x3 = mPointX-(float)(mSecondRadius*Math.sin(myarc));
		float y3 = mPointY+(float)(mSecondRadius*Math.cos(myarc));
		float x4 = mPointX-(float)(mSecondRadius*Math.cos(myarc));
		float y4 = mPointY-(float)(mSecondRadius*Math.sin(myarc));

		RectF dst_north = new RectF(x1-bmp_width/2, y1-bmp_width/2, x1+bmp_width/2, y1+bmp_width/2);
		canvas.drawBitmap(bmp_compass_north, null, dst_north, mPaint);
		RectF dst_south = new RectF(x3-bmp_width/2, y3-bmp_width/2, x3+bmp_width/2, y3+bmp_width/2);
		canvas.drawBitmap(bmp_compass_south, null, dst_south, mPaint);
		RectF dst_west = new RectF(x4-bmp_width/2, y4-bmp_width/2, x4+bmp_width/2, y4+bmp_width/2);
		canvas.drawBitmap(bmp_compass_west, null, dst_west, mPaint);
		RectF dst_east = new RectF(x2-bmp_width/2, y2-bmp_width/2, x2+bmp_width/2, y2+bmp_width/2);
		canvas.drawBitmap(bmp_compass_east, null, dst_east, mPaint);

	}
	
    /** 转换角度为弧度*/
    public double changeArc(float arc){
    	return arc*Math.PI/180;
    }
    
    public void setArc(float myarc){
    	arc = myarc;
    	invalidate();
    }
    
}


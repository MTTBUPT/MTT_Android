package com.mtt.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.view.View;

public class SteepView extends View{
	
	private Paint mPaint = new Paint();
	
	/** 屏幕宽度和长度*/
	private int mWidth,mHeight;
	/** 坡度view半径*/
	private int mRadius;
	/** 坡度第二个环半径*/
	private int mSecondRadius;
	/** 坡度第三个环半径*/
	private int mThirdRadius;
	
	/** 坡度*/
	private int mSteep = 0;
	/** 是否为上坡*/
	private boolean isUp;
	
	
	public SteepView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	      
		mWidth = this.getMeasuredWidth();
		mHeight = this.getMeasuredHeight();
		mRadius = mHeight/6;
		mSecondRadius = mHeight/7;
		mThirdRadius = mHeight/8;
		
	    setMeasuredDimension(mRadius*2, mRadius*2);  
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
		canvas.drawCircle(mRadius, mRadius, mRadius, mPaint);
		
		// 绘制第二个环
		mPaint.setARGB(255, 44, 54, 66);
		canvas.drawCircle(mRadius, mRadius, mSecondRadius, mPaint);
		
		// 绘制第三个环
		mPaint.setARGB(255, 34, 44, 54);
		canvas.drawCircle(mRadius, mRadius, mThirdRadius, mPaint);
		
		// 绘制扇形
		mPaint.setARGB(255, 226, 24, 39);
		RectF oval = new RectF(mRadius-mThirdRadius, mRadius-mThirdRadius, mRadius+mThirdRadius, mRadius+mThirdRadius);
		if(isUp){
			canvas.drawArc(oval, -mSteep*2, 180+mSteep*4, true, mPaint);
		}else if(!isUp && mSteep<45){
			canvas.drawArc(oval, mSteep*2, 180-mSteep*4, true, mPaint);
		}
		
		// 绘制矩形
		float x1 = mRadius-(float)(mThirdRadius*Math.cos(changeArc(2*mSteep)));
		float y1 = mRadius-(float)(mThirdRadius*Math.sin(changeArc(2*mSteep)));
		float x2 = mRadius+(float) (mThirdRadius*Math.cos(changeArc(2*mSteep)));
		float y2 = mRadius+(float)(mThirdRadius*Math.sin(changeArc(2*mSteep)));
		if(isUp){
			canvas.drawRect(x1, y1, x2, mRadius, mPaint);
		}else if(!isUp && mSteep<45){
			mPaint.setARGB(255, 34, 44, 54);
			canvas.drawRect(x1, mRadius, x2, y2, mPaint);
		}
		
		// 绘制坡度
		// ----------设置字符大小后绘制字符----------
		mPaint.setStrokeWidth(8);
		mPaint.setARGB(255, 255, 255, 55);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setTextSize(70);
		mPaint.setFakeBoldText(true); //true为粗体，false为非粗体
		mPaint.setTextAlign(Align.CENTER);
		if(isUp){
			canvas.drawText(mSteep+" °", mRadius, mRadius, mPaint);
		}else {
			canvas.drawText("-"+mSteep+" °", mRadius, mRadius, mPaint);
		}
	}
	
    public void setSteep(int steep,boolean b){

    	mSteep = steep;
    	isUp = b;
    	invalidate();
    }
    
    /** 转换角度为弧度*/
    public double changeArc(int arc){
    	return arc*Math.PI/180;
    }
    
}


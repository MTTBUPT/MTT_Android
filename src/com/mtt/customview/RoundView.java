package com.mtt.customview;

import com.mtt.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/** 
 * 自定义圆盘View，定义了接口实现圆盘按钮的操作
 * @author Kerry
 * */
public class RoundView extends View{

	public static final int COM_MTT_ROUNDVIEW_TOUCH_MABIAO = 1;
	public static final int COM_MTT_ROUNDVIEW_TOUCH_CAMERA = 2;
	public static final int COM_MTT_ROUNDVIEW_TOUCH_GUIDE = 3;
	public static final int COM_MTT_ROUNDVIEW_TOUCH_TOOL = 4;
	public static final int COM_MTT_ROUNDVIEW_TOUCH_START = 5;

	// 画笔
	private Paint mPaint = new Paint();

	/** 圆心坐标 */
	private int mPointX = 0, mPointY = 0;
	/** 外层半径 */
	private int outRadius = 0;
	/** 外圆半径 */
	private int exRadius = 0;
	/** 内圆半径 */
	private int inRadius = 0;
	
	private int isTouchDown = 0;
	private RoundViewOnTouchListener mOnTouchListener = null;
	
	
	// 读取InputStream并得到位图
	BitmapDrawable bmpDraw_mabiao=(BitmapDrawable)getResources().getDrawable(R.drawable.main_mabiao);
	Bitmap bmp_mabiao=bmpDraw_mabiao.getBitmap();
	
	BitmapDrawable bmpDraw_camera=(BitmapDrawable)getResources().getDrawable(R.drawable.main_camera);
	Bitmap bmp_camera=bmpDraw_camera.getBitmap();
	
	BitmapDrawable bmpDraw_guide=(BitmapDrawable)getResources().getDrawable(R.drawable.main_guide);
	Bitmap bmp_guide=bmpDraw_guide.getBitmap();
	
	BitmapDrawable bmpDraw_tool=(BitmapDrawable)getResources().getDrawable(R.drawable.main_tool);
	Bitmap bmp_tool=bmpDraw_tool.getBitmap();
	
	private int bmp_width = bmp_mabiao.getWidth();
	private int bmp_height = bmp_mabiao.getHeight();
	private int bmp_gap= bmp_height-bmp_width;
	
	public RoundView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		// 获取圆心坐标
		mPointX = this.getMeasuredWidth()/2;
		mPointY = this.getMeasuredHeight()/2;
		
		outRadius = (int)(mPointX*(0.5f));
		// 初始化半径
		exRadius = (int)(mPointX*(0.42f));
		inRadius = (int)(exRadius*(0.44f));
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.drawARGB(255, 52, 52, 52);
		// 去锯齿
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL);
		
		// 绘制外环
		mPaint.setARGB(255, 38, 38, 38);
		canvas.drawCircle(mPointX, mPointY, outRadius, mPaint);
		// 绘制外圆
		mPaint.setARGB(255, 52, 52, 52);;
		canvas.drawCircle(mPointX, mPointY, exRadius, mPaint);
		
		RectF oval = new RectF(mPointX - exRadius, mPointY - exRadius, mPointX + exRadius, mPointY + exRadius);
		switch (isTouchDown) {
		case COM_MTT_ROUNDVIEW_TOUCH_MABIAO:
			mPaint.setARGB(255, 232, 26, 41);
			canvas.drawArc(oval, 225, 90, true, mPaint);
			break;
		case COM_MTT_ROUNDVIEW_TOUCH_GUIDE:
			mPaint.setARGB(255, 232, 26, 41);
			canvas.drawArc(oval, 135, 90, true, mPaint);
			break;
		case COM_MTT_ROUNDVIEW_TOUCH_CAMERA:
			mPaint.setARGB(255, 232, 26, 41);
			canvas.drawArc(oval, 315, 90, true, mPaint);
			break;
		case COM_MTT_ROUNDVIEW_TOUCH_TOOL:
			mPaint.setARGB(255, 232, 26, 41);
			canvas.drawArc(oval, 45, 90, true, mPaint);
			break;
		default:
			break;
		}
		
		// 绘制内圆
		mPaint.setARGB(255, 38, 38, 38);
		canvas.drawCircle(mPointX, mPointY, inRadius, mPaint);
		
		// ----------设置后绘制四条直线----------
		mPaint.setARGB(255, 38, 38, 38);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(4);
		
		Path patha = new Path();
		patha.moveTo((float)(mPointX - inRadius*Math.sqrt(2)/2), (float)(mPointY - inRadius*Math.sqrt(2)/2) );
		patha.lineTo((float)(mPointX - exRadius*Math.sqrt(2)/2), (float)(mPointY - exRadius*Math.sqrt(2)/2));
		patha.close();
		canvas.drawPath(patha, mPaint);
		
		Path pathb = new Path();
		pathb.moveTo((float)(mPointX + inRadius*Math.sqrt(2)/2), (float)(mPointY - inRadius*Math.sqrt(2)/2));
		pathb.lineTo((float)(mPointX + exRadius*Math.sqrt(2)/2), (float)(mPointY - exRadius*Math.sqrt(2)/2));
		pathb.close();
		canvas.drawPath(pathb, mPaint);
		
		Path pathc = new Path();
		pathc.moveTo((float)(mPointX - inRadius*Math.sqrt(2)/2), (float)(mPointY + inRadius*Math.sqrt(2)/2));
		pathc.lineTo((float)(mPointX - exRadius*Math.sqrt(2)/2), (float)(mPointY + exRadius*Math.sqrt(2)/2));
		pathc.close();
		canvas.drawPath(pathc, mPaint);
		
		Path pathd = new Path();
		pathd.moveTo((float)(mPointX + inRadius*Math.sqrt(2)/2), (float)(mPointY + inRadius*Math.sqrt(2)/2));
		pathd.lineTo((float)(mPointX + exRadius*Math.sqrt(2)/2), (float)(mPointY + exRadius*Math.sqrt(2)/2));
		pathd.close();
		canvas.drawPath(pathd, mPaint);
		
		// ------------------绘制4个图片----------------------
		// 码表
		int x1 = mPointX;
		int y1 = (int)(mPointY - exRadius*0.7f);
		RectF dst_t1 = new RectF(x1-bmp_width/2,y1-bmp_height/2,x1+bmp_width/2,y1+bmp_height/2);
		canvas.drawBitmap(bmp_mabiao, null, dst_t1, mPaint);
		
		// 工具
		int x2 = mPointX;
		int y2 = (int)(mPointY + exRadius*0.7f);
		RectF dst_t2 = new RectF(x2-bmp_width/2,y2-bmp_height/2,x2+bmp_width/2,y2+bmp_height/2);
		canvas.drawBitmap(bmp_tool, null, dst_t2, mPaint);
		
		// 相机
		int x3 = (int)(mPointX +exRadius*0.7f-bmp_gap/2);
		int y3 = mPointY;
		RectF dst_t3 = new RectF(x3-bmp_width/2,y3-bmp_height/2,x3+bmp_width/2,y3+bmp_height/2);
		canvas.drawBitmap(bmp_camera, null, dst_t3, mPaint);
		
		// 导航		
		int x4 = (int)(mPointX -exRadius*0.7f+bmp_gap/2);
		int y4 = mPointY;
		RectF dst_t4 = new RectF(x4-bmp_width/2,y4-bmp_height/2,x4+bmp_width/2,y4+bmp_height/2);
		canvas.drawBitmap(bmp_guide, null, dst_t4, mPaint);
		
	}

	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int x,y;
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			x = (int) event.getX();
			y = (int) event.getY();
			switch (touchPositon(x, y)) {
			case COM_MTT_ROUNDVIEW_TOUCH_MABIAO:
				isTouchDown = COM_MTT_ROUNDVIEW_TOUCH_MABIAO;
				invalidate();
				break;
			case COM_MTT_ROUNDVIEW_TOUCH_GUIDE:
				isTouchDown = COM_MTT_ROUNDVIEW_TOUCH_GUIDE;
				invalidate();
				break;
			case COM_MTT_ROUNDVIEW_TOUCH_CAMERA:
				isTouchDown = COM_MTT_ROUNDVIEW_TOUCH_CAMERA;
				invalidate();
				break;
			case COM_MTT_ROUNDVIEW_TOUCH_TOOL:
				isTouchDown = COM_MTT_ROUNDVIEW_TOUCH_TOOL;
				invalidate();
				break;
			case COM_MTT_ROUNDVIEW_TOUCH_START:
				isTouchDown = COM_MTT_ROUNDVIEW_TOUCH_START;
			default:
				break;
			}
			
		}else if (event.getAction() == MotionEvent.ACTION_UP) {
			x = (int) event.getX();
			y = (int) event.getY();
			switch (touchPositon(x, y)) {
			case COM_MTT_ROUNDVIEW_TOUCH_MABIAO:
	            return mOnTouchListener.onTouchEvent(this, event,COM_MTT_ROUNDVIEW_TOUCH_MABIAO); 
	            
			case COM_MTT_ROUNDVIEW_TOUCH_GUIDE:
	            return mOnTouchListener.onTouchEvent(this, event,COM_MTT_ROUNDVIEW_TOUCH_GUIDE);  
			
			case COM_MTT_ROUNDVIEW_TOUCH_CAMERA:
	            return mOnTouchListener.onTouchEvent(this, event,COM_MTT_ROUNDVIEW_TOUCH_CAMERA);  
			
			case COM_MTT_ROUNDVIEW_TOUCH_TOOL:
	            return mOnTouchListener.onTouchEvent(this, event,COM_MTT_ROUNDVIEW_TOUCH_TOOL); 
			case COM_MTT_ROUNDVIEW_TOUCH_START:
				return mOnTouchListener.onTouchEvent(this, event, COM_MTT_ROUNDVIEW_TOUCH_START);

			default:
				resetTouch();
				invalidate();
				break;
			}
		}
		
		return true;
	}
	
	/**判断点击点是否在圆环内
	 * @param x 横坐标
	 * @param y 纵坐标
	 *  */
	private boolean isInRound(int x,int y){
		if(((x-mPointX)*(x-mPointX) + (y-mPointY)*(y-mPointY)) < exRadius*exRadius && 
				((x-mPointX)*(x-mPointX) + (y-mPointY)*(y-mPointY)) > inRadius*inRadius){
			return true;
		}else{
			return false;
		}
	}
	
	/** 
	 * 判断点击点是否在开始按钮中
	 * */
	private boolean isStart(int x,int y){
		if(((x-mPointX)*(x-mPointX) + (y-mPointY)*(y-mPointY)) < inRadius*inRadius){
			return true;
		}else {
			return false;
		}
	}
	
	/**点击点所在的位置
	 * @param x 横坐标
	 * @param y 纵坐标
	 * @return 1~6
	 *  */
	private int touchPositon(int x,int y){
		int result = 0;
		
		int i = Math.abs(x-mPointX);
		double j = Math.sqrt((x-mPointX)*(x-mPointX) + (y-mPointY)*(y-mPointY));
		double arc = arccos(i/j);
		
		if(arc>45 && y<mPointY && isInRound(x, y)){
			result = COM_MTT_ROUNDVIEW_TOUCH_MABIAO;
		}else if (isInRound(x, y) && arc<45 && x<mPointX){
			result = COM_MTT_ROUNDVIEW_TOUCH_GUIDE;
		}else if (isInRound(x, y) && arc<45 && x>mPointX){
			result = COM_MTT_ROUNDVIEW_TOUCH_CAMERA;
		}else if (isInRound(x, y) && arc>45 && y>mPointY){
			result = COM_MTT_ROUNDVIEW_TOUCH_TOOL;
		}else if (isStart(x, y)) {
			result = COM_MTT_ROUNDVIEW_TOUCH_START;
		}
		
		return result;
	}
	
    public void setOnTouchListener(RoundViewOnTouchListener mOnTouchListener){  
        this.mOnTouchListener=mOnTouchListener;  
    }  

	/** 
	   * 余弦反算函数，精度到0.03秒 
	   * @param a double 余弦值 
	   * @return double   角度(360) 
	   */ 
	public double arccos(double a) 
	{ 
	    double b = 90.0, c0 = 0.0, c1 = 180.0; 
	    if (a < 1 && a > -1) 
	    { 
	      do 
	      { 
	            if (Math.cos(b * Math.PI / 180) >= a) 
	            { 
	               c0 = b; 
	               b = (c0 + c1) / 2; 
	            } 
	            if (Math.cos(b * Math.PI / 180) <= a) 
	            { 
	               c1 = b; 
	               b = (c0 + c1) / 2; 
	            } 
	      } 
	      while (Math.abs(c0 - c1) > 0.00001); 
	    } 
	    return b; 
	}
	
	/** 重置按钮*/
	public void resetTouch(){
		isTouchDown = 0;
	}
	
}

package com.mtt.customview;

import com.mtt.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Paint.Align;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Scroller;

/** 
 * 自定义弹出对话框，可拖拽
 * @author Kerry
 * */
public class DialogView extends View{

	private Paint mPaint = new Paint();
	/** Scroller 拖动类 */
    private Scroller mScroller;
	/** 点击时候Y的坐标*/
	private int downY = 0;
	/** 拖动时候Y的坐标*/
	private int moveY = 0;
	/** 拖动时候Y的方向距离*/
	private int scrollY = 0;
	/** 松开时候Y的坐标*/
	private int upY = 0;
	/** 滑动动画时间 */
	public int mDuration = 800;

	/** 圆心坐标 */
	public int mPointX = 0, mPointY = 0;
	/** 矩形的长和宽*/
	public int mWidth = 0, mHeight = 0;
	
	/** view的展示状态*/
	private boolean isShow = false;	
	
	public DialogView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		// 获取圆心坐标
		mPointX = this.getWindowWidth(context)/2;
		mPointY = this.getWindowHeigh(context)/2;
		// 获取矩形长和宽
		mWidth = this.getWindowWidth(context)/2;
		mHeight = this.getWindowHeigh(context)/2;
		
		mScroller = new Scroller(context);
		
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		canvas.drawARGB(0, 0, 0, 0);
		// 去锯齿
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setARGB(255, 255, 255, 255);
		// 绘制矩形
		canvas.drawRect(mPointX*3/2-mWidth, mPointY-mHeight/2+mPointY*3/2, mPointX*3/2, mPointY+mHeight/2+mPointY*3/2, mPaint);
		
		// 定义一个Path对象，封闭成一个四角形
		mPaint.setARGB(255, 232, 25, 41);
		Path path1 = new Path();
		path1.moveTo(mPointX-0.13f*mPointX, mPointY/2+mPointY*3/2);
		path1.lineTo(mPointX-0.2f*mPointX, mPointY*4/16+mPointY*3/2);
		path1.lineTo(mPointX+0.2f*mPointX, mPointY*4/16+mPointY*3/2);
		path1.lineTo(mPointX+0.13f*mPointX, mPointY/2+mPointY*3/2);
		path1.close();
		canvas.drawPath(path1, mPaint);
		
		// 定义一个Path对象，封闭三个弧形
		mPaint.setARGB(255, 255, 255, 255);
		Path path2 = new Path();
		path2.moveTo(mPointX/2, mPointY/2+mPointY*3/2);
        path2.quadTo(mPointX-0.4f*mPointX, mPointY*4/16+mPointY*3/2, mPointX-0.3f*mPointX, mPointY*4/16+mPointY*3/2);
        path2.lineTo(mPointX-0.2f*mPointX, mPointY*4/16+mPointY*3/2);
        path2.lineTo(mPointX-0.13f*mPointX, mPointY/2+mPointY*3/2);
        path2.close();
        canvas.drawPath(path2, mPaint);
        
		Path path3 = new Path();
		path3.moveTo(mPointX*3/2, mPointY/2+mPointY*3/2);
        path3.quadTo(1.4f*mPointX, mPointY*4/16+mPointY*3/2, mPointX+0.3f*mPointX, mPointY*4/16+mPointY*3/2);
        path3.lineTo(mPointX+0.2f*mPointX, mPointY*4/16+mPointY*3/2);
		path3.lineTo(mPointX+0.13f*mPointX, mPointY/2+mPointY*3/2);
		path3.close();
		canvas.drawPath(path3, mPaint);
		
		Path path4 = new Path();
		path4.moveTo(mPointX/2, mPointY*1.5f+mPointY*3/2);
		path4.quadTo(mPointX-0.4f*mPointX, mPointY*28/16+mPointY*3/2, mPointX-0.3f*mPointX, mPointY*28/16+mPointY*3/2);
		path4.lineTo(mPointX+0.3f*mPointX, mPointY*28/16+mPointY*3/2);
		path4.quadTo(1.4f*mPointX, mPointY*28/16+mPointY*3/2, mPointX*3/2, mPointY*1.5f+mPointY*3/2);
		path4.close();
		canvas.drawPath(path4, mPaint);
		
		// ----------设置后绘制直线----------
		mPaint.setARGB(255, 184, 184, 184);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(3);
		
		Path patha = new Path();
		patha.moveTo(mPointX,mPointY-mHeight/2+mPointY*3/2);
		patha.lineTo(mPointX,mPointY+mHeight/2+mPointY*3/2);
		patha.close();
		canvas.drawPath(patha, mPaint);
		
		Path pathb = new Path();
		pathb.moveTo(mPointX-mWidth/2,mPointY+mPointY*3/2);
		pathb.lineTo(mPointX+mWidth/2,mPointY+mPointY*3/2);
		pathb.close();
		canvas.drawPath(pathb, mPaint);
		
		Path pathc = new Path();
		pathc.moveTo(mPointX/2,mPointY/2+mPointY*3/2);
		pathc.lineTo(mPointX*3/2, mPointY/2+mPointY*3/2);
		pathc.close();
		canvas.drawPath(pathc,mPaint);
		
		Path pathd = new Path();
		pathd.moveTo(mPointX/2,mPointY*1.5f+mPointY*3/2);
		pathd.lineTo(mPointX*3/2, mPointY*1.5f+mPointY*3/2);
		pathd.close();
		canvas.drawPath(pathd,mPaint);
		
		// ----------设置字符大小后绘制字符----------
		mPaint.setStrokeWidth(5);
		mPaint.setARGB(255, 232, 25, 41);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setTextSize(60);
		mPaint.setFakeBoldText(true); //true为粗体，false为非粗体
		mPaint.setShader(null);
		mPaint.setTextAlign(Align.CENTER);
		
		canvas.drawText(getResources().getString(R.string.home_back), mPointX, mPointY*13/8+mPointY*3/2, mPaint);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(event);
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int x,y;
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			downY = (int) event.getY();
			x = (int) event.getX();
			y = (int) event.getY();
			if(!isShow){
				if(couldShow(x, y)){
					Log.d("DialogView","ScrollX = " + getScrollX() +"ScrollY = " + getScrollY() );
					return true;
				}
			}else{
				if(couldTouchClose(x, y)){
//					DialogView.this.startMoveAnim(mPointY*3/2, -mPointY*3/2, mDuration);
//					isShow = false;
//					changed();
					break;
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			moveY = (int) event.getY();
			scrollY = moveY - downY;
			//向下滑动
			if (scrollY > 0) {
//				if(isShow){
//					scrollTo(0, -Math.abs(scrollY));
//				}
			}else{
				Log.d("DialogView","Top=" + getScrollY());
				if(mPointY*2 - this.getScrollY()>=mPointY*1/2 && !isShow){
						scrollTo(0, Math.abs(- scrollY));
				}
			}
			break;
		case MotionEvent.ACTION_UP:
			x = (int) event.getX();
			y = (int) event.getY();
			upY = (int) event.getY();
			if(isShow){
				} else {
						if( this.getScrollY() >= mPointY*4/5){
							Log.d("DialogView","ScrollX = " + getScrollX() +"ScrollY = " + getScrollY() );
							startMoveAnim(this.getScrollY(),mPointY*3/2 - this.getScrollY(), mDuration);
							isShow = true;
							changed();
						} else {
							if(Math.abs(upY-downY)>mPointY/8){
								Log.d("DialogView","ScrollX = " + getScrollX() +"ScrollY = " + getScrollY() );
								startMoveAnim(this.getScrollY(), -this.getScrollY(), mDuration);
								isShow = false;
								changed();
							}else{
								startMoveAnim(this.getScrollY(), mPointY*3/2 - this.getScrollY(), mDuration);
								isShow = true;
								changed();
							}
						}
				}
			break;
		}
		return super.onTouchEvent(event);
	}

	/** 判断点击点是否在可触发范围内,若触发则展开dialog
	 * @param x x点位置
	 * @param y y点位置
	 * @return true/false*/
	public boolean couldShow(int x,int y){
		if(x<mPointX*3/2 && x>mPointX/2 && y<mPointY*2 && y>mPointY*7/4){
			return true;
		}else{
			return false;
		}
	}
	
	/** 判断点击点是否在可触发范围内，如果在则可通过drag关闭dialog
	 * @param x x点位置
	 * @param y y点位置
	 * @return true/false*/
	public boolean couldDragClose(int x,int y){
		if(x<mPointX*3/2 && x>mPointX/2 && y>mPointY*1/4 && y<mPointY/2){
			return true;
		}else{
			return false;
		}
	}
	
	/** 判断点击点是否在可触发范围内，如果在则可通过点击关闭dialog
	 * @param x x点位置
	 * @param y y点位置
	 * @return true/false*/
	public boolean couldTouchClose(int x,int y){
		if(x<mPointX*3/2 && x>mPointX/2 && y<mPointY*7/4 && y>mPointY/4){
			return false;
		}else{
			return true;
		}
	}
	
	public int getWindowWidth(Context context) {
		// 获取屏幕分辨率
		WindowManager wm = (WindowManager) (context
				.getSystemService(Context.WINDOW_SERVICE));
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		int mScreenWidth = dm.widthPixels;
		return mScreenWidth;
	}

	public int getWindowHeigh(Context context) {
		// 获取屏幕分辨率
		WindowManager wm = (WindowManager) (context
				.getSystemService(Context.WINDOW_SERVICE));
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		int mScreenHeigh = dm.heightPixels;
		return mScreenHeigh;
	}
	
	/**
	 * 拖动动画
	 * @param startY  
	 * @param dy  移动到某点的Y坐标距离
	 * @param duration 时间
	 */
	public void startMoveAnim(int startY, int dy, int duration) {
		mScroller.startScroll(0, startY, 0, dy, duration);
		invalidate();//通知UI线程的更新
	}
	
	@Override
	public void computeScroll() {
		if (mScroller.computeScrollOffset()) {
			scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
			// 更新界面
			postInvalidate();
		} else {
		}
		super.computeScroll();
	}
	
	/** 开打界面 */
	public void show(){
		if(!isShow){
			DialogView.this.startMoveAnim(0, mPointY*3/2, mDuration);
			isShow = true;
			changed();
		}
	}
	
	/** 关闭界面 */
	public void dismiss(){
		if(isShow){
			DialogView.this.startMoveAnim(mPointY*3/2, -mPointY*3/2, mDuration);
			isShow = false;
			changed();
			
		}
	}
	
	/**
	 * 设置监听接口，实现遮罩层效果
	 */
	public void setOnStatusListener(onStatusListener listener){
		this.statusListener = listener;
	}
	
	/** 监听接口*/
	public onStatusListener statusListener;
	
	/**
	 * 监听接口，来在主界面监听界面变化状态
	 */
	public interface onStatusListener{		
		/**  开打状态  */
		public void onShow();
		/**  关闭状态  */
		public void onDismiss();
	}
	
	/**
	 * 显示状态发生改变时候执行回调借口
	 */
	public void changed(){
		if(statusListener != null){
			if(isShow){
				statusListener.onShow();
			}else{
				statusListener.onDismiss();
			}
		}
	}
	
	public boolean isShow(){
		return isShow;
	}
	
	public void setShow(boolean b){
		isShow = b;
	}
	
}
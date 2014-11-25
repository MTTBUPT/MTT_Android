package com.mtt.customview;

import com.mtt.R;
import com.mtt.view.SubFunctionActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Scroller;

/** 
 * 自定义弹出对话框(Switch)，可拖拽
 * @author Kerry
 * */
public class SwitchDialogView extends View{

	public static final int COM_MTT_START_MUSIC = 1;
	public static final int COM_MTT_START_DATA = 2;
	public static final int COM_MTT_START_PATH = 3;
	public static final int COM_MTT_START_STOPWATCH = 4;
	public static final int COM_MTT_START = 5;

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
	public int mDuration = 1200;

	/** 圆心坐标 */
	public int mPointX = 0, mPointY = 0;
	/** 矩形的长和宽*/
	public float mWidth = 0, mHeight = 0;
	/** 头标高度*/
	public float tabHeight = 0;
	/** view向上隐藏的高度*/
	public float moveHeight = 0;
	/** tab点按时的冗余*/
	public float tabMargin = 2.0f;
	
	/** view的展示状态*/
	private boolean isShow = false;	
	
	/** 是否点击*/
	private boolean isTouchData = false; 
	private boolean isTouchPath = false;
	
	// 读取InputStream并得到位图
	BitmapDrawable bmpDraw_music=(BitmapDrawable)getResources().getDrawable(R.drawable.dialog_music);
	Bitmap bmp_music=bmpDraw_music.getBitmap();
	BitmapDrawable bmpDraw_stopwatch=(BitmapDrawable)getResources().getDrawable(R.drawable.dialog_stopwatch);
	Bitmap bmp_stopwatch=bmpDraw_stopwatch.getBitmap();
	
	BitmapDrawable bmpDraw_close_data=(BitmapDrawable)getResources().getDrawable(R.drawable.dialog_close_data);
	Bitmap bmp_close_data=bmpDraw_close_data.getBitmap();
	BitmapDrawable bmpDraw_start_data=(BitmapDrawable)getResources().getDrawable(R.drawable.dialog_start_data);
	Bitmap bmp_start_data=bmpDraw_start_data.getBitmap();
	
	BitmapDrawable bmpDraw_close_path=(BitmapDrawable)getResources().getDrawable(R.drawable.dialog_close_path);
	Bitmap bmp_close_path=bmpDraw_close_path.getBitmap();
	BitmapDrawable bmpDraw_start_path=(BitmapDrawable)getResources().getDrawable(R.drawable.dialog_start_path);
	Bitmap bmp_start_path=bmpDraw_start_path.getBitmap();
	
	private int bmp_width = bmp_music.getWidth();
	private int bmp_height = bmp_stopwatch.getHeight();
	
	public SwitchDialogView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		// 获取圆心坐标
		mPointX = this.getWindowWidth(context)/2;
		mPointY = this.getWindowHeigh(context)/2;
		
		// 获取矩形长和宽
		mWidth = this.getWindowWidth(context)*0.47f;
		mHeight = this.getWindowHeigh(context)*0.28f;
		tabHeight = this.getWindowHeigh(context)*0.107f;
		
		moveHeight = mPointY+mHeight/2+tabHeight;
//		moveHeight = 0;
		// 获取滑动对象
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
		mPaint.setARGB(255, 52, 52, 52);
		
		// -------------------绘制中间矩形-----------------------------------
		// 绘制矩形（正中）
		canvas.drawRect(mPointX-mWidth/2, mPointY-mHeight/2-moveHeight, mPointX+mWidth/2, mPointY+mHeight/2-moveHeight, mPaint);
		
		// -------------------绘制下边框-----------------------------------
		// 绘制矩形（下中）
		canvas.drawRect(mPointX-mWidth/2+tabHeight, mPointY+mHeight/2-moveHeight, mPointX+mWidth/2-tabHeight, mPointY+mHeight/2+tabHeight-moveHeight, mPaint);
		
		// 绘制扇形（下左）
		RectF oval_bottom_left = new RectF(mPointX-mWidth/2, mPointY+mHeight/2-tabHeight-moveHeight, mPointX-mWidth/2+tabHeight*2, mPointY+mHeight/2+tabHeight-moveHeight);
		canvas.drawArc(oval_bottom_left, 90, 90, true, mPaint);
		// 绘制扇形（下右）
		RectF oval_bottom_right = new RectF(mPointX+mWidth/2-tabHeight*2, mPointY+mHeight/2-tabHeight-moveHeight, mPointX+mWidth/2, mPointY+mHeight/2+tabHeight-moveHeight);
		canvas.drawArc(oval_bottom_right, 0, 90, true, mPaint);
		
		// -------------------------设置后绘制直线-------------------------
		mPaint.setARGB(255, 86, 86, 86);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(1);
		
		// 矩形下边
		Path patha = new Path();
		patha.moveTo(mPointX-mWidth/2,mPointY+mHeight/2-moveHeight);
		patha.lineTo(mPointX+mWidth/2,mPointY+mHeight/2-moveHeight);
		patha.close();
		canvas.drawPath(patha, mPaint);
		
		// 上横边
		Path pathb = new Path();
		pathb.moveTo(mPointX-mWidth/2,mPointY-mHeight/2+mHeight/5-moveHeight);
		pathb.lineTo(mPointX+mWidth/2,mPointY-mHeight/2+mHeight/5-moveHeight);
		pathb.close();
		canvas.drawPath(pathb, mPaint);
		
		// 竖边1
		Path pathc = new Path();
		pathc.moveTo(mPointX-mWidth/4,mPointY-mHeight/2-moveHeight);
		pathc.lineTo(mPointX-mWidth/4,mPointY+mHeight/2-moveHeight);
		pathc.close();
		canvas.drawPath(pathc, mPaint);
		// 竖边2
		Path pathd = new Path();
		pathd.moveTo(mPointX,mPointY-mHeight/2-moveHeight);
		pathd.lineTo(mPointX,mPointY+mHeight/2-moveHeight);
		pathd.close();
		canvas.drawPath(pathd, mPaint);
		// 竖边3
		Path pathe= new Path();
		pathe.moveTo(mPointX+mWidth/4,mPointY-mHeight/2-moveHeight);
		pathe.lineTo(mPointX+mWidth/4,mPointY+mHeight/2-moveHeight);
		pathe.close();
		canvas.drawPath(pathe, mPaint);
		
		// ---------------------绘制点击tab-------------------------------
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setARGB(255, 232, 26, 41);

		// 绘制Data Tab
		if(isTouchData){
			canvas.drawRect(mPointX-mWidth/4+tabMargin, mPointY-mHeight*3/10-moveHeight+tabMargin, mPointX-tabMargin, mPointY+mHeight/2-moveHeight-tabMargin, mPaint);
		}
			
		if (isTouchPath) {
			canvas.drawRect(mPointX+tabMargin, mPointY-mHeight*3/10-moveHeight+tabMargin, mPointX+mWidth/4-tabMargin, mPointY+mHeight/2-moveHeight-tabMargin, mPaint);
		}
		
		//-----------------------绘制图片--------------------------------------
		int x1 = (int)(mPointX - mWidth*3/8);
		int y1 = (int)(mPointY-moveHeight-mHeight/7);
		RectF dst_t1 = new RectF(x1-bmp_width/2,y1-bmp_height/2,x1+bmp_width/2,y1+bmp_height/2);
		canvas.drawBitmap(bmp_music, null, dst_t1, mPaint);
		
		int x2 = (int)(mPointX - mWidth*1/8);
		int y2 = (int)(mPointY-moveHeight-mHeight/6);
		RectF dst_t2 = new RectF(x2-bmp_width/2,y2-bmp_height/2,x2+bmp_width/2,y2+bmp_height/2);
		if(isTouchData){
			canvas.drawBitmap(bmp_close_data, null, dst_t2, mPaint);
		}else {
			canvas.drawBitmap(bmp_start_data, null, dst_t2, mPaint);
		}

		int x3 = (int)(mPointX + mWidth*1/8);
		int y3 = (int)(mPointY-moveHeight-mHeight/6);
		RectF dst_t3 = new RectF(x3-bmp_width/2,y3-bmp_height/2,x3+bmp_width/2,y3+bmp_height/2);
		if(isTouchPath){
			canvas.drawBitmap(bmp_close_path, null, dst_t3, mPaint);
		}else {
			canvas.drawBitmap(bmp_start_path, null, dst_t3, mPaint);
		}
		
		int x4 = (int)(mPointX + mWidth*3/8);
		int y4 = (int)(mPointY-moveHeight-mHeight/7);
		RectF dst_t4 = new RectF(x4-bmp_width/2,y4-bmp_height/2,x4+bmp_width/2,y4+bmp_height/2);
		canvas.drawBitmap(bmp_stopwatch, null, dst_t4, mPaint);
		
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(event);
	}
	

	// -------------------区域判断--------------------------------
	
	
	/** 判断点击点是否在可触发范围内，如果在则可通过点击关闭dialog
	 * @param x x点位置
	 * @param y y点位置
	 * @return true/false
	 * */
	public boolean couldTouchClose(int x,int y){
		if(x<mPointX+mWidth/2 && x>mPointX-mWidth/2 && y>0 && y<mHeight+tabHeight){
			return false;
		}else{
			return true;
		}
	}
	
	/** 判断点击点的位置*/
	public int TouchTab(int x,int y){
		if (x>mPointX-mWidth/2 && x<mPointX-mWidth/4 && y>mHeight/5 && y<mHeight) {
			return COM_MTT_START_MUSIC;
		} else if(x>mPointX-mWidth/4 && x<mPointX && y>mHeight/5 && y<mHeight){
			return COM_MTT_START_DATA;
		}else if (x>mPointX && x<mPointX+mWidth/4 && y>mHeight/5 && y<mHeight) {
			return COM_MTT_START_PATH;
		}else if (x>mPointX+mWidth/4 && x<mPointX+mWidth/2 && y>mHeight/5 && y<mHeight) {
			return COM_MTT_START_STOPWATCH;
		}else if (x>mPointX-mWidth/2 && x<mPointX+mWidth/2 && y>mHeight && y<mHeight+tabHeight) {
			return COM_MTT_START;
		}
		return 0;
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
			SwitchDialogView.this.startMoveAnim(0, -(int)(mHeight+tabHeight), mDuration);
			isShow = true;
			changed();
		}
	}
	
	/** 关闭界面 */
	public void dismiss(){
		if(isShow){
			SwitchDialogView.this.startMoveAnim(-(int)(mHeight+tabHeight), (int)(mHeight+tabHeight), mDuration);
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
	
	/** 是否展开*/
	public boolean isShow(){
		return isShow;
	}
	
	/** 设置展开状态*/
	public void setShow(boolean b){
		isShow = b;
	}
	
	/** 设置Data tab 是否被按*/
	public void setIsTouchData(boolean b){
		isTouchData = b;
		invalidate();
	}
	/** 获取Data tab 状态*/
	public boolean getIsTouchData(){
		return isTouchData;
	}
	
	/** 设置Path tab是否被按*/
	public void setIsTouchPath(boolean b){
		isTouchPath = b;
		invalidate();
	}
	/** 获取Path data 状态*/
	public boolean getIsTouchPath(){
		return isTouchPath;
	}
	
	/** 获取屏幕的宽度*/
	public int getWindowWidth(Context context) {
		// 获取屏幕分辨率
		WindowManager wm = (WindowManager) (context
				.getSystemService(Context.WINDOW_SERVICE));
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		int mScreenWidth = dm.widthPixels;
		return mScreenWidth;
	}

	/** 获取屏幕的高度*/
	public int getWindowHeigh(Context context) {
		// 获取屏幕分辨率
		WindowManager wm = (WindowManager) (context
				.getSystemService(Context.WINDOW_SERVICE));
		DisplayMetrics dm = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(dm);
		int mScreenHeigh = dm.heightPixels;
		return mScreenHeigh;
	}
	
}

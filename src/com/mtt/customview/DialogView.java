package com.mtt.customview;

import com.mtt.R;

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
	public float mWidth = 0, mHeight = 0;
	/** 头标高度*/
	public float tabHeight = 0;
	
	/** view的展示状态*/
	private boolean isShow = false;	
	/** view所在的页面*/
	private int pageNum = 0;
	
	// 读取InputStream并得到位图
	/** 弹窗头标*/
	BitmapDrawable bmpDraw_mabiao=(BitmapDrawable)getResources().getDrawable(R.drawable.subfunction_mabiao);
	Bitmap bmp_mabiao=bmpDraw_mabiao.getBitmap();
	BitmapDrawable bmpDraw_camera=(BitmapDrawable)getResources().getDrawable(R.drawable.subfunction_camera);
	Bitmap bmp_camera=bmpDraw_camera.getBitmap();
	BitmapDrawable bmpDraw_music=(BitmapDrawable)getResources().getDrawable(R.drawable.subfunction_music);
	Bitmap bmp_music=bmpDraw_music.getBitmap();
	BitmapDrawable bmpDraw_stopwatch=(BitmapDrawable)getResources().getDrawable(R.drawable.subfunction_stopwatch);
	Bitmap bmp_stopwatch=bmpDraw_stopwatch.getBitmap();
	BitmapDrawable bmpDraw_guide=(BitmapDrawable)getResources().getDrawable(R.drawable.subfunction_guide);
	Bitmap bmp_guide=bmpDraw_guide.getBitmap();
	BitmapDrawable bmpDraw_path=(BitmapDrawable)getResources().getDrawable(R.drawable.subfunction_path);
	Bitmap bmp_path=bmpDraw_path.getBitmap();
	/** 位图的宽度*/
	private int bmp_width = bmp_mabiao.getWidth();
	
	/** 弹窗内容图标*/
	// 码表页面
	BitmapDrawable bmpDraw_mabiao_resetspeep=(BitmapDrawable)getResources().getDrawable(R.drawable.mabiao_resetspeed);
	Bitmap bmp_mabiao_resetspeed=bmpDraw_mabiao_resetspeep.getBitmap();
	BitmapDrawable bmpDraw_mabiao_resetmileage=(BitmapDrawable)getResources().getDrawable(R.drawable.mabiao_resetmileage);
	Bitmap bmp_mabiao_resetmileage=bmpDraw_mabiao_resetmileage.getBitmap();
	BitmapDrawable bmpDraw_mabiao_resetsteep=(BitmapDrawable)getResources().getDrawable(R.drawable.mabiao_resetsteep);
	Bitmap bmp_mabiao_resetsteep=bmpDraw_mabiao_resetsteep.getBitmap();
	BitmapDrawable bmpDraw_mabiao_resettime=(BitmapDrawable)getResources().getDrawable(R.drawable.mabiao_resettime);
	Bitmap bmp_mabiao_resettime=bmpDraw_mabiao_resettime.getBitmap();
	
	// 相机页面
	BitmapDrawable bmpDraw_camera_delay=(BitmapDrawable)getResources().getDrawable(R.drawable.camera_delay);
	Bitmap bmp_camera_delay=bmpDraw_camera_delay.getBitmap();
	BitmapDrawable bmpDraw_camera_storage=(BitmapDrawable)getResources().getDrawable(R.drawable.camera_storage);
	Bitmap bmp_camera_storage=bmpDraw_camera_storage.getBitmap();
	
	// 音乐页面
	BitmapDrawable bmpDraw_music_cycle=(BitmapDrawable)getResources().getDrawable(R.drawable.music_cycle);
	Bitmap bmp_music_cycle=bmpDraw_music_cycle.getBitmap();
	BitmapDrawable bmpDraw_music_random=(BitmapDrawable)getResources().getDrawable(R.drawable.music_random);
	Bitmap bmp_music_random=bmpDraw_music_random.getBitmap();
	
	// 导航页面
	BitmapDrawable bmpDraw_guide_setpath=(BitmapDrawable)getResources().getDrawable(R.drawable.guide_setpath);
	Bitmap bmp_guide_setpath=bmpDraw_guide_setpath.getBitmap();
	BitmapDrawable bmpDraw_guide_summary=(BitmapDrawable)getResources().getDrawable(R.drawable.guide_summary);
	Bitmap bmp_guide_summary=bmpDraw_guide_summary.getBitmap();
	BitmapDrawable bmpDraw_guide_offlinemap=(BitmapDrawable)getResources().getDrawable(R.drawable.guide_offlinemap);
	Bitmap bmp_guide_offlinemap=bmpDraw_guide_offlinemap.getBitmap();
	BitmapDrawable bmpDraw_guide_information=(BitmapDrawable)getResources().getDrawable(R.drawable.guide_information);
	Bitmap bmp_guide_information=bmpDraw_guide_information.getBitmap();	
	
	// 轨迹页面
	BitmapDrawable bmpDraw_path_resetstart=(BitmapDrawable)getResources().getDrawable(R.drawable.path_resetstart);
	Bitmap bmp_path_resetstart=bmpDraw_path_resetstart.getBitmap();
	BitmapDrawable bmpDraw_path_save=(BitmapDrawable)getResources().getDrawable(R.drawable.path_save);
	Bitmap bmp_path_save=bmpDraw_path_save.getBitmap();
	BitmapDrawable bmpDraw_path_continue=(BitmapDrawable)getResources().getDrawable(R.drawable.path_continue);
	Bitmap bmp_path_continue=bmpDraw_path_continue.getBitmap();
	BitmapDrawable bmpDraw_path_share=(BitmapDrawable)getResources().getDrawable(R.drawable.path_share);
	Bitmap bmp_path_share=bmpDraw_path_share.getBitmap();	
	
	/** 位图的宽度*/
	private int bmp_content_width = bmp_mabiao_resetspeed.getWidth();
	
	public DialogView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		
		// 获取圆心坐标
		mPointX = this.getWindowWidth(context)/2;
		mPointY = this.getWindowHeigh(context)/2;
		// 获取矩形长和宽
		mWidth = this.getWindowWidth(context)*0.47f;
		mHeight = this.getWindowHeigh(context)*0.57f;
		tabHeight = this.getWindowHeigh(context)*0.107f;
		
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
		// -------------------绘制中间-----------------------------------
		// 绘制矩形（正中）
		canvas.drawRect(mPointX-mWidth/2, mPointY-mHeight/2+mPointY+mHeight/2, mPointX+mWidth/2, mPointY+mHeight/2+mPointY+mHeight/2, mPaint);
		
		// -------------------绘制上边框-----------------------------------
		// 绘制小矩形（上中）
		mPaint.setARGB(255, 232, 25, 41);
		canvas.drawRect(mPointX-0.25f*mWidth, mPointY-mHeight/2-tabHeight+mPointY+mHeight/2, mPointX+0.25f*mWidth, mPointY-mHeight/2+mPointY+mHeight/2, mPaint);
		
		mPaint.setARGB(255, 52, 52, 52);
		// 绘制小矩形(上左)
		canvas.drawRect(mPointX-mWidth/2+tabHeight, mPointY-mHeight/2-tabHeight+mPointY+mHeight/2, mPointX-0.25f*mWidth, mPointY-mHeight/2+mPointY+mHeight/2, mPaint);
		// 绘制扇形（上左）
		RectF oval_top_left = new RectF(mPointX-mWidth/2, mPointY-mHeight/2-tabHeight+mPointY+mHeight/2, mPointX-mWidth/2+tabHeight*2, mPointY-mHeight/2+tabHeight+mPointY+mHeight/2);
		canvas.drawArc(oval_top_left, 180, 90, true, mPaint);
				
		// 绘制小矩形（上右）
		canvas.drawRect(mPointX+0.25f*mWidth, mPointY-mHeight/2-tabHeight+mPointY+mHeight/2, mPointX+mWidth/2-tabHeight, mPointY-mHeight/2+mPointY+mHeight/2, mPaint);
		// 绘制扇形（上右）
		RectF oval_top_right = new RectF(mPointX+mWidth/2-tabHeight*2, mPointY-mHeight/2-tabHeight+mPointY+mHeight/2, mPointX+mWidth/2, mPointY-mHeight/2+tabHeight+mPointY+mHeight/2);
		canvas.drawArc(oval_top_right, 270, 90, true, mPaint);
		
		// -------------------绘制下边框-----------------------------------
		// 绘制矩形（下中）
		canvas.drawRect(mPointX-mWidth/2+tabHeight, mPointY+mHeight/2+mPointY+mHeight/2, mPointX+mWidth/2-tabHeight, mPointY+mHeight/2+tabHeight+mPointY+mHeight/2, mPaint);
		
		// 绘制扇形（下左）
		RectF oval_bottom_left = new RectF(mPointX-mWidth/2, mPointY+mHeight/2-tabHeight+mPointY+mHeight/2, mPointX-mWidth/2+tabHeight*2, mPointY+mHeight/2+tabHeight+mPointY+mHeight/2);
		canvas.drawArc(oval_bottom_left, 90, 90, true, mPaint);
		// 绘制扇形（下右）
		RectF oval_bottom_right = new RectF(mPointX+mWidth/2-tabHeight*2, mPointY+mHeight/2-tabHeight+mPointY+mHeight/2, mPointX+mWidth/2, mPointY+mHeight/2+tabHeight+mPointY+mHeight/2);
		canvas.drawArc(oval_bottom_right, 0, 90, true, mPaint);
		
		// -------------------------设置后绘制直线-------------------------
		mPaint.setARGB(255, 86, 86, 86);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(1);
		
		// 矩形上边
		Path patha = new Path();
		patha.moveTo(mPointX-mWidth/2,mPointY-mHeight/2+mPointY+mHeight/2);
		patha.lineTo(mPointX+mWidth/2,mPointY-mHeight/2+mPointY+mHeight/2);
		patha.close();
		canvas.drawPath(patha, mPaint);
		
		// 矩形下边
		Path pathb = new Path();
		pathb.moveTo(mPointX-mWidth/2,mPointY+mHeight/2+mPointY+mHeight/2);
		pathb.lineTo(mPointX+mWidth/2,mPointY+mHeight/2+mPointY+mHeight/2);
		pathb.close();
		canvas.drawPath(pathb, mPaint);
		
		// 横边
		Path pathc = new Path();
		pathc.moveTo(mPointX,mPointY-mHeight/2+mPointY+mHeight/2);
		pathc.lineTo(mPointX,mPointY+mHeight/2+mPointY+mHeight/2);
		pathc.close();
		canvas.drawPath(pathc, mPaint);
		
		// 竖边
		Path pathd = new Path();
		pathd.moveTo(mPointX-mWidth/2,mPointY+mPointY+mHeight/2);
		pathd.lineTo(mPointX+mWidth/2,mPointY+mPointY+mHeight/2);
		pathd.close();
		canvas.drawPath(pathd, mPaint);
		
		// 图标中点
		// 上左
		float t_x1 = mPointX-mWidth/2+tabHeight;
		float t_y1 = mPointY-mHeight/2-tabHeight/2;
		// 上中
		float t_x2 = mPointX;
		float t_y2 = mPointY-mHeight/2-tabHeight/2;
		// 上右
		float t_x3 = mPointX+mWidth/2-tabHeight;
		float t_y3 = mPointY-mHeight/2-tabHeight/2;
		
		
		// 中1
		float m_x1 = mPointX-mWidth/4;
		float m_y1 = mPointY-mHeight/4;
		// 中2
		float m_x2 = mPointX+mWidth/4;
		float m_y2 = mPointY-mHeight/4;
		// 中3
		float m_x3 = mPointX-mWidth/4;
		float m_y3 = mPointY+mHeight/4;
		// 中4
		float m_x4 = mPointX+mWidth/4;
		float m_y4 = mPointY+mHeight/4;
		
		// 左头标
		RectF dst_t1 = new RectF(t_x1-bmp_width/2, t_y1-bmp_width/2+mPointY+mHeight/2, t_x1+bmp_width/2, t_y1+bmp_width/2+mPointY+mHeight/2);
		// 中头标
		RectF dst_t2 = new RectF(t_x2-bmp_width/2, t_y2-bmp_width/2+mPointY+mHeight/2, t_x2+bmp_width/2, t_y2+bmp_width/2+mPointY+mHeight/2);
		// 右头标
		RectF dst_t3 = new RectF(t_x3-bmp_width/2, t_y3-bmp_width/2+mPointY+mHeight/2, t_x3+bmp_width/2, t_y3+bmp_width/2+mPointY+mHeight/2);
		// 中间图标1
		RectF dst_m1 = new RectF(m_x1-bmp_content_width/2, m_y1-bmp_content_width/2+mPointY+mHeight/2, m_x1+bmp_content_width/2, m_y1+bmp_content_width/2+mPointY+mHeight/2);
		// 中间图标2
		RectF dst_m2 = new RectF(m_x2-bmp_content_width/2, m_y2-bmp_content_width/2+mPointY+mHeight/2, m_x2+bmp_content_width/2, m_y2+bmp_content_width/2+mPointY+mHeight/2);
		// 中间图标3
		RectF dst_m3 = new RectF(m_x3-bmp_content_width/2, m_y3-bmp_content_width/2+mPointY+mHeight/2, m_x3+bmp_content_width/2, m_y3+bmp_content_width/2+mPointY+mHeight/2);
		// 中间图标4
		RectF dst_m4 = new RectF(m_x4-bmp_content_width/2, m_y4-bmp_content_width/2+mPointY+mHeight/2, m_x4+bmp_content_width/2, m_y4+bmp_content_width/2+mPointY+mHeight/2);

		switch (pageNum) {
		case 0:
			// 码表页面
			
			// 绘制头标
			canvas.drawBitmap(bmp_path, null, dst_t1, mPaint);
			canvas.drawBitmap(bmp_mabiao, null, dst_t2, mPaint);
			canvas.drawBitmap(bmp_camera, null, dst_t3, mPaint);
			canvas.drawBitmap(bmp_mabiao_resetspeed, null, dst_m1, mPaint);
			canvas.drawBitmap(bmp_mabiao_resetmileage, null, dst_m2, mPaint);
			canvas.drawBitmap(bmp_mabiao_resettime, null, dst_m3, mPaint);
			canvas.drawBitmap(bmp_mabiao_resetsteep, null, dst_m4, mPaint);

			break;
		case 1:
			// 相机页面
			
			// 绘制头标
			canvas.drawBitmap(bmp_mabiao, null, dst_t1, mPaint);
			canvas.drawBitmap(bmp_camera, null, dst_t2, mPaint);
			canvas.drawBitmap(bmp_music, null, dst_t3, mPaint);
			canvas.drawBitmap(bmp_camera_delay, null, dst_m1, mPaint);
			canvas.drawBitmap(bmp_camera_storage, null, dst_m2, mPaint);
			break;
		case 2:
			// 音乐页面
			
			// 绘制头标
			canvas.drawBitmap(bmp_camera, null, dst_t1, mPaint);
			canvas.drawBitmap(bmp_music, null, dst_t2, mPaint);
			canvas.drawBitmap(bmp_stopwatch, null, dst_t3, mPaint);
			canvas.drawBitmap(bmp_music_cycle, null, dst_m1, mPaint);
			canvas.drawBitmap(bmp_music_random, null, dst_m2, mPaint);
			break;
		case 3:
			// 秒表页面
			
			// 绘制头标
			canvas.drawBitmap(bmp_music, null, dst_t1, mPaint);
			canvas.drawBitmap(bmp_stopwatch, null, dst_t2, mPaint);
			canvas.drawBitmap(bmp_guide, null, dst_t3, mPaint);
			break;
		case 4:
			// 导航页面
			
			// 绘制头标
			canvas.drawBitmap(bmp_stopwatch, null, dst_t1, mPaint);
			canvas.drawBitmap(bmp_guide, null, dst_t2, mPaint);
			canvas.drawBitmap(bmp_path, null, dst_t3, mPaint);
			canvas.drawBitmap(bmp_guide_setpath, null, dst_m1, mPaint);
			canvas.drawBitmap(bmp_guide_summary, null, dst_m2, mPaint);
			canvas.drawBitmap(bmp_guide_offlinemap, null, dst_m3, mPaint);
			canvas.drawBitmap(bmp_guide_information, null, dst_m4, mPaint);
			break;
		case 5:
			// 轨迹页面
			
			// 绘制头标
			canvas.drawBitmap(bmp_guide, null, dst_t1, mPaint);
			canvas.drawBitmap(bmp_path, null, dst_t2, mPaint);
			canvas.drawBitmap(bmp_mabiao, null, dst_t3, mPaint);
			canvas.drawBitmap(bmp_path_resetstart, null, dst_m1, mPaint);
			canvas.drawBitmap(bmp_path_save, null, dst_m2, mPaint);
			canvas.drawBitmap(bmp_path_continue, null, dst_m3, mPaint);
			canvas.drawBitmap(bmp_path_share, null, dst_m4, mPaint);
			break;
		default:
			break;
		}
		
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
							startMoveAnim(this.getScrollY(),(int)(mPointY+mHeight/2) - this.getScrollY(), mDuration);
							isShow = true;
							changed();
						} else {
							if(Math.abs(upY-downY)>mPointY/8){
								Log.d("DialogView","ScrollX = " + getScrollX() +"ScrollY = " + getScrollY() );
								startMoveAnim(this.getScrollY(), -this.getScrollY(), mDuration);
								isShow = false;
								changed();
							}else{
								startMoveAnim(this.getScrollY(), (int)(mPointY+mHeight/2) - this.getScrollY(), mDuration);
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
	
	/** 判断点击点是否在可触发范围内，如果在则可通过点击关闭dialog
	 * @param x x点位置
	 * @param y y点位置
	 * @return true/false*/
	public boolean couldTouchClose(int x,int y){
		if(x<mPointX+mWidth/2 && x>mPointX-mWidth/2 && y>mPointY-mHeight/2 && y<mPointY+mHeight/2+tabHeight){
			return false;
		}else{
			return true;
		}
	}
	
	/** 判断点击点是中间tab中的哪个*/
	public int touchMidTab(int x, int y){
		int i = 0;
		if(x>mPointX-mWidth/2 && x<mPointX && y<mPointY && y>mPointY-mHeight/2){
			i=1;
		}else if (x>mPointX && x<mPointX+mWidth/2 && y<mPointY && y>mPointY-mHeight/2) {
			i=2;
		}else if (x>mPointX-mWidth/2 && x<mPointX && y>mPointY && y<mPointY+mHeight/2) {
			i=3;
		}else if (x>mPointX && x<mPointX+mWidth/2 && y>mPointY && y<mPointY+mHeight/2) {
			i=4;
		}else if (x>mPointX-mWidth/2 && x<mPointX+mWidth/2 && y>mPointY+mHeight/2 && y<mPointY+mHeight/2+tabHeight) {
			i=5;
		}
		return i;
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
			DialogView.this.startMoveAnim(0, (int)(mPointY+mHeight/2), mDuration);
			isShow = true;
			changed();
		}
	}
	
	/** 关闭界面 */
	public void dismiss(){
		if(isShow){
			DialogView.this.startMoveAnim((int)(mPointY+mHeight/2), -(int)(mPointY+mHeight/2), mDuration);
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
	
	/** 设置展开状态*/
	public void setPageNum(int page){
		pageNum = page;
		invalidate();
	}
	
}

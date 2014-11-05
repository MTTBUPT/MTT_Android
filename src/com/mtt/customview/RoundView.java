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

	private Paint mPaint = new Paint();

	/** stone列表 */
	private BigStone[] mStones;
	/** stone数目 */
	private static final int STONE_COUNT = 6;
	
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
	BitmapDrawable bmpDraw_mabiao_untouch=(BitmapDrawable)getResources().getDrawable(R.drawable.mabiao_untouch);
	Bitmap bmp_mabiao_untouch=bmpDraw_mabiao_untouch.getBitmap();
	BitmapDrawable bmpDraw_mabiao_touch=(BitmapDrawable)getResources().getDrawable(R.drawable.mabiao_touch);
	Bitmap bmp_mabiao_touch=bmpDraw_mabiao_touch.getBitmap();
	
	BitmapDrawable bmpDraw_path_untouch=(BitmapDrawable)getResources().getDrawable(R.drawable.path_untouch);
	Bitmap bmp_path_untouch=bmpDraw_path_untouch.getBitmap();
	BitmapDrawable bmpDraw_path_touch=(BitmapDrawable)getResources().getDrawable(R.drawable.path_touch);
	Bitmap bmp_path_touch=bmpDraw_path_touch.getBitmap();
	
	BitmapDrawable bmpDraw_music_untouch=(BitmapDrawable)getResources().getDrawable(R.drawable.music_untouch);
	Bitmap bmp_music_untouch=bmpDraw_music_untouch.getBitmap();
	BitmapDrawable bmpDraw_music_touch=(BitmapDrawable)getResources().getDrawable(R.drawable.music_touch);
	Bitmap bmp_music_touch=bmpDraw_music_touch.getBitmap();
	
	BitmapDrawable bmpDraw_camera_untouch=(BitmapDrawable)getResources().getDrawable(R.drawable.camera_untouch);
	Bitmap bmp_camera_untouch=bmpDraw_camera_untouch.getBitmap();
	BitmapDrawable bmpDraw_camera_touch=(BitmapDrawable)getResources().getDrawable(R.drawable.camera_touch);
	Bitmap bmp_camera_touch=bmpDraw_camera_touch.getBitmap();
	
	BitmapDrawable bmpDraw_stopwatches_untouch=(BitmapDrawable)getResources().getDrawable(R.drawable.stopwatches_untouch);
	Bitmap bmp_stopwatches_untouch=bmpDraw_stopwatches_untouch.getBitmap();
	BitmapDrawable bmpDraw_stopwatches_touch=(BitmapDrawable)getResources().getDrawable(R.drawable.stopwatches_touch);
	Bitmap bmp_stopwatches_touch=bmpDraw_stopwatches_touch.getBitmap();
	
	BitmapDrawable bmpDraw_guide_untouch=(BitmapDrawable)getResources().getDrawable(R.drawable.guide_untouch);
	Bitmap bmp_guide_untouch=bmpDraw_guide_untouch.getBitmap();
	BitmapDrawable bmpDraw_guide_touch=(BitmapDrawable)getResources().getDrawable(R.drawable.guide_touch);
	Bitmap bmp_guide_touch=bmpDraw_guide_touch.getBitmap();
	
	private int btmap_mabiao_width = bmp_mabiao_untouch.getWidth();
	private int btmap_path_width = bmp_path_untouch.getWidth(); 
	private int btmap_music_width = bmp_music_untouch.getWidth(); 
	private int btmap_camera_width = bmp_camera_untouch.getWidth(); 
	private int btmap_stopwatches_width = bmp_stopwatches_untouch.getWidth(); 
	private int btmap_guide_width = bmp_guide_untouch.getWidth(); 

	public RoundView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
		// 获取圆心坐标
		mPointX = this.getMeasuredWidth()/2;
		mPointY = this.getMeasuredHeight()/2;
		
		outRadius = mPointX*5/9;
		// 初始化半径
		exRadius = mPointX*4/9;
		inRadius = exRadius/3;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		canvas.drawARGB(255, 34, 44, 54);
		// 去锯齿
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Paint.Style.FILL);
		
		// 绘制外环
		mPaint.setARGB(255, 25, 33, 43);
		canvas.drawCircle(mPointX, mPointY, outRadius, mPaint);
		// 绘制外圆
		mPaint.setARGB(255, 44, 54, 66);;
		canvas.drawCircle(mPointX, mPointY, exRadius, mPaint);
		
		RectF oval = new RectF(mPointX - exRadius, mPointY - exRadius, mPointX + exRadius, mPointY + exRadius);
		switch (isTouchDown) {
		case 1:
			// 绘制扇形（导航）
			mPaint.setARGB(255, 232, 25, 41);
			canvas.drawArc(oval, 180, 60, true, mPaint);
			break;
		case 2:
			// 绘制扇形（码表）
			mPaint.setARGB(255, 232, 25, 41);
			canvas.drawArc(oval, 240, 60, true, mPaint);
			break;
		case 3:
			// 绘制扇形（轨迹）
			mPaint.setARGB(255, 232, 25, 41);
			canvas.drawArc(oval, 300, 60, true, mPaint);
			break;
		case 4:
			// 绘制扇形（音乐）
			mPaint.setARGB(255, 232, 25, 41);
			canvas.drawArc(oval, 0, 60, true, mPaint);
			break;
		case 5:		
			// 绘制扇形（相机）
			mPaint.setARGB(255, 232, 25, 41);
			canvas.drawArc(oval, 60, 60, true, mPaint);
			break;
		case 6:
			// 绘制扇形（秒表）
			mPaint.setARGB(255, 232, 25, 41);
			canvas.drawArc(oval, 120, 60, true, mPaint);
			break;
		default:
			break;
		}
		
		// 绘制内圆
		mPaint.setARGB(255, 25, 33, 43);;
		canvas.drawCircle(mPointX, mPointY, inRadius, mPaint);
		
		// ----------设置后绘制六条直线----------
		mPaint.setARGB(255, 25, 33, 43);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(5);
		
		Path patha = new Path();
		patha.moveTo(mPointX - inRadius, mPointY);
		patha.lineTo(mPointX - exRadius, mPointY);
		patha.close();
		canvas.drawPath(patha, mPaint);
		
		Path pathb = new Path();
		pathb.moveTo(mPointX - inRadius/2, (float) (mPointY + inRadius*Math.sqrt(3)/2));
		pathb.lineTo(mPointX - exRadius/2, (float) (mPointY + exRadius*Math.sqrt(3)/2));
		pathb.close();
		canvas.drawPath(pathb, mPaint);
		
		Path pathc = new Path();
		pathc.moveTo(mPointX + inRadius/2, (float) (mPointY + inRadius*Math.sqrt(3)/2));
		pathc.lineTo(mPointX + exRadius/2, (float) (mPointY + exRadius*Math.sqrt(3)/2));
		pathc.close();
		canvas.drawPath(pathc, mPaint);
		
		Path pathd = new Path();
		pathd.moveTo(mPointX + inRadius, mPointY);
		pathd.lineTo(mPointX + exRadius, mPointY);
		pathd.close();
		canvas.drawPath(pathd, mPaint);
		
		Path pathe = new Path();
		pathe.moveTo(mPointX + inRadius/2, (float) (mPointY - inRadius*Math.sqrt(3)/2));
		pathe.lineTo(mPointX + exRadius/2, (float) (mPointY - exRadius*Math.sqrt(3)/2));
		pathe.close();
		canvas.drawPath(pathe, mPaint);
		
		Path pathf = new Path();
		pathf.moveTo(mPointX - inRadius/2, (float) (mPointY - inRadius*Math.sqrt(3)/2));
		pathf.lineTo(mPointX - exRadius/2, (float) (mPointY - exRadius*Math.sqrt(3)/2));
		pathf.close();
		canvas.drawPath(pathf, mPaint);
		
		// ----------设置字符大小后绘制字符----------
		mPaint.setStrokeWidth(1);
		mPaint.setColor(Color.WHITE);
		mPaint.setStyle(Paint.Style.FILL);
		mPaint.setTextSize(40);
		mPaint.setShader(null);
		mPaint.setTextAlign(Align.CENTER);
		// 绘制6个字符串和图片
		// 码表
		canvas.translate(mPointX, mPointY);
		if(isTouchDown ==2){
			mPaint.setARGB(255, 44, 54, 66);;
			canvas.drawText(getResources().getString(R.string.mabiao), 0, - inRadius*(1.5f), mPaint);
		}else{
			mPaint.setColor(Color.WHITE);
			canvas.drawText(getResources().getString(R.string.mabiao), 0, - inRadius*(1.5f), mPaint);
		}
		
		if(isTouchDown ==5){
			mPaint.setARGB(255, 44, 54, 66);;
			canvas.drawText(getResources().getString(R.string.camera), 0, inRadius*(1.5f), mPaint);
		}else {
			mPaint.setColor(Color.WHITE);
			canvas.drawText(getResources().getString(R.string.camera), 0, inRadius*(1.5f), mPaint);
		}
		
		if(isTouchDown == 2){
			canvas.drawBitmap(bmp_mabiao_touch, - btmap_mabiao_width/2, -inRadius*(2.6f), mPaint);
		}else{
			canvas.drawBitmap(bmp_mabiao_untouch, - btmap_mabiao_width/2, -inRadius*(2.6f), mPaint);
		}
		
		// 轨迹
		canvas.rotate(60);
		if(isTouchDown ==3){
			mPaint.setARGB(255, 44, 54, 66);;
			canvas.drawText(getResources().getString(R.string.path), 0, - inRadius*(1.5f), mPaint);
		}else {
			mPaint.setColor(Color.WHITE);
			canvas.drawText(getResources().getString(R.string.path), 0, - inRadius*(1.5f), mPaint);
		}
		
		if(isTouchDown ==6){
			mPaint.setARGB(255, 44, 54, 66);;
			canvas.drawText(getResources().getString(R.string.stopwatch), 0, inRadius*(1.5f), mPaint);
		}else {
			mPaint.setColor(Color.WHITE);
			canvas.drawText(getResources().getString(R.string.stopwatch), 0, inRadius*(1.5f), mPaint);
		}
		
		if(isTouchDown == 3){
			canvas.drawBitmap(bmp_path_touch, - btmap_path_width/2, -inRadius*(2.6f), mPaint);

		}else{
			canvas.drawBitmap(bmp_path_untouch, - btmap_path_width/2, -inRadius*(2.6f), mPaint);
		}
		
		// 音乐
		canvas.rotate(60);
		if(isTouchDown == 4){
			canvas.drawBitmap(bmp_music_touch, - btmap_music_width/2, -inRadius*(2.6f), mPaint);

		}else{
			canvas.drawBitmap(bmp_music_untouch, - btmap_music_width/2, -inRadius*(2.6f), mPaint);
		}
		
		// 相机
		canvas.rotate(60);
		if(isTouchDown == 5){
			canvas.drawBitmap(bmp_camera_touch, - btmap_camera_width/2, -inRadius*(2.6f), mPaint);
		}else{
			canvas.drawBitmap(bmp_camera_untouch, - btmap_camera_width/2, -inRadius*(2.6f), mPaint);
		}
		
		// 秒表
		canvas.rotate(60);
		if(isTouchDown == 6){
			canvas.drawBitmap(bmp_stopwatches_touch, - btmap_stopwatches_width/2, -inRadius*(2.6f), mPaint);
		}else{
			canvas.drawBitmap(bmp_stopwatches_untouch, - btmap_stopwatches_width/2, -inRadius*(2.6f), mPaint);
		}
		
		// 导航
		canvas.rotate(60);
		if(isTouchDown == 1){
			mPaint.setARGB(255, 44, 54, 66);;
			canvas.drawText(getResources().getString(R.string.navi), 0, - inRadius*(1.5f), mPaint);
		}else {
			mPaint.setColor(Color.WHITE);
			canvas.drawText(getResources().getString(R.string.navi), 0, - inRadius*(1.5f), mPaint);
		}
		
		if(isTouchDown == 4){
			mPaint.setARGB(255, 44, 54, 66);;
			canvas.drawText(getResources().getString(R.string.music), 0, inRadius*(1.5f), mPaint);
		}else {
			mPaint.setColor(Color.WHITE);
			canvas.drawText(getResources().getString(R.string.music), 0, inRadius*(1.5f), mPaint);
		}
		
		if(isTouchDown == 1){
			canvas.drawBitmap(bmp_guide_touch, - btmap_guide_width/2, -inRadius*(2.6f), mPaint);

		}else{
			canvas.drawBitmap(bmp_guide_untouch, - btmap_guide_width/2, -inRadius*(2.6f), mPaint);
		}

	}

	
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		int x,y;
		
		if(event.getAction() == MotionEvent.ACTION_DOWN){
			x = (int) event.getX();
			y = (int) event.getY();
			switch (touchPositon(x, y)) {
			case 1:
				isTouchDown = 1;
				invalidate();
				break;
			case 2:
				isTouchDown = 2;
				invalidate();
				break;
			case 3:
				isTouchDown = 3;
				invalidate();
				break;
			case 4:
				isTouchDown = 4;
				invalidate();
				break;
			case 5:
				isTouchDown = 5;
				invalidate();
				break;
			case 6:
				isTouchDown = 6;
				invalidate();
				break;
			default:
				break;
			}
			
		}else if (event.getAction() == MotionEvent.ACTION_UP) {
			x = (int) event.getX();
			y = (int) event.getY();
			switch (touchPositon(x, y)) {
			case 1:
	            return mOnTouchListener.onTouchEvent(this, event,1);  
			case 2:
	            return mOnTouchListener.onTouchEvent(this, event,2);  
			case 3:
	            return mOnTouchListener.onTouchEvent(this, event,3);  
			case 4:
	            return mOnTouchListener.onTouchEvent(this, event,4);  
			case 5:
	            return mOnTouchListener.onTouchEvent(this, event,5);  
			case 6:
	            return mOnTouchListener.onTouchEvent(this, event,6);  

			default:
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
		
		if(arc<60 && x<mPointX && y<mPointY && isInRound(x, y)){
			result = 1;
		}else if (isInRound(x, y) && arc>60 && y<mPointY){
			result = 2;
		}else if (isInRound(x, y) && arc<60 && x>mPointX && y<mPointY){
			result = 3;
		}else if (isInRound(x, y) && arc<60 && x>mPointX && y>mPointY){
			result = 4;
		}else if (isInRound(x, y) && arc>60 && y>mPointY){
			result = 5;
		}else if (isInRound(x, y) && arc<60 && x<mPointX && y>mPointY){
			result = 6;
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
	
	class BigStone {

		// 图片
		Bitmap bitmap;

		// 角度
		int angle;

		// x坐标
		float x;

		// y坐标
		float y;

	}
	
}

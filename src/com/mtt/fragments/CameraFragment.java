package com.mtt.fragments;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import com.mtt.R;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Bitmap.CompressFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.SurfaceHolder.Callback;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/** 
 * 相机功能页面
 * @author Kerry
 * */
public class CameraFragment extends Fragment{
	public static final String TAG = "com.mtt.fragment.CameraFragment";

	private SurfaceView sView;
	private SurfaceHolder holder;
	/** 摄像按钮*/
	private ImageButton mBtStartStop;
	/** 拍照按钮*/
	private ImageButton capture;
	/** 录像时间*/
	private TextView mVideoTime;
	SurfaceHolder surfaceHolder;
	/** 屏幕宽度和高度*/
	int screenWidth, screenHeight;
	/** 定义系统所用的照相机*/
	Camera camera;
	
	/** 是否在预览中*/
	boolean isPreview = false;
	
	/** 是否开始*/
	private boolean mStartedFlg = false;
	private MediaRecorder mRecorder;
	
    private int hour = 0;
    private int minute = 0;
    private int second = 0;
    private boolean bool;
    
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        final View view = inflater.inflate(R.layout.fragment_camera, container, false);
		
		// 获取窗口管理器
		WindowManager wm = getActivity().getWindowManager();
		Display display = wm.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		// 获取屏幕的宽和高
		display.getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		
		// 获取界面中SurfaceView组件
		sView = (SurfaceView) view.findViewById(R.id.sView);
		
		mBtStartStop = (ImageButton) view.findViewById(R.id.btStartStop);
		mBtStartStop.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				video(view);
			}
		});
		
		capture = (ImageButton) view.findViewById(R.id.ibCapture);
		capture.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				capture(view);
			}
		});
		
		mVideoTime = (TextView) view.findViewById(R.id.video_time);
		mVideoTime.setVisibility(View.GONE);
		
		// 获得SurfaceView的SurfaceHolder
		holder = sView.getHolder();
        return view;
	}
	
	
    @Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// 为surfaceHolder添加一个回调监听器
		holder.addCallback(new Callback()
		{
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format,
				int width, int height)
			{
                surfaceHolder = holder;
			}

			@Override
			public void surfaceCreated(SurfaceHolder holder)
			{
                surfaceHolder = holder;
				// 打开摄像头
				initCamera();
			}

			@Override
			public void surfaceDestroyed(SurfaceHolder holder)
			{
				// 如果camera不为null ,释放摄像头
				if (camera != null)
				{
					if (isPreview) camera.stopPreview();
					camera.release();
					camera = null;
				}
				sView = null;
				surfaceHolder = null;
				mRecorder = null;
			}
		});
	}


	/*
     * 定时器设置，实现计时
     */
    private Handler handler = new Handler();
    private Runnable task = new Runnable() {
        public void run() {
            if (bool) {
                handler.postDelayed(this, 1000);
                second++;
                if (second >= 60) {
                    minute++;
                    second = second % 60;
                }
                if (minute >= 60) {
                    hour++;
                    minute = minute % 60;
                }
                mVideoTime.setText(format(hour) + ":" + format(minute) + ":"
                        + format(second));
            }
        }
    };
    
    /*
     * 格式化时间
     */
    public String format(int i) {
        String s = i + "";
        if (s.length() == 1) {
            s = "0" + s;
        }
        return s;
    }


    /** 初始化摄像头*/
	private void initCamera()
	{
		if (!isPreview)
		{
			// 此处默认打开后置摄像头。
			// 通过传入参数可以打开前置摄像头
			camera = Camera.open(0);  
			camera.setDisplayOrientation(0);
		}
		if (camera != null && !isPreview)
		{
			try
			{
				Camera.Parameters parameters = camera.getParameters();
				// 设置预览照片的大小
				parameters.setPreviewSize(screenWidth, screenHeight);
				// 设置预览照片时每秒显示多少帧的最小值和最大值
				parameters.setPreviewFpsRange(4, 10);
				// 设置图片格式
				parameters.setPictureFormat(ImageFormat.JPEG);
				// 设置JPG照片的质量
				parameters.set("jpeg-quality", 85);
				// 设置照片的大小
				parameters.setPictureSize(screenWidth, screenHeight);
				// 通过SurfaceView显示取景画面
				camera.setPreviewDisplay(surfaceHolder);  
				// 开始预览
				camera.startPreview();  
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			isPreview = true;
		}
	}

	/** 拍照*/
	public void capture(View source)
	{
		if (camera != null)
		{
			// 控制摄像头自动对焦后才拍照
			camera.autoFocus(autoFocusCallback);  
		}
	}
	
	/** 摄像*/
	public void video(View source)
	{
        if (isPreview) {
            camera.stopPreview();
            camera.release();
            camera = null;
            isPreview = false;
        }
        hour = 0;
        minute = 0;
        second = 0;
        bool = true;
        
		if (!mStartedFlg) {
			// Start
			if (mRecorder == null) {
				mRecorder = new MediaRecorder(); // Create MediaRecorder
			}
			try {

				// Set audio and video source and encoder
				// 这两项需要放在setOutputFormat之前
				mRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
				mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
				
				mRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
		        mRecorder.setPreviewDisplay(surfaceHolder.getSurface()); 
		        
		        // Set output file path 
		        String path = getSDPath();
		        if (path != null) {
		        	
		        	File dir = new File(path + "/MTT");
				    if (!dir.exists()) {
					    dir.mkdir();
				    }
		        	path = dir + "/" + getDate() + ".MP4";
			        mRecorder.setOutputFile(path);
			        mRecorder.prepare();
			        
                    mVideoTime.setVisibility(View.VISIBLE);
                    handler.postDelayed(task, 1000);
                    
			        mRecorder.start();   // Recording is now started
			        mStartedFlg = true;
			        mBtStartStop.setImageResource(R.drawable.camera_videoing);
		        }
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// stop
			if (mStartedFlg) {
				try {
					mRecorder.stop();
                    bool = false;
                    mVideoTime.setText(format(hour) + ":" + format(minute) + ":"
                            + format(second));
			        mRecorder.reset();   // You can reuse the object by going back to setAudioSource() step
			        mBtStartStop.setImageResource(R.drawable.camera_video);
					Toast.makeText(getActivity().getApplicationContext(), "录像已保存", Toast.LENGTH_SHORT).show();
			        initCamera();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			mStartedFlg = false; // Set button status flag
		}
	}
	
	/**  
     * 获取系统时间  
     * @return  
     */  
 	public static String getDate(){
 		Calendar ca = Calendar.getInstance();   
 		int year = ca.get(Calendar.YEAR);			// 获取年份   
 		int month = ca.get(Calendar.MONTH);			// 获取月份    
 		int day = ca.get(Calendar.DATE);			// 获取日   
 		int minute = ca.get(Calendar.MINUTE);		// 分    
 		int hour = ca.get(Calendar.HOUR);			// 小时    
 		int second = ca.get(Calendar.SECOND);		// 秒   
     
 		String date = "" + year + (month + 1 )+ day + hour + minute + second;
 		
        return date;         
    }

 	/**  
     * 获取SD path  
     * @return  
     */
 	public String getSDPath(){ 
 		File sdDir = null; 
 		boolean sdCardExist = Environment.getExternalStorageState() 
 				.equals(android.os.Environment.MEDIA_MOUNTED); // 判断sd卡是否存在 
 		if (sdCardExist) 
 		{ 
 			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录 
 			return sdDir.toString(); 
 		}
 		
 		return null;
 	}
	
	AutoFocusCallback autoFocusCallback = new AutoFocusCallback()
	{
		// 当自动对焦时激发该方法
		@Override
		public void onAutoFocus(boolean success, Camera camera)
		{
			if (success)
			{
				// takePicture()方法需要传入3个监听器参数
				// 第1个监听器：当用户按下快门时激发该监听器
				// 第2个监听器：当相机获取原始照片时激发该监听器
				// 第3个监听器：当相机获取JPG照片时激发该监听器
				camera.takePicture(new ShutterCallback()
				{
					public void onShutter()
					{
						// 按下快门瞬间会执行此处代码
					}
				}, new PictureCallback()
				{
					public void onPictureTaken(byte[] data, Camera c)
					{
						// 此处代码可以决定是否需要保存原始照片信息
					}
				}, myJpegCallback);  
			}
		}
	};

	PictureCallback myJpegCallback = new PictureCallback()
	{
		@Override
		public void onPictureTaken(byte[] data, Camera camera)
		{
			// 根据拍照所得的数据创建位图
			final Bitmap bm = BitmapFactory.decodeByteArray(data, 0,
				data.length);
			// 加载/layout/save.xml文件对应的布局资源
			View saveDialog = getActivity().getLayoutInflater().inflate(R.layout.save,
				null);
			final EditText photoName = (EditText) saveDialog
				.findViewById(R.id.phone_name);
			String pName = getDate();
			photoName.setText(pName);
			// 获取saveDialog对话框上的ImageView组件
			ImageView show = (ImageView) saveDialog.findViewById(R.id.show);
			// 显示刚刚拍得的照片
			show.setImageBitmap(bm);
        	File dir = new File(Environment.getExternalStorageDirectory() + "/aaaa");
		    if (!dir.exists()) {
			    dir.mkdir();
		    }
			// 使用对话框显示saveDialog组件
			new AlertDialog.Builder(getActivity()).setView(saveDialog)
				.setPositiveButton("保存", new OnClickListener()
				{
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						// 创建一个位于SD卡上的文件
						File file = new File(Environment
							.getExternalStorageDirectory() + "/aaaa", photoName
							.getText().toString() + ".jpg");
						
						FileOutputStream outStream = null;
						try
						{
							// 打开指定文件对应的输出流
							outStream = new FileOutputStream(file);
							// 把位图输出到指定文件中
							bm.compress(CompressFormat.JPEG, 100,
								outStream);
							outStream.close();
							Toast.makeText(getActivity().getApplicationContext(), "相片已保存", Toast.LENGTH_SHORT).show();
						}
						catch (IOException e)
						{
							e.printStackTrace();
						}
					}
				}).setNegativeButton("取消", null).show();
			// 重新浏览
			camera.stopPreview();
			camera.startPreview();
			isPreview = true;
		}
	};
	
}

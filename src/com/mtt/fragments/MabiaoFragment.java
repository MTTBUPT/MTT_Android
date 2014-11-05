package com.mtt.fragments;

import java.util.List;

import com.mtt.R;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/** 
 * 码表功能页面
 * @author Kerry
 * */
public class MabiaoFragment extends Fragment implements SensorEventListener{
	public static final String TAG = "com.mtt.fragment.MabiaoFragment";

	private View view;
	/** 显示坡度值*/
	private TextView tv_slope =null; 
	/** 罗盘图标*/
	private ImageView iv_compass =null;  
	
	/** 传感器*/
	private SensorManager mMgr;   
	private List<Sensor> mSensorList;
	/** 指南针图片转过的角度*/
	float currentDegree = 0f; 
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        view = inflater.inflate(com.mtt.R.layout.fragment_mabiao, container, false);  
		initView();  // 初始化

		return view;
	}
	
	/** 资源初始化*/
	private void initView() {
		// TODO Auto-generated method stub
		tv_slope = (TextView) view.findViewById(R.id.tv_slope);
		iv_compass = (ImageView) view.findViewById(R.id.iv_compass);
	    Context context = getActivity();
	    mMgr = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	    
	    tv_slope.setText("0 °");
	}
	
	 /** 将输入的字符转换为角度，并显示出来*/
	private void setScreenForAngle(float paramFloat)
	{		
	    StringBuilder localStringBuilder1 = new StringBuilder();
	    int i = Math.abs(Math.round(paramFloat));

	    if (paramFloat < 0.0F)
	    { 
	    	localStringBuilder1.append("-");
	    }
	    if (paramFloat < 90.0F)
	    {
	    	localStringBuilder1.append(" ");
	    	localStringBuilder1.append(i);
	    	localStringBuilder1.append(" °");
	    }
	      
	    while (true)
	    {
	      tv_slope.setText(localStringBuilder1);
	      return;
	    }
	}
	
	/** 开始感应*/
	private void startSensing()
	{
	  this.mSensorList = this.mMgr.getSensorList(3);
	  Sensor localSensor = mSensorList.get(0);
	      
	  mMgr.registerListener(this, localSensor, SensorManager.SENSOR_DELAY_GAME);
	  
	}

	/** 结束感应*/
	private void stopSensing()
	{
	  this.mMgr.unregisterListener(this);
	}
	
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		  int sensorType = event.sensor.getType();
			
		  switch(sensorType){
		  case 3:
			  setScreenForAngle(event.values[2]);
			  
			  float degree = event.values[0]; //获取z转过的角度
			  degree = degree + 90.0F;
			  //创建旋转动画
			  RotateAnimation ra = new RotateAnimation(currentDegree,-degree,Animation.RELATIVE_TO_SELF,0.5f
			  		,Animation.RELATIVE_TO_SELF,0.5f);
			  ra.setDuration(100);//动画持续时间
			  iv_compass.startAnimation(ra);
			  currentDegree = -degree;
			  break;
		  }
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		stopSensing();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		startSensing();
	}
	
}

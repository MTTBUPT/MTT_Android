package com.mtt.fragments;

import java.util.List;

import com.mtt.R;
import com.mtt.customview.CompassView;
import com.mtt.customview.SteepView;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/** 
 * 码表功能页面
 * @author Kerry
 * */
public class MabiaoFragment extends Fragment implements SensorEventListener{
	public static final String TAG = "com.mtt.fragment.MabiaoFragment";

	private View view;
	/** 坡度view*/
	private SteepView mSteepView;
	/** 罗盘view*/
	private CompassView mCompassView;
	
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
		mCompassView = (CompassView) view.findViewById(R.id.mabiaofragment_mycompassview);
		mSteepView = (SteepView) view.findViewById(R.id.mabiaofragment_mysteepview);
	    Context context = getActivity();
	    mMgr = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
	    
	}
	
	 /** 将输入的字符转换为角度，并显示出来*/
	private void setScreenForAngle(float paramFloat)
	{		
	    int i = Math.abs(Math.round(paramFloat));
	    Log.d("MySteep", i + "");
	    if (paramFloat < 0.0F)
	    { 
	    	mSteepView.setSteep(i,false);
	    }
	    if (paramFloat < 90.0F && paramFloat > 0.0F)
	    {
	    	mSteepView.setSteep(i,true);
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
			  mCompassView.setArc(degree);
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

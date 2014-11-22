package com.mtt.fragments;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.navi.AMapNavi;
import com.amap.api.navi.AMapNaviListener;
import com.amap.api.navi.model.AMapNaviInfo;
import com.amap.api.navi.model.AMapNaviLocation;
import com.amap.api.navi.model.AMapNaviPath;
import com.amap.api.navi.model.NaviLatLng;
import com.amap.api.navi.view.RouteOverLay;
import com.mtt.R;
import com.mtt.util.ToastUtil;
 
/**
 * 混合定位示例
 * */
public class GuideFragment extends Fragment implements LocationSource,
		AMapLocationListener,OnClickListener,SensorEventListener,AMapNaviListener{
	public static final String TAG = "com.mtt.fragment.GuideFragment";

	public final static String ACTION_GUIDE = "com.mtt.fragment.GuideFragent.ACTION_GUIDE";
	/** Fragment View*/
	private View view;
	/** 系统的Context*/
	private Context mContext;
	
	// 地图资源
	private AMap aMap;
	private MapView mapView;
    private UiSettings mUiSettings;
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	
	// 传感器
	private SensorManager mSensorManager;
	private Sensor mSensor;
	/** 手机旋转角度*/
	private float mAngle;
	/** 上一次获取传感器的时间*/
	private long lastTime = 0;
	/** 传感器感应起作用的时间*/
	private final int TIME_SENSOR = 200;
	/** 地图旋转的角度*/
	private int Map_bear;
	
	/** 缩放按钮*/
	private Button btn_zoom_in,btn_zoom_out;
	
	/** 缩放等级*/
	private int zoom_level=15;
	
	/** 路径规划*/
	private AMapNavi mAMapNavi; 
	/** 起点经纬度*/
	private NaviLatLng mNaviStart;
	/** 终点经纬度*/
	private NaviLatLng mNaviEnd;;
	/** 起点终点列表*/
	private ArrayList<NaviLatLng> mStartPoints = new ArrayList<NaviLatLng>();
	private ArrayList<NaviLatLng> mEndPoints = new ArrayList<NaviLatLng>();
	/** 规划路线*/
	private RouteOverLay mRouteOverLay;
	
	/**  当前定位点的经纬度*/
	private double latitude; 
	private double longtitude;
	
	/** 路线规划 起点经纬度*/
	double startLatitude = 0;
	double startLongtitude = 0;
	/** 路线规划 终点经纬度*/
	double endLatitude = 0;
	double endLongtitude = 0;
	private boolean isFirstPage = false;

	public GuideFragment(boolean isFirstPage) {
		// TODO Auto-generated constructor stub
		super();
		this.isFirstPage = isFirstPage;
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		// TODO Auto-generated method stub
		if(isVisibleToUser && !isFirstPage){
			init();
			registerSensorListener(); // 注册传感器
		}
		super.setUserVisibleHint(isVisibleToUser);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    Log.d("Fragment_E", "-------------E-------OnCreate()");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	    Log.d("Fragment_E", "-------------E-------OnCreateView()");

		view = inflater.inflate(R.layout.fragment_guide, container,false);
		mContext = getActivity();

		mapView = (MapView) view.findViewById(R.id.map);
		mapView.onCreate(savedInstanceState);// 此方法必须重写
	   //初始化传感器	
		mSensorManager = (SensorManager) mContext.getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getSensorList(3).get(0);
		
		btn_zoom_in = (Button) view.findViewById(R.id.btn_zoom_in);
		btn_zoom_in.setOnClickListener(this);
		btn_zoom_out = (Button) view.findViewById(R.id.btn_zoom_out);
		btn_zoom_out.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
	    Log.d("Fragment_E", "-------------E-------OnActivityCreated()");

		super.onActivityCreated(savedInstanceState);
		LocalBroadcastManager broadcastManager = LocalBroadcastManager.getInstance(getActivity());
		broadcastManager.registerReceiver(mGuideReceiver, GuideIntentFilter());
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
	    Log.d("Fragment_E", "-------------E-------OnResume()");

	    if(isFirstPage){
			init();
			registerSensorListener(); // 注册传感器
	    }
		super.onResume();
		mapView.onResume();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
	    Log.d("Fragment_E", "-------------E-------OnPause()");

		super.onPause();
		mapView.onPause();
		deactivate();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
	    Log.d("Fragment_E", "-------------E-------OnStop()");

		super.onStop();
	}

	/**
	 * 方法必须重写
	 */
	@Override
	public void onSaveInstanceState(Bundle outState) {
	    Log.d("Fragment_E", "-------------E-------OnSaveInstanceState()");

		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
	    Log.d("Fragment_E", "-------------E-------OnDestroyView()");

		super.onDestroyView();
	}
	
	/**
	 * 方法必须重写
	 */
	@Override
	public void onDestroy() {
	    Log.d("Fragment_E", "-------------E-------OnDestroy()");

		super.onDestroy();
		mapView.onDestroy();
		//删除监听 
		AMapNavi.getInstance(mContext).removeAMapNaviListener(this);
	}
	
	/**
	 * 初始化
	 */
	private void init() {
		if (aMap == null) {
			aMap = mapView.getMap();
			setUpMap();
		}
		
		// 获取路径规划
		mAMapNavi = AMapNavi.getInstance(mContext);
		mAMapNavi.setAMapNaviListener(this);
		// 路径
		mRouteOverLay = new RouteOverLay(aMap, null);
	}

	/**
	 * 设置一些amap的属性
	 */
	private void setUpMap() {
		mUiSettings = aMap.getUiSettings(); 

        /** 禁用缩放手势*/
        mUiSettings.setZoomGesturesEnabled(false);
        /** 禁用平移手势*/
        mUiSettings.setScrollGesturesEnabled(false);
        /** 禁用旋转手势*/
        mUiSettings.setRotateGesturesEnabled(false);
        /** 禁用倾斜手势*/
        mUiSettings.setTiltGesturesEnabled(false);
        /** 关闭默认缩放按钮*/
        mUiSettings.setZoomControlsEnabled(false);
        
		aMap.setLocationSource(this);// 设置定位监听
		aMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
		aMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
		// 设置定位的类型为定位模式 ，可以由定位、跟随或地图根据面向方向旋转几种
		aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_zoom_out:
			zoom_level--;
			new CameraUpdateFactory();
			aMap.moveCamera(CameraUpdateFactory.zoomOut());
			break;
		case R.id.btn_zoom_in:
			zoom_level++;
			new CameraUpdateFactory();
			aMap.moveCamera(CameraUpdateFactory.zoomIn());
			break;
		}
	}
	
	/**
	 * 此方法已经废弃
	 */
	@Override
	public void onLocationChanged(Location location) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	/**
	 * 定位成功后回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		if (mListener != null && amapLocation != null) {
			if (amapLocation!=null&&amapLocation.getAMapException().getErrorCode() == 0) {
				mListener.onLocationChanged(amapLocation);// 显示系统小蓝点
				
				// 获取定位点的经纬度
				latitude = amapLocation.getLatitude();
				longtitude = amapLocation.getLongitude();
				LatLng mLatLng = new LatLng(latitude, longtitude);
				
				// Camera的定位位置（中心点、缩放等级、倾斜度、旋转角度）
				CameraPosition mCameraPosition = new CameraPosition(mLatLng, zoom_level, 0, Map_bear);
				new CameraUpdateFactory();
				Log.d("FragmentE", "-----------------active location");
				// 定位Camera
				aMap.animateCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
//				aMap.moveCamera(CameraUpdateFactory.newCameraPosition(mCameraPosition));
			}
		}
	}

	/**
	 * 激活定位
	 */
	@Override
	public void activate(OnLocationChangedListener listener) {
		mListener = listener;
		if (mAMapLocationManager == null) {
			mAMapLocationManager = LocationManagerProxy.getInstance(mContext);
			//此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
			//注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
			//在定位结束后，在合适的生命周期调用destroy()方法		
			//其中如果间隔时间为-1，则定位只定一次
			//在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
			mAMapLocationManager.requestLocationData(
					LocationProviderProxy.AMapNetwork, 6*1000, 10, this);
		}
	}

	/**
	 * 停止定位
	 */
	@Override
	public void deactivate() {
		mListener = null;
		if (mAMapLocationManager != null) {
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destroy();
		}
		mAMapLocationManager = null;
		unRegisterSensorListener();
	}
	
	/** ---------------------------------传感器功能----------------------------------------*/
	
	public void registerSensorListener() {
		mSensorManager.registerListener(this, mSensor,
				SensorManager.SENSOR_DELAY_UI);
	}

	public void unRegisterSensorListener() {
		mSensorManager.unregisterListener(this, mSensor);
	}
	
	@Override
	public void onSensorChanged(SensorEvent event) {
		// TODO Auto-generated method stub
		if (System.currentTimeMillis() - lastTime < TIME_SENSOR) {
			return;
		}
		switch (event.sensor.getType()) {
		case 3: {
			float x = event.values[0];
			
			if (Math.abs(mAngle-x) < 7.0f) {
				break;
			}
			
			mAngle = x;
			
//			Log.d("My3DMap", "mAngle: " +mAngle);
			if(mAngle<45 || mAngle>315){
				new CameraUpdateFactory();
				aMap.animateCamera(CameraUpdateFactory.changeBearing(90),200,null);
				Map_bear = 90;
			}else if(mAngle>45 && mAngle<135){
				new CameraUpdateFactory();
				aMap.animateCamera(CameraUpdateFactory.changeBearing(180),200,null);
				Map_bear = 180;
			}else if(mAngle>135 && mAngle<225){
				new CameraUpdateFactory();
				aMap.animateCamera(CameraUpdateFactory.changeBearing(-90),200,null);
				Map_bear = -90;
			}else if(mAngle>225 && mAngle<315){
				new CameraUpdateFactory();
				aMap.animateCamera(CameraUpdateFactory.changeBearing(0),200,null);
				Map_bear = 0;
			}
			
			lastTime = System.currentTimeMillis();
		}
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// TODO Auto-generated method stub
		
	}

	/** ------------------------------导航功能--------------------------------------------*/
	
	//计算驾车路线
	private void calculateDriveRoute() {
		boolean isSuccess = mAMapNavi.calculateDriveRoute(mStartPoints,
				mEndPoints, null, AMapNavi.DrivingDefault);
		if (!isSuccess) {
			ToastUtil.show(mContext, "路线计算失败,检查参数情况");
		}

	}
	
	@Override
	public void onArriveDestination() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onArrivedWayPoint(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCalculateRouteFailure(int arg0) {
		// TODO Auto-generated method stub
		ToastUtil.show(mContext, "路径规划出错" + arg0);
	}

	@Override
	public void onCalculateRouteSuccess() {
		// TODO Auto-generated method stub
		AMapNaviPath naviPath = mAMapNavi.getNaviPath();
		if (naviPath == null) {
			return;
		}
		// 获取路径规划线路，显示到地图上
		mRouteOverLay.setRouteInfo(naviPath);
		mRouteOverLay.addToMap();
		ToastUtil.show(mContext, "路线计算成功");
	}

	@Override
	public void onEndEmulatorNavi() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGetNavigationText(int arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onGpsOpenStatus(boolean arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInitNaviFailure() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onInitNaviSuccess() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChange(AMapNaviLocation arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNaviInfoUpdated(AMapNaviInfo arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReCalculateRouteForTrafficJam() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onReCalculateRouteForYaw() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStartNavi(int arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTrafficStatusUpdate() {
		// TODO Auto-generated method stub
		
	}

	// -----------------------接收广播------------------------------------
	private final BroadcastReceiver mGuideReceiver = new BroadcastReceiver() {
    	@Override
    	public void onReceive(Context context, Intent intent) {
    		final String action = intent.getAction();
    		if (ACTION_GUIDE.equals(action)){
    			mStartPoints.clear();
    			mEndPoints.clear();
    			
    			// 获取Intent中数据
    			Bundle mBundle = intent.getExtras();
    			endLatitude = mBundle.getDouble("mlatitude");
    			endLongtitude = mBundle.getDouble("mlongtitude");
    			ToastUtil.show(mContext, "开始规划路径");
    			
    			// 设置初始点
    			startLatitude = latitude;
    			startLongtitude = longtitude;
    			mNaviStart = new NaviLatLng(startLatitude, startLongtitude);
    			mStartPoints.add(mNaviStart);
    			// 设置终点
    			mNaviEnd = new NaviLatLng(endLatitude,endLongtitude);
    			mEndPoints.add(mNaviEnd);
    			calculateDriveRoute();
    		}
    	}
    };
    
    private static IntentFilter GuideIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ACTION_GUIDE);
        return intentFilter;
    }
}

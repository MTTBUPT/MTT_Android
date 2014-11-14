package com.mtt.fragments;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps.AMap.OnMarkerClickListener;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.maps.AMap;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.mtt.R;
import com.mtt.util.MapDatas;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class PathFragment extends Fragment implements OnMarkerClickListener,LocationSource
						,AMapLocationListener,OnClickListener{
	/** 获取该fragment view*/
	private View view;
	/** 开始记录轨迹按钮*/
	private Button btn_record;
	/** 是否开始记录轨迹*/
	private boolean isRecord = false;
	
	/** 地图资源*/
	private MapView mapView;
	private AMap aMap;
	/** 定位*/
	private OnLocationChangedListener mListener;
	private LocationManagerProxy mAMapLocationManager;
	/** 标注*/
	private MarkerOptions markerOptions;
	
	/** 当前定位点*/
	private LatLng ll;
	/** 上一次定位点*/
	private LatLng oldll;
	/** 轨迹信息记录*/
	private MapDatas mapdata=new MapDatas();
	private String fileName="MyMapDatas.txt";
	/** 上一次放置Marker的时间*/
	private long lastTime = 0;
	/** 放置marker的间隔*/
	private final long TIME_LOCATION = 600000;
	/** 已放置的marker数*/
	private int i = 0;
	/** 是否是第一个marker*/
	private boolean isFirstMarker = true;

	/** 保存信息文件*/
	private File file;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		view = inflater.inflate(R.layout.fragment_path, container, false);
		mapView=(MapView) view.findViewById(R.id.map);
		btn_record = (Button) view.findViewById(R.id.btn_pathfragment_start);
		btn_record.setOnClickListener(this);
		
		mapView.onCreate(savedInstanceState);
		init();
		
		return view;
	}


	private void init(){
		if(aMap==null){
			aMap=mapView.getMap();
			setUpMap();
		}
		
	}
	private void setUpMap() {
		aMap.setLocationSource(this);
		aMap.setOnMarkerClickListener(this);// 设置点击Marker事件监听器
		aMap.getUiSettings().setMyLocationButtonEnabled(true);
		aMap.getUiSettings().setCompassEnabled(true);
		aMap.getUiSettings().setScaleControlsEnabled(true);
		aMap.setMyLocationEnabled(true);
		aMap.setMapType(AMap.MAP_TYPE_NORMAL);
		aMap.setMyLocationType(AMap.LOCATION_TYPE_MAP_FOLLOW);
		aMap.setTrafficEnabled(true);
		try {
			file=mapdata.creatFile(fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}	
	public static String convertToTime(long time) {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = new Date(time);
		return df.format(date);
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.d("Map-Su", "------------------onResume");
		mapView.onResume();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("Map-Su", "------------------onPause");
		mapView.onPause();
	}
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d("Map-Su", "------------------onDestroy");
		mapView.onDestroy();
		deactivate();
	}
	
	@Override
	public void onLocationChanged(AMapLocation amaplocation) {
		// TODO Auto-generated method stub
		if(mListener!=null&&amaplocation!=null){
			if(amaplocation.getAMapException().getErrorCode()==0){
				mListener.onLocationChanged(amaplocation);
				ll=new LatLng(amaplocation.getLatitude(),amaplocation.getLongitude());

				if(isRecord){
					/** MarkerOption ：anchor（）定义marker图标的锚点
					 * 	draggable()是否设置可被拖动  setFlat是否平贴地图
					 */
				    markerOptions=new MarkerOptions().anchor(0.5f, 0.5f)
				  			.visible(true).position(ll)
				  			.title("第几个位置"+i)
				  		    .snippet("经度："+amaplocation.getLatitude()+"纬度："+amaplocation.getLongitude()+"精度"+amaplocation.getAccuracy())
				  		    .draggable(false);
				    markerOptions.setFlat(true);
			  		Log.d("Map-Su", "Location is:" + amaplocation.getLatitude() + "lastTime: " + lastTime);
			  		Log.d("Map-Su", "currentTime: " + System.currentTimeMillis());
				  	if(System.currentTimeMillis() - lastTime > TIME_LOCATION || isFirstMarker){
				  		aMap.addMarker(markerOptions);	
				  		Log.d("Map-Su", "Marker is:" + amaplocation.getLatitude());
				  		i++;
				  		isFirstMarker = false;
					  	lastTime = System.currentTimeMillis();
				  	}
				}
			}
		}
		
		if(isRecord){
			//地图上划线
			aMap.addPolyline((new PolylineOptions()).add(oldll,ll).color(Color.RED));
			oldll=ll;
			String strll=ll.toString();
			String time=convertToTime(amaplocation.getTime());
			String str=time+strll;
			try {
				mapdata.write2SD(file,str);	
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	@Override
	public void activate(OnLocationChangedListener listener) {
		// TODO Auto-generated method stub
		mListener=listener;
		if(mAMapLocationManager==null){
			mAMapLocationManager=LocationManagerProxy.getInstance(getActivity());
			mAMapLocationManager.requestLocationData(LocationProviderProxy.AMapNetwork,6000,5, this);
		}
		mAMapLocationManager.setGpsEnable(true);
	  	Log.d("Map-Su", "Loction activate");
	}
	
	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		mListener=null;
		if(mAMapLocationManager!=null){
			mAMapLocationManager.removeUpdates(this);
			mAMapLocationManager.destroy();
		}
		mAMapLocationManager=null;
  	    Log.d("Map-Su", "Loction deactivate");
	}
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean onMarkerClick(Marker arg0) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_pathfragment_start:
			if(isRecord){
				btn_record.setText("Start");
				isRecord = false;
			}else {
				btn_record.setText("Pause");
				isRecord = true;
			}
			
			break;

		default:
			break;
		}
	}
		
}

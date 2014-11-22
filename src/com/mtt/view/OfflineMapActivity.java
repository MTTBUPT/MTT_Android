package com.mtt.view;

import com.amap.api.mapcore.util.t;
import com.amap.api.maps.MapView;
import com.amap.api.maps.MapsInitializer;
import com.amap.api.maps.offlinemap.OfflineMapManager;
import com.amap.api.maps.offlinemap.OfflineMapStatus;
import com.amap.api.maps.offlinemap.OfflineMapManager.OfflineMapDownloadListener;
import com.mtt.R;
import com.mtt.util.ToastUtil;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class OfflineMapActivity extends Activity implements OnClickListener,OfflineMapDownloadListener{

	private Button btn_startmap,btn_pausemap,btn_stopmap,btn_deletemap;
	private ImageButton ib_return;
	private TextView tv_rate;
	
	private OfflineMapManager amapManager = null;
	private int completeCode;
	private boolean isStart = false;
	
	private MapView mapView;
	
	private Handler mHandler = new Handler(){
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			if(msg.what == -2){
				tv_rate.setText("正在下载" + completeCode + "%");
			}else if (msg.what == OfflineMapStatus.UNZIP) {
				tv_rate.setText("正在解压" + completeCode + "%");
			}else if (msg.what == OfflineMapStatus.PAUSE) {
				tv_rate.setText("暂停");
			}else if (msg.what == OfflineMapStatus.STOP) {
				tv_rate.setText("停止");
			}
		};
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_offlinemap);
		MapsInitializer.sdcardDir = getSdCacheDir(this);
		
		btn_startmap = (Button) findViewById(R.id.offlinemap_btn_start);
		btn_startmap.setOnClickListener(this);
		btn_pausemap = (Button) findViewById(R.id.offlinemap_btn_pause);
		btn_pausemap.setOnClickListener(this);
		btn_stopmap = (Button) findViewById(R.id.offlinemap_btn_stop);
		btn_stopmap.setOnClickListener(this);
		btn_deletemap = (Button) findViewById(R.id.offlinemap_btn_delete);
		btn_deletemap.setOnClickListener(this);
		
		ib_return = (ImageButton) findViewById(R.id.offlinemap_btn_return);
		ib_return.setOnClickListener(this);
		
		tv_rate = (TextView) findViewById(R.id.offlinemap_tv_rate);
		
		mapView=new MapView(this);
		amapManager = new OfflineMapManager(this, this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.offlinemap_btn_start:
			try {
					isStart = amapManager.downloadByCityName("北京"); 
					if (!isStart) {
						ToastUtil.show(this, "下载离线地图数据失败"); 
						} else {
							ToastUtil.show(this, "开始下载离线地图数据"); 
						}
				} catch (Exception e) { 
					e.printStackTrace();
					ToastUtil.show(this, "开启下载失败,请检查网络是否开启!"); 
				}
			break;
		case R.id.offlinemap_btn_pause:
			amapManager.pause();
			ToastUtil.show(this, "暂停下载离线地图数据");
			break;
		case R.id.offlinemap_btn_stop:
			amapManager.stop();
			ToastUtil.show(this, "停止下载离线地图数据");
			break;
		case R.id.offlinemap_btn_delete:
			amapManager.remove("北京");
			ToastUtil.show(this, "删除北京离线地图");
			tv_rate.setText("下载离线地图包");
			break;
		case R.id.offlinemap_btn_return:
			startActivity(new Intent(OfflineMapActivity.this,SubFunctionActivity.class));
			break;
		}
	}

	@Override
	public void onDownload(int status, int completeCode, String downName) {
		// TODO Auto-generated method stub
		switch (status) {
		case OfflineMapStatus.SUCCESS:
			changeOfflineMapState(OfflineMapStatus.SUCCESS);
			break;
		case OfflineMapStatus.LOADING:
			OfflineMapActivity.this.completeCode = completeCode;
			changeOfflineMapState(-2);// -2表示正在下载离线地图数据
			break;
		case OfflineMapStatus.UNZIP:
			OfflineMapActivity.this.completeCode = completeCode;
			changeOfflineMapState(OfflineMapStatus.UNZIP);
			break;
		case OfflineMapStatus.WAITING:
			break;
		case OfflineMapStatus.PAUSE:
			changeOfflineMapState(OfflineMapStatus.PAUSE);
			break;
		case OfflineMapStatus.STOP:
			changeOfflineMapState(OfflineMapStatus.STOP);
			break;
		case OfflineMapStatus.ERROR:
			break;
		}
	}
		
	private void changeOfflineMapState(int status){
		if(status == -2){
			Message message = new Message();
			message.what = -2;
			mHandler.sendMessage(message);
		}else if(status == OfflineMapStatus.UNZIP){
			Message message = new Message();
			message.what = OfflineMapStatus.UNZIP;
			mHandler.sendMessage(message);
		}else if(status == OfflineMapStatus.PAUSE){
			Message message = new Message();
			message.what = OfflineMapStatus.PAUSE;
			mHandler.sendMessage(message);
		}else if (status == OfflineMapStatus.STOP) {
			Message message = new Message();
			message.what = OfflineMapStatus.STOP;
			mHandler.sendMessage(message);
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(mapView!=null){
			mapView.onDestroy();
		}
	}

	/**
	 * 获取map 缓存和读取目录
	 */
	private String getSdCacheDir(Context context) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			java.io.File fExternalStorageDirectory = Environment
					.getExternalStorageDirectory();
			java.io.File autonaviDir = new java.io.File(
					fExternalStorageDirectory, "amapsdk");
			boolean result = false;
			if (!autonaviDir.exists()) {
				result = autonaviDir.mkdir();
			}
			java.io.File minimapDir = new java.io.File(autonaviDir,
					"offlineMap");
			if (!minimapDir.exists()) {
				result = minimapDir.mkdir();
			}
			return minimapDir.toString() + "/";
		} else {
			return "";
		}
	}
}

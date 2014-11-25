package com.mtt.view;
import com.mtt.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.mtt.customview.RoundViewOnTouchListener;
import com.mtt.customview.RoundView;
import com.mtt.customview.StartDialogView;

/**软件首页面
 * @author Kerry 
 * */
public class MainActivity extends Activity implements RoundViewOnTouchListener{
	
	// 圆盘view
	RoundView rv;
	// 开始弹窗view
	StartDialogView sdView;
	
	/** 触发背景*/
	private View view_mask;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rv = (RoundView) findViewById(R.id.roundview);
		rv.setOnTouchListener(this);
		
		view_mask = findViewById(R.id.main_view_mask);
		sdView = (StartDialogView) findViewById(R.id.main_startdialog);
		sdView.setEnabled(true);
		
		sdView.setOnStatusListener(new StartDialogView.onStatusListener() {
			
			@Override
			public void onShow() {
				// TODO Auto-generated method stub
				view_mask.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
				view_mask.setVisibility(View.GONE);
			}
		});
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		rv.resetTouch();
	}

	/**
	 * 打开设置页面
	 */
	public void openSetting(View v)
	{
		Intent i = new Intent();
		i.setClass(MainActivity.this, SettingsActivity.class);
		MainActivity.this.startActivity(i);		
	}
	
	/**
	 * 打开user页面
	 */
	public void openMine(View v)
	{
		Intent i = new Intent();
		i.setClass(MainActivity.this, UserActivity.class);
		MainActivity.this.startActivity(i);		
	}
	
	/**
	 * open more page
	 */
	public void openMore(View v)
	{
		Intent i = new Intent();
		i.setClass(MainActivity.this, MoreActivity.class);
		MainActivity.this.startActivity(i);		
	}
	
	/**
	 * open more page
	 */
	public void openBluetoothLe(View v)
	{
		Toast.makeText(this, "蓝牙页面即将完成", Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onTouchEvent(View v, MotionEvent event, int i) {
		// TODO Auto-generated method stub
		if (i == RoundView.COM_MTT_ROUNDVIEW_TOUCH_GUIDE) {
			//跳转导航页面
			Intent intent = new Intent(MainActivity.this,SubFunctionActivity.class);
			intent.putExtra("subpage", SubFunctionActivity.COM_MTT_FRAGMENT_GUIDE);
			startActivity(intent);
		}else if (i == RoundView.COM_MTT_ROUNDVIEW_TOUCH_MABIAO) {
			//跳转码表页面
			Intent intent = new Intent(MainActivity.this,SubFunctionActivity.class);
			intent.putExtra("subpage", SubFunctionActivity.COM_MTT_FRAGMENT_MABIAO);
			startActivity(intent);
		}else if (i == RoundView.COM_MTT_ROUNDVIEW_TOUCH_CAMERA) {
			//跳转相机页面
			Intent intent = new Intent(MainActivity.this,SubFunctionActivity.class);
			intent.putExtra("subpage", SubFunctionActivity.COM_MTT_FRAGMENT_CAMERA);
			startActivity(intent);
		}else if (i == RoundView.COM_MTT_ROUNDVIEW_TOUCH_TOOL) {
			//跳转工具页面
		}else if (i == RoundView.COM_MTT_ROUNDVIEW_TOUCH_START) {
			//点击开始按钮
			if(!sdView.isShow()){
				sdView.show();
				return false;
			}
		}
		return true;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		int x,y=0;
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x=(int)ev.getX();
			y=(int)ev.getY();
			// 开始弹窗处于展开状态
			if(sdView.isShow()){
				if(sdView.couldTouchClose(x, y)){
					sdView.dismiss();
					return false;
				}
				
				// 点击tab
				switch (sdView.TouchTab(x, y)) {
				case StartDialogView.COM_MTT_START_MUSIC:
					Toast.makeText(this, "点击打开音乐", Toast.LENGTH_SHORT).show();
					return false;
				case StartDialogView.COM_MTT_START_DATA:
					Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
					if(sdView.getIsTouchData()){
						sdView.setIsTouchData(false);
					}else {
						sdView.setIsTouchData(true);
					}
					return false;
				case StartDialogView.COM_MTT_START_PATH:
					Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
					if(sdView.getIsTouchPath()){
						sdView.setIsTouchPath(false);
					}else {
						sdView.setIsTouchPath(true);
					}
					return false;
				case StartDialogView.COM_MTT_START_STOPWATCH:
					Toast.makeText(this, "点击打开秒表", Toast.LENGTH_SHORT).show();
					return false;
				case StartDialogView.COM_MTT_START:
					Toast.makeText(this, "开始", Toast.LENGTH_SHORT).show();
					sdView.dismiss();
					return false;
				default:
					break;
				}
			}
			break;

		default:
			break;
		}
		return super.dispatchTouchEvent(ev);
	}

	
}

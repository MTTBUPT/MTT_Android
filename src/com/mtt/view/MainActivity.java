package com.mtt.view;
import com.mtt.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.mtt.customview.RoundViewOnTouchListener;
import com.mtt.customview.RoundView;

/**软件首页面
 * @author Kerry 
 * */
public class MainActivity extends Activity implements RoundViewOnTouchListener{
	RoundView rv;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		rv = (RoundView) findViewById(R.id.roundview);
		rv.setOnTouchListener(this);
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
//		Intent i = new Intent();
//		i.setClass(MainActivity.this, MoreActivity.class);
//		MainActivity.this.startActivity(i);		
		Toast.makeText(this, "蓝牙页面即将完成", Toast.LENGTH_SHORT).show();

	}

	@Override
	public boolean onTouchEvent(View v, MotionEvent event, int i) {
		// TODO Auto-generated method stub
		if (i == 1) {
			//跳转导航页面
			Intent intent = new Intent(MainActivity.this,SubFunctionActivity.class);
			intent.putExtra("subpage", 4);
			startActivity(intent);
		}else if (i ==2) {
			//跳转码表页面
			Intent intent = new Intent(MainActivity.this,SubFunctionActivity.class);
			intent.putExtra("subpage", 0);
			startActivity(intent);
		}else if (i ==3) {
			//跳转轨迹页面
			Intent intent = new Intent(MainActivity.this,SubFunctionActivity.class);
			intent.putExtra("subpage", 5);
			startActivity(intent);
		}else if (i ==4) {
			//跳转音乐页面
			Intent intent = new Intent(MainActivity.this,SubFunctionActivity.class);
			intent.putExtra("subpage", 2);
			startActivity(intent);
		}else if (i ==5) {
			//跳转相机页面
			Intent intent = new Intent(MainActivity.this,SubFunctionActivity.class);
			intent.putExtra("subpage", 1);
			startActivity(intent);
		}else if (i ==6) {
			//跳转秒表页面
			Intent intent = new Intent(MainActivity.this,SubFunctionActivity.class);
			intent.putExtra("subpage", 3);
			startActivity(intent);
		}
		return true;
	}

}

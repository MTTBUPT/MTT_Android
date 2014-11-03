package com.mtt.view;

import com.mtt.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WeatherActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_weather);
	}
	
	/**
	 * 返回更多页面
	 */
	public void backmore(View v)
	{
		Intent i = new Intent();
		i.setClass(WeatherActivity.this, MoreActivity.class);
		WeatherActivity.this.startActivity(i);		
	}

}

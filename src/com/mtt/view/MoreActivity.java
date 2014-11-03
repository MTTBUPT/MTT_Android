package com.mtt.view;

import com.mtt.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MoreActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_more);
	}
	
	/**
	 * 返回主页面
	 */
	public void backmain(View v)
	{
		Intent i = new Intent();
		i.setClass(MoreActivity.this, MainActivity.class);
		MoreActivity.this.startActivity(i);		
	}
	
	/**
	 * 打开天气消息
	 */
	public void openWeather(View v)
	{
		Intent i = new Intent();
		i.setClass(MoreActivity.this, WeatherActivity.class);
		MoreActivity.this.startActivity(i);		
	}
	
	/**
	 * 打开推荐路线
	 */
	public void openRecommendedpath(View v)
	{
		Intent i = new Intent();
		i.setClass(MoreActivity.this, RecommendedpathActivity.class);
		MoreActivity.this.startActivity(i);		
	}
	
	/**
	 * onlinestore主页面
	 */
	public void openOnlinestore(View v)
	{
		Intent i = new Intent();
		i.setClass(MoreActivity.this, OnlinestoreActivity.class);
		MoreActivity.this.startActivity(i);		
	}
	
	/**
	 * CommonqActivity页面
	 */
	public void openCommonq(View v)
	{
		Intent i = new Intent();
		i.setClass(MoreActivity.this, CommonqActivity.class);
		MoreActivity.this.startActivity(i);		
	}
	
	/**
	 * 返回主页面
	 */
	public void openAboutus(View v)
	{
		Intent i = new Intent();
		i.setClass(MoreActivity.this, AboutusActivity.class);
		MoreActivity.this.startActivity(i);		
	}


}

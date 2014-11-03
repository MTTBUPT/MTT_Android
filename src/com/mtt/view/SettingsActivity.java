package com.mtt.view;


import com.mtt.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class SettingsActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
	}
	
	/**
	 * 返回主页面
	 */
	public void backmain(View v)
	{
		Intent i = new Intent();
		i.setClass(SettingsActivity.this, MainActivity.class);
		SettingsActivity.this.startActivity(i);		
	}


}

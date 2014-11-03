package com.mtt.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mtt.R;

public class OnlinestoreActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_onlinestore);
	}
	
	/**
	 * 返回更多页面
	 */
	public void backmore(View v)
	{
		Intent i = new Intent();
		i.setClass(OnlinestoreActivity.this, MoreActivity.class);
		OnlinestoreActivity.this.startActivity(i);		
	}

}

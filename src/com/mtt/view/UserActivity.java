package com.mtt.view;

import com.mtt.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class UserActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
	}
	
	/**
	 * 返回主页面
	 */
	public void backmain(View v)
	{
		Intent i = new Intent();
		i.setClass(UserActivity.this, MainActivity.class);
		UserActivity.this.startActivity(i);		
	}

}

package com.mtt.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mtt.R;

public class AboutusActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutus);
	}

	/**
	 * 返回更多页面
	 */
	public void backmore(View v)
	{
		Intent i = new Intent();
		i.setClass(AboutusActivity.this, MoreActivity.class);
		AboutusActivity.this.startActivity(i);		
	}
}


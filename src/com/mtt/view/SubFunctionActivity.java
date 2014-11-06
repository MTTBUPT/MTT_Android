package com.mtt.view;

import java.util.ArrayList;
import java.util.List;

import com.mtt.R;
import com.mtt.fragments.*;
import com.mtt.customview.*;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MotionEvent;
import android.view.View;

/**
 * 子功能页面（Fragment+Viewpager）
 * 手势滑动切换以及动画
 * 
 * @author Kerry
 * 
 */
public class SubFunctionActivity extends FragmentActivity{
	/** 页卡内容*/
	private ViewPager mPager;
	/** Fragment列表*/
	private List<Fragment> fragments = new ArrayList<Fragment>();
	/** viewpage适配器*/
	private MyPagerAdapter adapter;
	
	/** 触发背景*/
	private View view_mask;
	/** 自定义对话框*/
	private DialogView dialog_view;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_sub_functions);
		InitViewPager();
		
		view_mask = findViewById(R.id.view_mask);
		dialog_view = (DialogView) findViewById(R.id.dialog_view);
		dialog_view.setEnabled(true);
		
		//设置登录界面状态监听
		dialog_view.setOnStatusListener(new DialogView.onStatusListener() {
			@Override
			public void onShow() {
				//显示
				view_mask.setVisibility(View.VISIBLE);
			}
		
			@Override
			public void onDismiss() {
				//隐藏
				view_mask.setVisibility(View.GONE);
			}
		});
	}
	
	
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		int x,y;
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x = (int) ev.getX();
			y = (int) ev.getY();
			if(dialog_view.isShow()){
				if(dialog_view.couldTouchClose(x, y)){
					dialog_view.startMoveAnim(dialog_view.mPointY*3/2, - dialog_view.mPointY*3/2, dialog_view.mDuration);
					dialog_view.setShow(false);
					dialog_view.changed();
					return false;
				}
			}
			break;
		}
		return super.dispatchTouchEvent(ev);
	}


	/**
	 * ViewPager适配器
	 */
	public class MyPagerAdapter extends FragmentPagerAdapter {
		public List<Fragment> fragments;

		public MyPagerAdapter(FragmentManager fm){
			super(fm);
		}
		
		public MyPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
			// TODO Auto-generated constructor stub
			super(fm);
			this.fragments = fragments;
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
			return fragments.get(arg0);
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			// 这里Destroy的是Fragment的视图层次，而不是Destroy Fragment的对象
			super.destroyItem(container, position, object);
		}
		
	}
	
	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.vPager);
		
		fragments.add(new MabiaoFragment());
		fragments.add(new CameraFragment());
		fragments.add(new MusicFragment());
		fragments.add(new StopWatchFragment());
		
		adapter = new MyPagerAdapter(getSupportFragmentManager(),fragments);
		mPager.setCurrentItem(0);
		mPager.setAdapter(adapter);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
}

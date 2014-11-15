package com.mtt.view;

import java.util.ArrayList;
import java.util.List;

import com.mtt.R;
import com.mtt.fragments.*;
import com.mtt.util.ToastUtil;
import com.mtt.customview.*;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
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
	
	/**　传值*/
	private Intent intent;
	/**　子功能页面参数*/
	private int subpage=1;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_sub_functions);
		intent = getIntent();
		subpage = intent.getIntExtra("subpage",1);
		
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
					// 关闭dialog_view
					dialog_view.dismiss();
					return false;
				}
				
				switch(subpage){
				case 0:
					// 码表页面
					switch (dialog_view.touchMidTab(x, y)) {
					case 1:
						ToastUtil.show(this, "tab1");
						dialog_view.dismiss();
						return false;
					case 2:
						ToastUtil.show(this, "tab2");
						dialog_view.dismiss();
						return false;
					case 3:
						ToastUtil.show(this, "tab3");
						dialog_view.dismiss();
						return false;
					case 4:
						ToastUtil.show(this, "坡度清零");
				        final Intent initIntent = new Intent(MabiaoFragment.ACTION_STEEP_INITIAL);
				        LocalBroadcastManager.getInstance(this).sendBroadcast(initIntent);
						dialog_view.dismiss();
						return false;
					case 5:
						dialog_view.dismiss();
						Intent intent = new Intent(SubFunctionActivity.this,MainActivity.class);
						startActivity(intent);
						return false;
					default:
						break;
					}
					break;
				case 1:
					// 相机页面
					switch (dialog_view.touchMidTab(x, y)) {
					case 1:
						ToastUtil.show(this, "tab11");
						dialog_view.dismiss();
						return false;
					case 2:
						ToastUtil.show(this, "tab21");
                        Intent i = new Intent();
                        i.setType("image/*");
                        i.setAction(Intent.ACTION_GET_CONTENT);
                        startActivity(i);
						dialog_view.dismiss();
						return false;
					case 3:
						ToastUtil.show(this, "tab31");
						dialog_view.dismiss();
						return false;
					case 4:
						ToastUtil.show(this, "tab41");
						dialog_view.dismiss();
						return false;
					case 5:
						dialog_view.dismiss();
						Intent intent = new Intent(SubFunctionActivity.this,MainActivity.class);
						startActivity(intent);
						return false;
					default:
						break;
					}
					break;
				case 2:
					// 音乐
					switch (dialog_view.touchMidTab(x, y)) {
					case 1:
						ToastUtil.show(this, "tab12");
						dialog_view.dismiss();
						return false;
					case 2:
						ToastUtil.show(this, "tab22");
						dialog_view.dismiss();
						return false;
					case 3:
						ToastUtil.show(this, "tab32");
						dialog_view.dismiss();
						return false;
					case 4:
						ToastUtil.show(this, "tab42");
						dialog_view.dismiss();
						return false;
					case 5:
						dialog_view.dismiss();
						Intent intent = new Intent(SubFunctionActivity.this,MainActivity.class);
						startActivity(intent);
						return false;
					default:
						break;
					}
					break;
				case 3:
					// 秒表页面
					switch (dialog_view.touchMidTab(x, y)) {
					case 1:
						ToastUtil.show(this, "tab1");
						dialog_view.dismiss();
						return false;
					case 2:
						ToastUtil.show(this, "tab2");
						dialog_view.dismiss();
						return false;
					case 3:
						ToastUtil.show(this, "tab3");
						dialog_view.dismiss();
						return false;
					case 4:
						ToastUtil.show(this, "tab4");
						dialog_view.dismiss();
						return false;
					case 5:
						dialog_view.dismiss();
						Intent intent = new Intent(SubFunctionActivity.this,MainActivity.class);
						startActivity(intent);
						return false;
					default:
						break;
					}
					break;
				case 4:
					// 导航页面
					switch (dialog_view.touchMidTab(x, y)) {
					case 1:
						ToastUtil.show(this, "tab1");
						dialog_view.dismiss();
						return false;
					case 2:
						ToastUtil.show(this, "tab2");
						dialog_view.dismiss();
						return false;
					case 3:
						ToastUtil.show(this, "tab3");
						dialog_view.dismiss();
						return false;
					case 4:
						ToastUtil.show(this, "tab4");
						dialog_view.dismiss();
						return false;
					case 5:
						dialog_view.dismiss();
						Intent intent = new Intent(SubFunctionActivity.this,MainActivity.class);
						startActivity(intent);
						return false;
					default:
						break;
					}
					break;
				case 5:
					// 轨迹页面
					switch (dialog_view.touchMidTab(x, y)) {
					case 1:
						ToastUtil.show(this, "tab1");
						dialog_view.dismiss();
						return false;
					case 2:
						ToastUtil.show(this, "tab2");
						dialog_view.dismiss();
						return false;
					case 3:
						ToastUtil.show(this, "tab3");
						dialog_view.dismiss();
						return false;
					case 4:
						ToastUtil.show(this, "tab4");
						dialog_view.dismiss();
						return false;
					case 5:
						dialog_view.dismiss();
						Intent intent = new Intent(SubFunctionActivity.this,MainActivity.class);
						startActivity(intent);
						return false;
					default:
						break;
					}
					break;
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

/*		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			// 这里Destroy的是Fragment的视图层次，而不是Destroy Fragment的对象
			super.destroyItem(container, position, object);
		}*/
		
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
		fragments.add(new GuideFragment());
		fragments.add(new PathFragment());
		
		adapter = new MyPagerAdapter(getSupportFragmentManager(),fragments);
		mPager.setAdapter(adapter);
		mPager.setCurrentItem(subpage);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				subpage = arg0;
				dialog_view.setPageNum(subpage);
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

package com.mtt.view;

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
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * 子功能页面（Fragment+Viewpager）
 * 手势滑动切换以及动画
 * 
 * @author Kerry
 * 
 */
public class SubFunctionActivity extends FragmentActivity{ 
	
	public static final int COM_MTT_FRAGMENT_CAMERA = 0;
	public static final int COM_MTT_FRAGMENT_MABIAO = 1;
	public static final int COM_MTT_FRAGMENT_GUIDE = 2;
	
	/** 页卡内容*/
	private ViewPager mPager;
	/** viewpage适配器*/
	private MyPagerAdapter adapter;
	
	/** 触发背景*/
	private View view_mask;
	/** 自定义对话框*/
	private DialogView dialog_view;
	/** 开关弹窗view*/
	private SwitchDialogView switchDialogView;
	
	/**　传值*/
	private Intent intent;
	/**　子功能页面参数*/
	private int subpage=COM_MTT_FRAGMENT_MABIAO;
	private boolean isFirstPage = true;
	
	private MabiaoFragment mabiaoFragment = new MabiaoFragment();
	private CameraFragment cameraFragment = new CameraFragment();
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.activity_sub_functions);
		
		// 获取Intent传值，确定跳转页面
		intent = getIntent();
		subpage = intent.getIntExtra("subpage",COM_MTT_FRAGMENT_MABIAO);

		// 初始化ViewPager
		InitViewPager();
		
		view_mask = findViewById(R.id.view_mask);
		dialog_view = (DialogView) findViewById(R.id.dialog_view);
		dialog_view.setEnabled(true);
		switchDialogView = (SwitchDialogView) findViewById(R.id.sub_switchdialog);
		switchDialogView.setEnabled(true);
		
		//设置弹窗状态监听
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
		
		//设置弹窗状态监听
		switchDialogView.setOnStatusListener(new SwitchDialogView.onStatusListener() {
			
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
	public boolean dispatchTouchEvent(MotionEvent ev) {
		// TODO Auto-generated method stub
		int x,y;
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			x = (int) ev.getX();
			y = (int) ev.getY();
			
			// 开关弹窗
			if(switchDialogView.isShow()){
				// 点击弹窗外关闭弹窗
				if(switchDialogView.couldTouchClose(x, y)){
					switchDialogView.dismiss();
					return false;
				}
				
				// 点击tab
				switch (switchDialogView.TouchTab(x, y)) {
				case SwitchDialogView.COM_MTT_START_MUSIC:
					Toast.makeText(this, "点击打开音乐", Toast.LENGTH_SHORT).show();
					Intent music_intent = new Intent(SubFunctionActivity.this,MusicActivity.class);
					startActivity(music_intent);
					return false;
				case SwitchDialogView.COM_MTT_START_DATA:
					Toast.makeText(this, "2", Toast.LENGTH_SHORT).show();
					if(switchDialogView.getIsTouchData()){
						switchDialogView.setIsTouchData(false);
					}else {
						switchDialogView.setIsTouchData(true);
					}
					return false;
				case SwitchDialogView.COM_MTT_START_PATH:
					Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
					if(switchDialogView.getIsTouchPath()){
						switchDialogView.setIsTouchPath(false);
					}else {
						switchDialogView.setIsTouchPath(true);
					}
					return false;
				case SwitchDialogView.COM_MTT_START_STOPWATCH:
					Toast.makeText(this, "点击打开秒表", Toast.LENGTH_SHORT).show();
					Intent stopwatch_intent = new Intent(SubFunctionActivity.this,StopWatchActivity.class);
					startActivity(stopwatch_intent);
					return false;
				case SwitchDialogView.COM_MTT_START:
					Toast.makeText(this, "开始", Toast.LENGTH_SHORT).show();
					switchDialogView.dismiss();
					return false;
				default:
					break;
				}
				
			}else {
				if(x>0 && x<200 && y>dialog_view.mPointY*3/2 && y<dialog_view.mPointY*2){
					switchDialogView.show();
				}
			}
			
			// 功能弹窗展开状态
			if(dialog_view.isShow()){
				// 点击弹窗外关闭弹窗
				if(dialog_view.couldTouchClose(x, y)){
					dialog_view.dismiss();
					return false;
				}
				
				// 点击弹窗内的tab
				switch(subpage){
				case COM_MTT_FRAGMENT_MABIAO:
					// 码表页面
					switch (dialog_view.touchMidTab(x, y)) {
					case DialogView.COM_MTT_DIALOGVIEW_TAB_A:
						ToastUtil.show(this, "清除均速");
						dialog_view.dismiss();
						return false;
					case DialogView.COM_MTT_DIALOGVIEW_TAB_B:
						ToastUtil.show(this, "清除里程");
						dialog_view.dismiss();
						return false;
					case DialogView.COM_MTT_DIALOGVIEW_TAB_C:
						ToastUtil.show(this, "清除时间");
						dialog_view.dismiss();
						return false;
					case DialogView.COM_MTT_DIALOGVIEW_TAB_D:
				        final Intent initIntent = new Intent(MabiaoFragment.ACTION_STEEP_INITIAL);
				        LocalBroadcastManager.getInstance(this).sendBroadcast(initIntent);
						dialog_view.dismiss();
						return false;
					case DialogView.COM_MTT_DIALOGVIEW_TAB_BACK:
						Intent intent = new Intent(SubFunctionActivity.this,MainActivity.class);
						startActivity(intent);
						return false;
					default:
						break;
					}
					break;
				case COM_MTT_FRAGMENT_CAMERA:
					// 相机页面
					switch (dialog_view.touchMidTab(x, y)) {
					case DialogView.COM_MTT_DIALOGVIEW_TAB_A:
						ToastUtil.show(this, "延时十秒");
						dialog_view.dismiss();
						return false;
					case DialogView.COM_MTT_DIALOGVIEW_TAB_B:
						ToastUtil.show(this, "图库");
                        Intent i = new Intent();
                        i.setType("image/*");
                        i.setAction(Intent.ACTION_GET_CONTENT);
                        startActivity(i);
						dialog_view.dismiss();
						return false;
					case DialogView.COM_MTT_DIALOGVIEW_TAB_BACK:
						Intent intent = new Intent(SubFunctionActivity.this,MainActivity.class);
						startActivity(intent);
						return false;
					default:
						break;
					}
					break;
				case COM_MTT_FRAGMENT_GUIDE:
					// 导航页面
					switch (dialog_view.touchMidTab(x, y)) {
					case DialogView.COM_MTT_DIALOGVIEW_TAB_A:
						ToastUtil.show(this, "路线规划");
						Intent desIntent = new Intent(SubFunctionActivity.this,DestinationActivity.class);
						startActivityForResult(desIntent, 1);
						dialog_view.dismiss();
						return false;
					case DialogView.COM_MTT_DIALOGVIEW_TAB_B:
						ToastUtil.show(this, "路线概览");
						dialog_view.dismiss();
						return false;
					case DialogView.COM_MTT_DIALOGVIEW_TAB_C:
						ToastUtil.show(this, "离线地图");
						Intent offlineIntent = new Intent(SubFunctionActivity.this,OfflineMapActivity.class);
						startActivity(offlineIntent);
						dialog_view.dismiss();
						return false;
					case DialogView.COM_MTT_DIALOGVIEW_TAB_D:
						ToastUtil.show(this, "周边信息");
						dialog_view.dismiss();
						return false;
					case DialogView.COM_MTT_DIALOGVIEW_TAB_BACK:
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
	public class MyPagerAdapter extends FragmentStatePagerAdapter {
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
			return 3;
		}

		@Override
		public Fragment getItem(int position) {
			// TODO Auto-generated method stub
            Fragment f = null;  
            if(position == COM_MTT_FRAGMENT_CAMERA){  
            	f = cameraFragment;
            }else if(position == COM_MTT_FRAGMENT_MABIAO){  
                f = mabiaoFragment;
            }else if(position == COM_MTT_FRAGMENT_GUIDE){  
                f = new GuideFragment(isFirstPage);
            }
            
			dialog_view.setPageNum(subpage);
			return f;
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			// TODO Auto-generated method stub
			// 这里Destroy的是Fragment的视图层次，而不是Destroy Fragment的对象
		}
		
	}
	
	/**
	 * 初始化ViewPager
	 */
	private void InitViewPager() {
		mPager = (ViewPager) findViewById(R.id.vPager);
		
		adapter = new MyPagerAdapter(getSupportFragmentManager());
		mPager.setOffscreenPageLimit(3);
		mPager.setAdapter(adapter);
		mPager.setCurrentItem(subpage);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				subpage = arg0;
				dialog_view.setPageNum(subpage);
				isFirstPage = false;
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
	
	// 从终点设置页面获取返回值（经纬度）
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode == 1 && resultCode == 1){
			ToastUtil.show(SubFunctionActivity.this, "开始规划路径 SubFunction");
			// 获取Intent中数据
			Bundle mBundle = data.getExtras();
			double endLatitude = mBundle.getDouble("mlatitude");
			double endLongtitude = mBundle.getDouble("mlongtitude");
			
			// 向fragment发送广播
	        final Intent guideIntent = new Intent(GuideFragment.ACTION_GUIDE);
			Bundle Bundle = new Bundle();
			Bundle.putDouble("mlatitude", endLatitude);
			Bundle.putDouble("mlongtitude", endLongtitude);
			guideIntent.putExtras(Bundle);
	        LocalBroadcastManager.getInstance(this).sendBroadcast(guideIntent);
		}
	}
}

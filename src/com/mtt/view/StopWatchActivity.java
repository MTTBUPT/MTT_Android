package com.mtt.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.mtt.R;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/** 
 * 秒表功能页面
 * @author Kerry Zhouyoqin
 * */
public class StopWatchActivity extends Activity{

	public static final String TAG = "com.mtt.view.StopWatchActivity";

	/** 当前秒数*/
	private long mlCount = 0;
	/** 计次时秒数*/
	private long mCurrentCount = 0;
	
	private TextView mytime,myCurrentTime;
	private ImageButton btnStartStop,btnCountReset;
	private ListView stopwatch_record;
	List<Map<String, String>> listItems = new ArrayList<Map<String, String>>();;
	SimpleAdapter simpleAdapter;

	private Timer timer = null;
	private TimerTask task = null;
	private Handler handler = null;
	private Message msg = null;
	
	/** 是否在计数*/
	private boolean IsRunningFlg = false;
	/** 计次次数*/
	private int timeFlg = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    Log.d("Fragment_D", "-------------D-------OnCreate()");
		setContentView(R.layout.activity_stopwatch);

	    
		initView();  // 初始化 
		
        // Handle timer message
        handler = new Handler(){
    		@Override
    		public void handleMessage(Message msg) {
    			// TODO Auto-generated method stub
    			switch(msg.what) {
    			case 1:
    				mlCount++;
    				int totalSec = 0;
    				int yushu = 0;
    				
					totalSec = (int)(mlCount / 10);
        			yushu = (int)(mlCount % 10);
					
    				
    				// Set time display
    				int min = (totalSec / 60);
    				int sec = (totalSec % 60);
    				
    				// 设置计次后时间
    				long mThisCount = mlCount - mCurrentCount;
    				int current_totalSec = 0;
    				int current_yushu = 0;
    				
    				current_totalSec = (int)(mThisCount/10);
    				current_yushu = (int)(mThisCount%10);
    				int current_min = (current_totalSec/60);
    				int current_sec = (current_totalSec%60);
    				try{

    					mytime.setText(String.format("%1$02d:%2$02d:%3$d", min, sec, yushu));
    					myCurrentTime.setText(String.format("%1$02d:%2$02d:%3$d", current_min, current_sec, current_yushu));
    					
    				} catch(Exception e) {
    					mytime.setText("" + min + ":" + sec + ":" + yushu);
    					e.printStackTrace();
    					Log.e("MyTimer onCreate", "Format string error.");
    				}
    				break;
    				
    			default:
    				break;
    			}
    			
    			super.handleMessage(msg);
    		}
    	};
 
        btnStartStop.setOnClickListener(startStopListener);
        btnCountReset.setOnClickListener(countResetListener);
	}


	@Override
	public void onResume() {
		// TODO Auto-generated method stub
	    Log.d("Fragment_D", "-------------D-------onResume()");

		super.onResume();
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
	    Log.d("Fragment_D", "-------------D-------onPause()");

		super.onPause();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
	    Log.d("Fragment_D", "-------------D-------onStop()");

		super.onStop();
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
	    Log.d("Fragment_D", "-------------D-------onDestroy()");

		super.onDestroy();
	}

	/** 资源初始化*/
	private void initView() {
		stopwatch_record = (ListView) findViewById(R.id.lv_stopwatch_records);
		
		mytime = (TextView) findViewById(R.id.myTime);
		myCurrentTime = (TextView) findViewById(R.id.myCurrentTime);
		btnStartStop = (ImageButton) findViewById(R.id.btnStartStop);
		btnCountReset = (ImageButton) findViewById(R.id.btnCountReset);
		
		mytime.setText("00:00:00");
	}
	
	// Start/STOP事件监听器
	View.OnClickListener startStopListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (null == timer) {
				if (null == task) {
					task = new TimerTask() {
	
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (null == msg) {
								msg = new Message();
							} else {
								msg = Message.obtain();
							}
							msg.what = 1;
							handler.sendMessage(msg);
						}
						
					};
				}
				
				timer = new Timer(true);
				timer.schedule(task, 100, 100); // set timer duration
			}
			
			// start
			if (!IsRunningFlg) {
				IsRunningFlg = true;
				btnStartStop.setImageResource(R.drawable.stopwatch_stop);
				btnCountReset.setImageResource(R.drawable.stopwatch_count);
			} else { // pause
				try{
					IsRunningFlg = false;
					task.cancel();
					task = null;
					timer.cancel(); // Cancel timer
					timer.purge();
					timer = null;
					handler.removeMessages(msg.what);
					btnStartStop.setImageResource(R.drawable.stopwatch_start);
					btnCountReset.setImageResource(R.drawable.stopwatch_reset);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}			
	};

	// 计次/复位事件监听器
	View.OnClickListener countResetListener = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int totalSec = 0;
			int yushu = 0;
			totalSec = (int)(mlCount / 10);
			yushu = (int)(mlCount % 10);
			int min = (totalSec / 60);
			int sec = (totalSec % 60);
			
			// 设置计次后时间
			long mThisCount = mlCount - mCurrentCount;
			int current_totalSec = 0;
			int current_yushu = 0;
			
			current_totalSec = (int)(mThisCount/10);
			current_yushu = (int)(mThisCount%10);
			int current_min = (current_totalSec/60);
			int current_sec = (current_totalSec%60);
			
			mCurrentCount = mlCount;
			if(IsRunningFlg){
				// 计次
				AddItem(timeFlg+"",
						String.format("%1$02d:%2$02d:%3$d", current_min, current_sec, current_yushu),
						String.format("%1$02d:%2$02d:%3$d", min, sec, yushu));
				timeFlg++;				
			}else{
				// 复位
				listItems = new ArrayList<Map<String, String>>();
				simpleAdapter = new SimpleAdapter(StopWatchActivity.this, listItems,
						R.layout.fragment_stopwatch_listitem, 
						new String[] { "jici", "danwei" , "leiji"},
						new int[] { R.id.item_tv_jici, R.id.item_tv_danwei, R.id.item_tv_leiji});
				stopwatch_record.setAdapter(simpleAdapter);	
				
				mytime.setText("00:00:00");
				myCurrentTime.setText("00:00:00");
				
				if (null != timer) {
					task.cancel();
					task = null;
					timer.cancel(); // Cancel timer
					timer.purge();
					timer = null;
					handler.removeMessages(msg.what);
				}
				
				mlCount = 0;
				mCurrentCount = 0;
				timeFlg = 0;
				IsRunningFlg = false;
				btnCountReset.setImageResource(R.drawable.stopwatch_count);
			}
			
		}
	};
	
	/**
	 * 添加元素,更新listview
	 * @param v
	 */
	public void AddItem(String jici, String danwei, String leiji)
	{
		Map<String, String> listItem = new HashMap<String, String>();
		listItem.put("jici", jici);
		listItem.put("danwei", danwei);
		listItem.put("leiji", leiji);
		listItems.add(listItem);
		simpleAdapter = new SimpleAdapter(StopWatchActivity.this, listItems,
				R.layout.fragment_stopwatch_listitem, 
				new String[] { "jici", "danwei" , "leiji"},
				new int[] { R.id.item_tv_jici, R.id.item_tv_danwei, R.id.item_tv_leiji});
			
		// 为ListView设置Adapter
		stopwatch_record.setAdapter(simpleAdapter);	
	}

}


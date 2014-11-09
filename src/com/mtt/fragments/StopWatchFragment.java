package com.mtt.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.mtt.R;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

/** 
 * 秒表功能页面
 * @author Kerry Zhouyoqin
 * */
public class StopWatchFragment extends Fragment{

	public static final String TAG = "com.mtt.fragment.StopWatchFragment";

	/** Fragment View*/
	private View view;
	/** 当前秒数*/
	private long mlCount = 0;
	
	private TextView mytime;
	private ImageButton btnStartStop,btnCountReset;
	private TextView tv_jici_userdata1,tv_danwei_userdata1,tv_leiji_userdata1;
	private TextView tv_jici_userdata2,tv_danwei_userdata2,tv_leiji_userdata2;
	private TextView tv_jici_userdata3,tv_danwei_userdata3,tv_leiji_userdata3;
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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment_stopwatch, container, false);  
        
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
    				try{

    					mytime.setText(String.format("%1$02d:%2$02d:%3$d", min, sec, yushu));
    					
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
        return view;
	}

	/** 资源初始化*/
	private void initView() {
		stopwatch_record = (ListView) view.findViewById(R.id.lv_stopwatch_records);
		
		mytime = (TextView) view.findViewById(R.id.myTime);
		btnStartStop = (ImageButton) view.findViewById(R.id.btnStartStop);
		btnCountReset = (ImageButton) view.findViewById(R.id.btnCountReset);
		
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
			
			if(IsRunningFlg){
				AddItem(timeFlg+"",
						String.format("%1$02d:%2$02d:%3$d", min, sec, yushu),
						String.format("%1$02d:%2$02d:%3$d", min, sec, yushu));
				timeFlg++;				
			}else{
				listItems = new ArrayList<Map<String, String>>();
				simpleAdapter = new SimpleAdapter(getActivity(), listItems,
						R.layout.fragment_stopwatch_listitem, 
						new String[] { "jici", "danwei" , "leiji"},
						new int[] { R.id.item_tv_jici, R.id.item_tv_danwei, R.id.item_tv_leiji});
				stopwatch_record.setAdapter(simpleAdapter);	
				
				mytime.setText("00:00:00");
				
				if (null != timer) {
					task.cancel();
					task = null;
					timer.cancel(); // Cancel timer
					timer.purge();
					timer = null;
					handler.removeMessages(msg.what);
				}
				
				mlCount = 0;
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
		simpleAdapter = new SimpleAdapter(getActivity(), listItems,
				R.layout.fragment_stopwatch_listitem, 
				new String[] { "jici", "danwei" , "leiji"},
				new int[] { R.id.item_tv_jici, R.id.item_tv_danwei, R.id.item_tv_leiji});
			
		// 为ListView设置Adapter
		stopwatch_record.setAdapter(simpleAdapter);	
	}

}


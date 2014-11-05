package com.mtt.fragments;

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
import android.widget.ImageView;
import android.widget.TextView;

/** 
 * 秒表功能页面
 * @author Kerry
 * */
public class StopWatchFragment extends Fragment{

	public static final String TAG = "com.mtt.fragment.StopWatchFragment";

	private View view;
	private long mlCount = 0;
	private TextView tv_time1,tv_time2,tv_time3,tv_time4,tv_time5,tv_time6;
	private ImageView time_pause,time_stop,time_trash;
	private TextView mytime,time_start;
	private Timer timer = null;
	private TimerTask task = null;
	private Handler handler = null;
	private Message msg = null;
	private boolean IsRunningFlg = false;
	private int timeFlg = 0;
	private static final String MYTIMER_TAG = "MYTIMER_LOG"; 
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment_stopwatch, container, false);  
        
		initView();  // 初始化 
		time_pause.setVisibility(time_pause.INVISIBLE);
		time_stop.setVisibility(time_stop.INVISIBLE);
		
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
 
        time_start.setOnClickListener(startListener);
        time_pause.setOnClickListener(pausestartListener);
        time_stop.setOnClickListener(stopListener);
        time_trash.setOnClickListener(trashListener);
        return view;
	}

	/** 资源初始化*/
	private void initView() {
		// TODO Auto-generated method stub
		tv_time1 = (TextView) view.findViewById(R.id.time_1);
		tv_time2 = (TextView) view.findViewById(R.id.time_2);
		tv_time3 = (TextView) view.findViewById(R.id.time_3);
		tv_time4 = (TextView) view.findViewById(R.id.time_4);
		tv_time5 = (TextView) view.findViewById(R.id.time_5);
		tv_time6 = (TextView) view.findViewById(R.id.time_6);
		
		time_pause = (ImageView) view.findViewById(R.id.time_pause);
		time_stop = (ImageView) view.findViewById(R.id.time_stop);
		time_trash = (ImageView) view.findViewById(R.id.time_trash);
		
		mytime = (TextView) view.findViewById(R.id.mytime);
		time_start= (TextView) view.findViewById(R.id.time_start);
		
		mytime.setText("00:00:0");
		time_stop.setImageResource(R.drawable.time_stop);
	}
	
    // trash
    View.OnClickListener trashListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			tv_time1.setText("");
			tv_time2.setText("");
			tv_time3.setText("");
			tv_time4.setText("");
			tv_time5.setText("");
			tv_time6.setText("");
			
			timeFlg = 0;
		}
    };	

    // start
    View.OnClickListener startListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
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
			
			IsRunningFlg = true;
			time_start.setVisibility(time_start.INVISIBLE);
			time_pause.setVisibility(time_pause.VISIBLE);
			time_pause.setImageResource(R.drawable.time_pause);	
			time_stop.setVisibility(time_stop.VISIBLE);
		}
    };
    
    // pause
    View.OnClickListener pausestartListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			
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
				time_pause.setImageResource(R.drawable.time_pause);
			} else { // pause
				try{
					IsRunningFlg = false;
					task.cancel();
					task = null;
					timer.cancel(); // Cancel timer
					timer.purge();
					timer = null;
					handler.removeMessages(msg.what);
					time_pause.setImageResource(R.drawable.time_start);
				} catch(Exception e) {
					e.printStackTrace();
				}
			}
		}
    };

    // stop
    View.OnClickListener stopListener = new View.OnClickListener() {

		@Override
		public void onClick(View v) {
			int totalSec = 0;
			int yushu = 0;
			totalSec = (int)(mlCount / 10);
			yushu = (int)(mlCount % 10);
			int min = (totalSec / 60);
			int sec = (totalSec % 60);
			
			switch(timeFlg){
			case 0:
				tv_time1.setText(String.format("%1$02d:%2$02d:%3$d", min, sec, yushu));
				timeFlg ++;
				break;
			case 1:
				tv_time2.setText(String.format("%1$02d:%2$02d:%3$d", min, sec, yushu));
				timeFlg ++;
				break;
			case 2:
				tv_time3.setText(String.format("%1$02d:%2$02d:%3$d", min, sec, yushu));
				timeFlg ++;
				break;
			case 3:
				tv_time4.setText(String.format("%1$02d:%2$02d:%3$d", min, sec, yushu));
				timeFlg ++;
				break;
			case 4:
				tv_time5.setText(String.format("%1$02d:%2$02d:%3$d", min, sec, yushu));
				timeFlg ++;
				break;
			case 5:
				tv_time6.setText(String.format("%1$02d:%2$02d:%3$d", min, sec, yushu));
				break;
			}
			
			if (null != timer) {
				task.cancel();
				task = null;
				timer.cancel(); // Cancel timer
				timer.purge();
				timer = null;
				handler.removeMessages(msg.what);
			}
			
			mlCount = 0;
			IsRunningFlg = false;
			time_start.setVisibility(time_start.VISIBLE);
			time_pause.setVisibility(time_pause.INVISIBLE);
			time_stop.setVisibility(time_stop.INVISIBLE);
			
			mytime.setText("00:00:0");
		}
    };
}


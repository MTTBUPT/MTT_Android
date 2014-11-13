package com.mtt.fragments;

import java.util.List;

import com.mtt.R;
import com.mtt.service.MusicService;
import com.mtt.service.MusicService.MusicBinder;
import com.mtt.util.FormatHelper;
import com.mtt.util.MusicLoader;
import com.mtt.util.MusicLoader.MusicInfo;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/** 
 * 音乐功能页面
 * @author Kerry
 * */
public class MusicFragment extends Fragment implements OnClickListener{

	private View view;
	public static final String TAG = "com.mtt.fragment.MusicFragment";

	/** 当前歌曲名*/
	private TextView tvCurrentMusic; 
	/** 当前歌手*/
	private TextView tvCurrentArtist;  
	/** 当前播放进度*/
	private TextView tvCurrentTime;  
	/** 歌曲时间*/
	private TextView tvDurationTime;  
	/** 往后*/
	private ImageButton btToPrevious; 
	/** 往前*/
	private ImageButton btToNext; 
	/** 开始/暂停*/
	private ImageButton btStartStop; 
	/** 音乐列表*/
	private List<MusicInfo> musicList;
	
	/** 正在播放的音乐*/
	private int currentMusic = 0;
	/** 音乐播放的进度*/
	private int currentPosition; 
	/** 广播监听器*/
	private ProgressReceiver progressReceiver;
	private MusicBinder musicBinder;	

	private ServiceConnection serviceConnection = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName name) {
			
		}
		
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			musicBinder = (MusicBinder) service;	// 在service绑定的时候获取Binder		
		}
	};
	
	/** 绑定service*/
	private void connectToMusicService(){		
		Intent intent = new Intent(getActivity(), MusicService.class);				
		getActivity().bindService(intent, serviceConnection, getActivity().BIND_AUTO_CREATE);				
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        view = inflater.inflate(R.layout.fragment_music, container, false);  
		// 实例化一个MusicLoader，同时进行查询音乐数据
		MusicLoader musicLoader = MusicLoader.instance(getActivity().getContentResolver());
		// 获取音乐信息列表
		musicList = musicLoader.getMusicList();
		// 绑定service
		connectToMusicService();
		// 组件初始化
		initComponents();	
		return view;
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(musicBinder != null){
			getActivity().unbindService(serviceConnection); // 关闭service的绑定
		}
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// 注销progressReceiver
		getActivity().unregisterReceiver(progressReceiver);
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// 注册progressReceiver
		registerReceiver();
	}
	
	/** 初始化组件*/
	private void initComponents() {
		// TODO Auto-generated method stub
		tvCurrentMusic = (TextView) view.findViewById(R.id.music_title);
		tvCurrentArtist = (TextView) view.findViewById(R.id.music_artist);
		tvCurrentTime = (TextView) view.findViewById(R.id.tvCurrentTime);
		tvDurationTime = (TextView) view.findViewById(R.id.tvDurationTime);
		
		tvCurrentTime.setText(FormatHelper.formatDuration(currentPosition));
		tvDurationTime.setText(FormatHelper.formatDuration(musicList.get(currentMusic).getDuration()));
		tvCurrentMusic.setText(musicList.get(currentMusic).getTitle());
		tvCurrentArtist.setText(musicList.get(currentMusic).getArtist());
		
		btToPrevious = (ImageButton) view.findViewById(R.id.music_toPrevious);
		btToPrevious.setOnClickListener(this);
		
		btToNext = (ImageButton) view.findViewById(R.id.music_toNext);
		btToNext.setOnClickListener(this);
		
		btStartStop = (ImageButton) view.findViewById(R.id.music_btStartStop);
		btStartStop.setOnClickListener(this);
	}

	/** 注册广播，并设置广播接收的intent*/
	private void registerReceiver(){
		progressReceiver = new ProgressReceiver();	
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(MusicService.ACTION_UPDATE_PROGRESS);
		intentFilter.addAction(MusicService.ACTION_UPDATE_CURRENT_MUSIC);
		getActivity().registerReceiver(progressReceiver, intentFilter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.music_btStartStop:		
			play(currentMusic,R.id.music_btStartStop);
			break;	
		case R.id.music_toPrevious:
			musicBinder.toPrevious();
			break;		
		case R.id.music_toNext:						
			musicBinder.toNext();
			break;
		}	
	}

	/** 播放*/
	private void play(int position, int resId){		
		if(musicBinder.isPlaying()){
			musicBinder.stopPlay();
			btStartStop.setImageResource(R.drawable.music_start);
		}else{
			musicBinder.startPlay(position,currentPosition);
			btStartStop.setImageResource(R.drawable.music_stop);
		}
	}
	
	class ProgressReceiver extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(MusicService.ACTION_UPDATE_CURRENT_MUSIC.equals(action)){
				//Retrive the current music and get the title to show on top of the screen.
				// 显示当前的音乐名在屏幕上
				currentMusic = intent.getIntExtra(MusicService.ACTION_UPDATE_CURRENT_MUSIC, 0);				
				tvCurrentMusic.setText(musicList.get(currentMusic).getTitle());
				tvCurrentArtist.setText(musicList.get(currentMusic).getArtist());
				tvDurationTime.setText(FormatHelper.formatDuration(musicList.get(currentMusic).getDuration()));
				btStartStop.setImageResource(R.drawable.music_stop);
			}else if (MusicService.ACTION_UPDATE_PROGRESS.equals(action)) {
				// 更新进度
				int progress = intent.getIntExtra(MusicService.ACTION_UPDATE_PROGRESS, 0);
				if(progress > 0){
					currentPosition = progress; // Remember the current position
					tvCurrentTime.setText(FormatHelper.formatDuration(progress));
				}
			}
		}
	}
}

package com.mtt.service;

import java.util.List;

import com.mtt.util.*;
import com.mtt.util.MusicLoader.MusicInfo;
import com.mtt.fragments.*;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

public class MusicService extends Service{
	private static final String TAG = "com.mymusic.service.MUSIC_SERVICE";
	/** 音乐播放器*/
	private MediaPlayer mediaPlayer;
	
	/** 是否在播放*/
	private boolean isPlaying = false;
	
	/** 音乐信息列表*/
	private List<MusicInfo> musicList;
	
	/** 与MusicService 绑定的*/
	private Binder musicBinder = new MusicBinder();
	
	/** 当前音乐位置*/
	private int currentMusic;
	/** 当前播放进度*/
	private int currentPosition;

	private static final int updateCurrentMusic = 1;
	private static final int updateProgress = 2;
	
	public static final String ACTION_UPDATE_PROGRESS = "com.example.nature.UPDATE_PROGRESS";
	public static final String ACTION_UPDATE_CURRENT_MUSIC = "com.mymusic.service.UPDATE_CURRENT_MUSIC";

	private Notification notification; 

	private Handler handler = new Handler(){
		
		public void handleMessage(Message msg){
			switch(msg.what){
			case updateCurrentMusic:
				toUpdateCurrentMusic();
				break;
			case updateProgress:				
				toUpdateProgress();
				break;
			}
		}
	};
	
	/** 更新音乐播放进度*/
	private void toUpdateProgress(){
		if(mediaPlayer != null && isPlaying){					
			int progress = mediaPlayer.getCurrentPosition();					
			Intent intent = new Intent();
			intent.setAction(ACTION_UPDATE_PROGRESS);
			intent.putExtra(ACTION_UPDATE_PROGRESS,progress);
			sendBroadcast(intent);
			if(isPlaying){
			// 继续向handler发送message更新进度
			handler.sendEmptyMessageDelayed(updateProgress, 1000);			
			}		
		}
	}
	
	/** 更新当前音乐*/
	private void toUpdateCurrentMusic(){
		Intent intent = new Intent();
		intent.setAction(ACTION_UPDATE_CURRENT_MUSIC);
		intent.putExtra(ACTION_UPDATE_CURRENT_MUSIC,currentMusic);
		sendBroadcast(intent);				
	}
	
	@Override
	public void onCreate(){
		// 初始化MediaPlayer
		initMediaPlayer();
		musicList = MusicLoader.instance(getContentResolver()).getMusicList();
		Log.v(TAG, "OnCreate");   
		super.onCreate();
		
		Intent intent = new Intent(this, MusicFragment.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
		
		PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
		notification = new Notification.Builder(this)
					.setTicker("Music")
					.setContentTitle("Playing")
					.setContentText(musicList.get(currentMusic).getTitle())
					.setContentIntent(pendingIntent)
					.getNotification();		
		notification.flags |= Notification.FLAG_NO_CLEAR;
		  
		startForeground(1, notification);
	}	
	
	@Override
	public void onDestroy(){
		if(mediaPlayer != null){
			// 释放资源
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}
	
	/**
	 * initialize the MediaPlayer
	 */
	private void initMediaPlayer(){
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);		
		mediaPlayer.setOnPreparedListener(new OnPreparedListener() {				
			@Override
			public void onPrepared(MediaPlayer mp) {				
				mediaPlayer.start();
				mediaPlayer.seekTo(currentPosition);
				Log.v(TAG, "[OnPreparedListener] Start at " + currentMusic + ", currentPosition : " + currentPosition);
			}
		});
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {			
			@Override
			public void onCompletion(MediaPlayer mp) {
				if(isPlaying){
					Log.v(TAG, "[OnCompletionListener] On Completion at " + currentMusic);
						
					if(currentMusic < musicList.size() - 1){						
							playNext();
					}

					Log.v(TAG, "[OnCompletionListener] Going to play at " + currentMusic);
				}
			}
		});
	}
	
	/** 设置当前播放的音乐，同时更新界面*/
	private void setCurrentMusic(int pCurrentMusic){
		currentMusic = pCurrentMusic;
		handler.sendEmptyMessage(updateCurrentMusic);
	}

	/** 播放音乐*/
	private void play(int currentMusic, int pCurrentPosition) {
		currentPosition = pCurrentPosition;
		setCurrentMusic(currentMusic);
		
		/*Resets the MediaPlayer to its uninitialized state. 
		After calling this method, you will have to initialize it again 
		by setting the data source and calling prepare().*/
		mediaPlayer.reset();
		try {
			mediaPlayer.setDataSource(musicList.get(currentMusic).getUrl());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.v(TAG, "[Play] Start Preparing at " + currentMusic);
		mediaPlayer.prepareAsync();
		handler.sendEmptyMessage(updateProgress);

		isPlaying = true;
	}
	
	/** 停止播放*/
	private void stop(){
		mediaPlayer.stop();
		isPlaying = false;
	}
	
	/** 播放下一首歌*/
	private void playNext(){
		if(currentMusic + 1 == musicList.size()){
			Toast.makeText(this, "No more song.", Toast.LENGTH_SHORT).show();
		}else{
			play(currentMusic + 1, 0);
		}
	}
	
	/** 播放前一首歌*/
	private void playPrevious(){		
		if(currentMusic - 1 < 0){
			Toast.makeText(this, "No previous song.", Toast.LENGTH_SHORT).show();
		}else{
			play(currentMusic - 1, 0);
		}
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return musicBinder;
	}	
	
	/** activity与service交互的桥梁*/
	public class MusicBinder extends Binder{
		
		public void startPlay(int currentMusic, int currentPosition){
			play(currentMusic,currentPosition);
		}
		
		public void stopPlay(){
			stop();
		}
		
		public void toNext(){
			playNext();
		}
		
		public void toPrevious(){
			playPrevious();
		}
		
		/**
		 * The service is playing the music
		 * @return
		 */
		public boolean isPlaying(){
			return isPlaying;
		}
		
		/**
		 * Notify Activities to update the current music and duration when current activity changes.
		 */
		public void notifyActivity(){
			toUpdateCurrentMusic();
		}
		
	}
}

package jp.ac.anan_nct.TabletInterphone.Audio;

import java.util.Timer;
import java.util.TimerTask;

import jp.ac.anan_nct.TabletInterphone.Const;
import jp.ac.anan_nct.TabletInterphone.SharedVariable;
import jp.ac.anan_nct.TabletInterphone.Communication.WifiSocket;
import jp.ac.anan_nct.TabletInterphone.Serializable.AudioSerializable;
import jp.ac.anan_nct.TabletInterphone.Utility.Util;
import android.media.AudioFormat;
import android.media.MediaRecorder.AudioSource;
import android.util.Log;

public class CustomAudioRecord extends android.media.AudioRecord{
	private static final int SAMPLE_RATE = 8000;
	private static final int IN_CHANNEL = AudioFormat.CHANNEL_IN_MONO;
	private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;
	
	private boolean isRecording = false;
	
	private short limitvolume = 10000;
	
	private boolean ischecking = true;
	
	private Timer volumeTimer;					//タイマー用
	private volumeTimerTask volumeTimerTask;		//タイマタスククラス
	
	public CustomAudioRecord(){
		super(AudioSource.DEFAULT, SAMPLE_RATE, IN_CHANNEL, AUDIO_FORMAT, SAMPLE_RATE);
	}
	
	private int getMinBufferSize(){
		return CustomAudioRecord.getMinBufferSize(SAMPLE_RATE, IN_CHANNEL, AUDIO_FORMAT);
	}
	
	private boolean soundChecker(short buffer[], int buffersize){
		short max = 0;
		for(int i = 0; i < buffersize; i++){
			if(max < buffer[i]){
				max = buffer[i];
			}
		}
		//long avg = sum / (long)buffersize;
		if(max > limitvolume){
			Log.d("volume","volumeHigh" + max);
			return true;
		}
		Log.d("volume","volumeLow" + max);
		return false;
	}

	public void startRecording(final WifiSocket ws){
		try{
			super.startRecording();
		}
		catch(IllegalStateException e){
			return;
		}
		
		isRecording = true;
		
		new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					recordRunnableMethod(ws);
				}
				catch(Exception e){}
			}
		}).start();
	}
	
	//////////10/27追加///////////////////////////////////////
	public void startRecording(final WifiSocket ws, final boolean opt){
		try{
			super.startRecording();
		}
		catch(IllegalStateException e){
			return;
		}
		
		isRecording = true;
		
		new Thread(new Runnable(){
			@Override
			public void run(){
				try{
					recordRunnableMethod(ws, opt);
				}
				catch(Exception e){}
			}
		}).start();
	}
	
	private void recordRunnableMethod(WifiSocket ws, boolean opt){
		byte buf[] = new byte[getMinBufferSize()];
		short shortbuf[] = new short[getMinBufferSize()];
		AudioSerializable audioSerializable = new AudioSerializable(buf);

		while(isRecording) {
			// 録音データ読み込み
			if(ischecking & opt){
				super.read(shortbuf, 0, shortbuf.length);
				if(!(soundChecker(shortbuf,shortbuf.length))){
					try{
						super.stop();
						super.startRecording();
					}
					catch(IllegalStateException e){
						
					}
					continue;
				}
				else{
					//0511追加
					volumeTimer = new Timer();
					//タスククラスインスタンス生成
					volumeTimerTask = new volumeTimerTask();	
					//タイマースケジュール設定＆開始
					volumeTimer.schedule(volumeTimerTask, 10000,10000);
					ischecking = false;
					recordRunnableMethod(ws, opt);
					break;
				}
			}
			super.read(buf, 0, buf.length);
			ws.writeObject(audioSerializable);
		}
	}
	///////////////////////////////////////////////////
	
	private void recordRunnableMethod(WifiSocket ws){
		byte buf[] = new byte[getMinBufferSize()];
		AudioSerializable audioSerializable = new AudioSerializable(buf);

		while(isRecording) {
			// 録音データ読み込み
			super.read(buf, 0, buf.length);
			ws.writeObject(audioSerializable);
		}
	}
	
	@Override
	public void stop(){
		isRecording = false;
		
		try{
			super.stop();
		}
		catch(IllegalStateException e){}
	}
	
	protected class volumeTimerTask extends TimerTask {
		@Override
		public void run() {
			ischecking = true;
			volumeTimer.cancel();
		}
	}
}

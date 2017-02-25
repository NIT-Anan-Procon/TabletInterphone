package jp.ac.anan_nct.TabletInterphone.Service;

import java.util.Timer;
import java.util.TimerTask;

import jp.ac.anan_nct.TabletInterphone.Const;
import jp.ac.anan_nct.TabletInterphone.Activity.ConnectionErrorDialogActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.Inside.InsideConnectRecoverActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.Inside.VisiterCheckActivity;
import jp.ac.anan_nct.TabletInterphone.Communication.BluetoothConnection;
import jp.ac.anan_nct.TabletInterphone.Communication.WifiSocket;
import jp.ac.anan_nct.TabletInterphone.Serializable.AudioSerializable;
import jp.ac.anan_nct.TabletInterphone.Serializable.MessageSerializable;
import jp.ac.anan_nct.TabletInterphone.Serializable.WriteSerializable;
import jp.ac.anan_nct.TabletInterphone.Utility.Util;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class InsideService extends BaseService{
	private BluetoothConnection bluetoothConnection;
	private WifiSocket wifiSocket;
	
	private Window currentWindow;
	
	private boolean endFlag = false;
	//private long[] vibratePattern = {1000,2000,1000,3000};
	private Timer VCTimer;					//タイマー用
	private VCTimerTask vcTimerTask;		//タイマタスククラス
	private int count = 0;
	
	private Timer wifiTimer;					//タイマー用
	private wifiTimerTask wifiTimerTask;		//タイマタスククラス
	
	private int add = 0;

	
	private final Runnable bluetoothReceiveRunnable = new Runnable() {
		@Override
		public void run() {
			try{
			startTIActivityUsingHandler(VisiterCheckActivity.class);
				//bluetoothReceive();
			}
			catch(Exception e){}
				wifiTimerStart();
				//endCheck();
		}
	};
	
	private final Runnable wifiReceiveRunnable = new Runnable() {
		@Override
		public void run() {
			try{
				wifiReceive();
				//wifiSocket.writeObject(Const.BLUETOOTH_VISIT);
				//Log.d("Ins","El03" + String.valueOf(wifiSocket.writeObject(Const.BLUETOOTH_VISIT)));
				//Log.d("Ins","El04" + String.valueOf(wifiSocket.readObject()));
				//startTIActivityUsingHandler(VisiterCheckActivity.class);
				//0429追加
				//sharedVariable.visiterCheckActivity.setBusiness((Integer)wifiSocket.readObject());
				//
			}
			catch(Exception e){}
			endCheck();
		}
	};
	
	private synchronized void endCheck(){
		
		if(!endFlag){
			endFlag = true;
			
			stopService();
			
			//startTIActivityUsingHandler(ConnectionErrorDialogActivity.class);
			startTIActivityUsingHandler(InsideConnectRecoverActivity.class);

		}
	}
	
	private void wifiTimerStart(){
		//0511追加
		wifiTimer = new Timer();
		//タスククラスインスタンス生成
		wifiTimerTask = new wifiTimerTask();	
		//タイマースケジュール設定＆開始
		wifiTimer.schedule(wifiTimerTask, 60000,60000);
	}
	
	private void startRingtone(){
		
		count = 0;
		
		sharedVariable.ringtone.play();
		
		//0511追加
		VCTimer = new Timer();
		//タスククラスインスタンス生成
		vcTimerTask = new VCTimerTask();	
		//タイマースケジュール設定＆開始
		VCTimer.schedule(vcTimerTask, 10000,10000);
		
	}
	/*
	private void logaudio(byte[] buf){
		Log.d("audio","audiofirst");
		for(int i = 0; i < buf.length; i++){
			Log.d("audio","audio" + buf[i]);
		}
		Log.d("audio","audioend");
	}
	*/
	
	private void wifiReceive(){
		WifiSocket wifiSocket = this.wifiSocket;
		Object obj;
		while((obj = wifiSocket.readObject()) != null){
			if(obj instanceof byte[]){
				final ImageView cameraView = sharedVariable.cameraView;
				
				if(cameraView != null){
					final byte[] bytes = (byte[])obj;
					handler.post(new Runnable(){
						@Override
						public void run() {
							cameraView.setImageBitmap(Util.bitmapDecode(bytes));
						}
					});
					wifiSocket.writeObject(Const.RECEIVE_IMAGE + add++);
					if(add >= 100){
						add = 0;
					}
				}
			}
			else if(obj instanceof AudioSerializable){
				if(audioTrack != null){
					//logaudio(((AudioSerializable)obj).getAudio());
					audioTrack.write(((AudioSerializable)obj).getAudio());
				}
			}
			//0531追加
			else if(obj instanceof WriteSerializable){
				//Wake();
				final WriteSerializable i = (WriteSerializable)obj;
				final Context c = this;
				handler.post(new Runnable(){
					@Override
					public void run(){
						if(sharedVariable.getOtherFrag() == 1){
							sharedVariable.setWserialOther(i);
						}
						else if(sharedVariable.getCameraFrag() == 1){
							try{
								sharedVariable.visiterCheckActivity.setImage(i);
							}
							catch(Exception e){
								LocalBroadcastManager.getInstance(c).sendBroadcast(new Intent(Const.INSIDE_VISITERCHECK_ACTVITY_OPEN));
								startRingtone();
							}
							
							if(sharedVariable.getCameraFrag() == 1){
								sharedVariable.setWserial(i);
							}
							sharedVariable.resetCameraFrag();
						}
					}
				});
			}
			//
			else if(obj instanceof Integer){
				final int i = (Integer)obj;
				final Context c = this;
				handler.post(new Runnable(){
					@Override
					public void run(){
						switch(i){
						case Const.BUSINESS_PUSH_BELL:
								LocalBroadcastManager.getInstance(c).sendBroadcast(new Intent(Const.INSIDE_VISITERCHECK_ACTVITY_OPEN));
								startRingtone();
								break;
						case Const.CAMERA_FRAG_SET:
								sharedVariable.setCameraFrag();
								break;
						case Const.OTHER_FRAG_SET:
								sharedVariable.setOtherFrag();
								break;
						case Const.INTERACTION_CAMERA_ON:
								try{
									sharedVariable.interval = 0;
									LocalBroadcastManager.getInstance(c).sendBroadcast(new Intent(Const.INSIDE_CAMERA_CHANGE));
								}
								catch(Exception e){
									
								}
								break;
						case Const.INTERACTION_CAMERA_IDLE:
								try{
								sharedVariable.interval = 1;
								LocalBroadcastManager.getInstance(c).sendBroadcast(new Intent(Const.INSIDE_CAMERA_CHANGE));
								}
								catch(Exception e){
									
								}
								break;
						case Const.INTERACTION_CAMERA_OFF:
								try{
									sharedVariable.interval = -1;
								LocalBroadcastManager.getInstance(c).sendBroadcast(new Intent(Const.INSIDE_CAMERA_CHANGE));
								}
								catch(Exception e){
									
								}
								break;
						default:
							try{
								if(i == Const.BUSINESS_PRE_SELECT && sharedVariable.writeStatus){
									break;
								}
								sharedVariable.visiterCheckActivity.setBusiness(i);
							}
							catch(Exception e){
								LocalBroadcastManager.getInstance(c).sendBroadcast(new Intent(Const.INSIDE_VISITERCHECK_ACTVITY_OPEN));
								startRingtone();
							}
								break;
						}
					}
				});
			}
			else if(obj instanceof MessageSerializable){
				MessageSerializable messageSerializable = (MessageSerializable)obj;
				final String message = messageSerializable.getMessage();
				final Context c = this;
				switch(messageSerializable.getMessageType()){
				case OTHER:
					handler.post(new Runnable(){
						@Override
						public void run(){
							try{
								if(sharedVariable.getOtherFrag() == 1){
									sharedVariable.resetOtherFrag();
									sharedVariable.visiterCheckActivity.setOtherBusinessAddImage(message);
									
								}
								else{
									sharedVariable.visiterCheckActivity.setOtherBusiness(message);
								}
							}
							catch(Exception e){
								LocalBroadcastManager.getInstance(c).sendBroadcast(new Intent(Const.INSIDE_VISITERCHECK_ACTVITY_OPEN));
								startRingtone();
							}
						}
					});
					break;
				case MORE:
					handler.post(new Runnable(){
						@Override
						public void run(){
							sharedVariable.visiterCheckActivity.setMoreBusiness(message);
						}
					});
					break;
				default:
					break;
				}
			}
		}
	}
	
	
	
	@Override
	protected void stop(){
		endFlag = true;
		// 4/25
		//bluetoothConnection.close();
		sharedVariable.service.endTrackAndRecord();
		wifiTimer.cancel();
		wifiSocket.stopConnect();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		this.bluetoothConnection = sharedVariable.bluetoothConnection;
		this.wifiSocket = sharedVariable.wifiSocket;
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("Wi-FiDisConnection");
		
		wifiSocket.writeObject(Const.BLUETOOTH_VISIT);
		new Thread(bluetoothReceiveRunnable).start();
		
		new Thread(wifiReceiveRunnable).start();
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	protected boolean isInside(){
		return true;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	protected class VCTimerTask extends TimerTask {
		@Override
		public void run() {
				VCTimer.cancel();
				if(sharedVariable.ringtone.isPlaying()){
					sharedVariable.ringtone.stop();
				}
		}
	}
	
	protected class wifiTimerTask extends TimerTask {
		@Override
		public void run() {
			sharedVariable.wifiSocket.writeObject(Const.INTERACTION_CONNECTION_CHECK);
		}
	}
	
	/*
	public class TiInsideWakefulBroadcastReceiver extends WakefulBroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent){
			if(intent.getAction().equals("Wi-FiDisConnection")){
				try{
					sharedVariable.visiterCheckActivity.setOtherBusiness("通信失敗");
				}
				catch(Exception e){
				}
				finally{
					Log.d("hahaha","hahaha06");
					WakefulBroadcastReceiver.completeWakefulIntent(intent);
				}
			}
		}
	};
	*/
	
}

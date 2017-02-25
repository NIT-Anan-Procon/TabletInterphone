package jp.ac.anan_nct.TabletInterphone.Service;

import jp.ac.anan_nct.TabletInterphone.Const;
import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.SharedVariable;
import jp.ac.anan_nct.TabletInterphone.Activity.ServiceCancelDialogActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.Inside.InsideConnectRecoverActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.Outside.OutsideConnectRecoverActivity;
import jp.ac.anan_nct.TabletInterphone.Audio.CustomAudioRecord;
import jp.ac.anan_nct.TabletInterphone.Audio.CustomAudioTrack;
import jp.ac.anan_nct.TabletInterphone.Utility.Util;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.widget.EditText;

public abstract class BaseService extends Service{
	protected static final Handler handler = new Handler();
	protected SharedVariable sharedVariable;
	
	protected EditText receiveEditText;
	
	protected CustomAudioTrack audioTrack;
	protected CustomAudioRecord audioRecord;
	
	public void setReceiveEditText(EditText receiveEditText){
		this.receiveEditText = receiveEditText;
	}
	
	public void startTrackAndRecord(){
		if(audioTrack == null){
			audioTrack = new CustomAudioTrack();
			audioTrack.play();
		
			audioRecord = new CustomAudioRecord();
			//audioRecord.startRecording(sharedVariable.wifiSocket);
			//10/24追加
			audioRecord.startRecording(sharedVariable.wifiSocket, sharedVariable.isOutside);
		}
	}
	//////////////////////////////////////////////////////////////////////////
	//9月28日追加
	//startTrackAndRecord()にオプションとして一方通行を設けたもの
	//
	//////////////////////////////////////////////////////////////////////////
	public void startTrackAndRecord(int Opt){
		if(audioTrack == null){
				audioTrack = new CustomAudioTrack();
				audioTrack.play();
				
			
				audioRecord = new CustomAudioRecord();
			if(Opt == Const.VOICE_CHAT_DUAL || Opt == Const.VOICE_CHAT_SPEAK_ONLY){
				audioRecord.startRecording(sharedVariable.wifiSocket, sharedVariable.isOutside);
			}
		}
	}
	/*
	public boolean isTrackAndRecord(){
		if(audioTrack == null){
			return false;
		}
		else{
			return true;
		}
	}
	*/
	
	//////////////////////////////////////////////////////////////////////////
	public void endTrackAndRecord(){
		if(audioTrack != null){
			//audioTrack.stop();
			audioTrack.release(); //stop()も自動で呼ばれる
			audioTrack = null;
			
			//audioRecord.stop();
			audioRecord.release();
			audioRecord = null;
		}
	}
	
	protected void startTIActivityUsingHandler(final Class<?> cls){
		startTIActivityUsingHandler(new Intent(this, cls));
	}
	
	protected void startTIActivityUsingHandler(final Intent intent){
		handler.post(new Runnable(){
			@Override
			public void run(){
				startTIActivity(intent);
			}
		});
	}
	
	protected void startActivity(Class<?> cls){
		Intent intent = new Intent(this, cls);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	
	protected void startTIActivity(Class<?> cls){
		startTIActivity(new Intent(this, cls));
	}
	
	protected void startTIActivity(Intent intent){
		sharedVariable.startTIActivity(intent);
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		startNotification();
		this.sharedVariable = Util.getSharedVariable(this);
		this.sharedVariable.service = this;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId){
		super.onStartCommand(intent, flags, startId);

		return START_STICKY;
	}
	
	protected abstract boolean isInside();
	
	protected abstract void stop();
	
	public void stopService(){
		stop();
		sharedVariable.textToSpeechShutdown();
		stopSelf();
	}
	
	////////////////////////////////////////
	//10/24追加　オプション付きstopService()
	////////////////////////////////////////
	public void stopService(int opt){
		switch(opt){
			case 0:
				stop();
				sharedVariable.textToSpeechShutdown();
				stopSelf();
				if(sharedVariable.isOutside){
					startActivity(OutsideConnectRecoverActivity.class);
				}
				if(!sharedVariable.isOutside){
					startTIActivityUsingHandler(InsideConnectRecoverActivity.class);
				}
				
				break;
			case 666:
				stopService();
				sharedVariable.currentTIActivity.finish();
				sharedVariable.currentTIActivity.moveTaskToBack(true);
				break;
			default:
				stopService();
				break;
		}
	}
	//////////////////////////////////////
	
	@Override
	public void onDestroy(){
		this.sharedVariable.service = null;

		super.onDestroy();
	}
	
	/***************************
	 * 通知アイコン表示
	 ***************************/
	private void startNotification(){
		Intent intent = new Intent(this, ServiceCancelDialogActivity.class);
		
		// NotificationBuilderを作成
		Notification.Builder builder = new Notification.Builder(this)
			.setTicker("Tablet Interphone") // 開始時テキスト
			.setContentTitle("Tablet Interphone") // タイトル
			.setContentText(isInside() ? "Mode : Inside" : "Mode : Outside") // サブタイトル
			.setSmallIcon(R.drawable.ic_launcher) // 小さいアイコン
			.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher)) // 大きいアイコン
			.setContentIntent(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)) // クリックで起動
			.setAutoCancel(false); // タップで消えない
		
		startForeground(1027, Util.getNotification(builder)); // バックグラウンドで動作
	}
}

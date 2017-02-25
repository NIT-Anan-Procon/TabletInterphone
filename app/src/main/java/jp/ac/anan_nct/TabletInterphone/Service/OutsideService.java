package jp.ac.anan_nct.TabletInterphone.Service;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.LinearLayout;

import jp.ac.anan_nct.TabletInterphone.Activity.Outside.GetOutActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.Outside.OtherActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.Outside.OutsideConnectRecoverActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.Outside.OutsideShowNameActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.Outside.SelectBusinessActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.Outside.ShowWriteActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.Outside.WaitActivity;
import jp.ac.anan_nct.TabletInterphone.CameraControl;
import jp.ac.anan_nct.TabletInterphone.Communication.WifiSocket;
import jp.ac.anan_nct.TabletInterphone.Const;
import jp.ac.anan_nct.TabletInterphone.Serializable.AudioSerializable;
import jp.ac.anan_nct.TabletInterphone.Serializable.WriteSerializable;
import jp.ac.anan_nct.TabletInterphone.Utility.Util;

public class OutsideService extends BaseService{
	private WindowManager wm;
	private SurfaceView surfaceView;
	private CameraControl camera;
	private WifiSocket wifiSocket;
	
	private boolean endFlag = false;
	
	private int num = 0;
	
	private int sleeptime = 1000;
	private boolean counter = true;
	
	private int sendImage = 0x0FF000;
	private int difference = 0;
	
	private final Runnable wifiReceiveRunnable = new Runnable() {
		@Override
		public void run() {
			try{
				wifiReceive();
				//4/28追加
				//startActivity(SelectBusinessActivity.class);
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
			startActivity(OutsideConnectRecoverActivity.class);
		}
	}
	
	private void wifiReceive(){
		WifiSocket wifiSocket = this.wifiSocket;
		Object obj;
		
		while((obj = wifiSocket.readObject()) != null){
			if(obj instanceof AudioSerializable){
				if(audioTrack != null){
					audioTrack.write(((AudioSerializable)obj).getAudio());
				}
			}
			else if(obj instanceof WriteSerializable){
				Intent intent = new Intent(this, ShowWriteActivity.class);
				intent.putExtra("WriteImage", (WriteSerializable)obj);
				startTIActivity(intent);
			}
			else if(obj instanceof Integer){
				if((Integer)obj >= Const.RECEIVE_IMAGE){
					difference = sendImage - ((Integer)obj);
					if(difference < 0){
						difference += 100;
					}
					if(difference > 10){
						sharedVariable.interval = -10;
					}
					if((difference < 5) && (sharedVariable.interval == -10)){
						sharedVariable.interval = 0;
					}
				}
				
				switch((Integer)obj){
				case Const.BLUETOOTH_VISIT:
					new Thread(new Runnable() {
						public void run() {
							startCameraCapture();
						}
					}).start();
					startTIActivity(SelectBusinessActivity.class);
					//startTIActivity(WaitVisiterActivity.class);
					break;
				case Const.INTERACTION_WAIT:
					startTIActivity(WaitActivity.class);
					break;
				case Const.INTERACTION_GET_OUT:
					startTIActivity(GetOutActivity.class);
					break;
				/*
				case Const.INTERACTION_PHONE_CALL:
					Util.getSharedVariable(this).service.startTrackAndRecord();
					//startActivity(OutsidePhoneCallActivity.class);
					break;
				*/
				case Const.VOICE_CHAT_DUAL:
					Util.getSharedVariable(this).phoneCallStatus(Const.VOICE_CHAT_DUAL);
					Util.getSharedVariable(this).service.startTrackAndRecord();
					//startActivity(OutsidePhoneCallActivity.class);
					break;
				case Const.VOICE_CHAT_SPEAK_ONLY:
					Util.getSharedVariable(this).phoneCallStatus(Const.VOICE_CHAT_SPEAK_ONLY);
					Util.getSharedVariable(this).service.startTrackAndRecord(Const.VOICE_CHAT_SPEAK_ONLY);
					//startActivity(OutsidePhoneCallActivity.class);
					break;
				case Const.VOICE_CHAT_LISTEN_ONLY:
					Util.getSharedVariable(this).phoneCallStatus(Const.VOICE_CHAT_LISTEN_ONLY);
					Util.getSharedVariable(this).service.startTrackAndRecord(Const.VOICE_CHAT_LISTEN_ONLY);
					//startActivity(OutsidePhoneCallActivity.class);
					break;
				case Const.INTERACTION_PHONE_CALL_END:
					Util.getSharedVariable(this).service.endTrackAndRecord();
					//LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Const.OUTSIDE_PHONE_CALL_ACTIVITY_CLOSE));
					break;
				/*
				 	case Const.INTERACTION_MORE:
					startTIActivity(MoreActivity.class);
					break;
				*/
				case Const.INTERACTION_MORE:
					startTIActivity(OtherActivity.class);
					break;
				case Const.INTERACTION_SHOW_NAME:
					startTIActivity(OutsideShowNameActivity.class);
					break;
				case Const.INTERACTION_FINISH:
					Util.getSharedVariable(this).selectBusinessFlag = 0x00;
					startTIActivity(SelectBusinessActivity.class);
					break;
				case Const.INTERACTION_CAMERA_ON:
					Util.getSharedVariable(this).interval = 0;
					break;
				case Const.INTERACTION_CAMERA_IDLE:
					Util.getSharedVariable(this).interval = 9;
					break;
				case Const.INTERACTION_CAMERA_OFF:
					Util.getSharedVariable(this).interval = -1;
					break;
				case Const.INTERACTION_STOP:
					super.stopService(666);
					break;
				case Const.INTERACTION_LIGHT_ON:
					Util.getSharedVariable(this).selectBusinessFlag = 0x10;
					startTIActivity(SelectBusinessActivity.class);
					Util.getSharedVariable(this).wifiSocket.writeObject(Const.BUSINESS_PUSH_BELL);
				default:
					break;
				}
			}
		}
	}
	
	private void surfaceViewSetting(){
		SurfaceView surfaceView = new SurfaceView(this);

    	final LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(1, 1);

    	//レイアウト設定
    	surfaceView.setLayoutParams(lp);

		//オーバーレイ用レイアウト
		final WindowManager.LayoutParams wmLP = new WindowManager.LayoutParams(
				1,
				1,
				WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
				WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE |
				WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR |
				WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN |
				WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
				PixelFormat.TRANSLUCENT);

		//オーバーレイ用
		wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		this.surfaceView = surfaceView;
		//オーバーレイ表
		wm.addView(surfaceView, wmLP);
	}
	
	private boolean NumCounter(){
		if(sharedVariable.interval < 0){
			counter = false;
			return false;
		}
		
		if((num++) < sharedVariable.interval){
			counter = false;
			return false;
		}
		else{
			num = 0;
			counter = true;
			return true;
		}
	}
	
	private void startCameraCapture(){
		if(camera != null)
			return;
		
		camera = new CameraControl(surfaceView);
    	//キャプチャ時間コールバック設定
		camera.setOnCaptureListener(new CameraControl.OnCaptureListener() {
			@Override
			public void onCapture(final Bitmap bitmap) {
				new Thread(new Runnable() {
					@Override
					public void run() {
						try{
							//キャプチャ動画送信
							//8/30追加bitmap保存
							sharedVariable.setoutBm(bitmap);
							//
							//10/25追加二回に一回
							if(NumCounter()){
								wifiSocket.writeObject(Util.bitmapEncode(bitmap));
								sendImage++;
								if((sendImage - Const.RECEIVE_IMAGE) >= 100){
									sendImage = Const.RECEIVE_IMAGE;
								}
							}
							
						}
						catch(Exception e){
							return;
						}
						if(counter){
							Util.sleep(34);
						}
						//Util.sleep(sleeptime);
						
						if(camera != null)
							camera.capture();
					}
				}).start();
			}
		});
		
		//キャプチャ
		camera.capture();
	}
	
	private void endCameraCapture(){
		if(camera == null)
			return;
		
		CameraControl tmpCamera = this.camera;
		tmpCamera.release();
		this.camera = null;
	}

	@Override
	protected void stop(){
		endFlag = true;
		wifiSocket.stopConnect();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		this.wifiSocket = sharedVariable.wifiSocket;

		surfaceViewSetting();
		
		new Thread(wifiReceiveRunnable).start();
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	protected boolean isInside(){
		return false;
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onDestroy() {
		sharedVariable.service.endTrackAndRecord();
		endCameraCapture();
		wm.removeView(surfaceView);
		super.onDestroy();
	}
	
}

package jp.ac.anan_nct.TabletInterphone.Activity.Inside;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.SharedVariable;
import jp.ac.anan_nct.TabletInterphone.Activity.StartActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.Outside.SelectBusinessActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.Outside.WhoActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.Inside.VisiterLogActivity;
import jp.ac.anan_nct.TabletInterphone.Communication.BluetoothConnection;
import jp.ac.anan_nct.TabletInterphone.Communication.WifiSocket;
import jp.ac.anan_nct.TabletInterphone.Dialog.CustomDialog;
import jp.ac.anan_nct.TabletInterphone.Dialog.DialogBuilder;
import jp.ac.anan_nct.TabletInterphone.Service.InsideService;
import jp.ac.anan_nct.TabletInterphone.Utility.BluetoothUtil;
import jp.ac.anan_nct.TabletInterphone.Utility.Util;
import android.app.Activity;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.util.Log;
import android.os.PowerManager;
import android.provider.Settings;
import android.app.KeyguardManager;


public class InsideConnectRecoverActivity extends Activity {
	private static final Handler handler = new Handler();
	
	private Button bluetoothSettingButton;
	private Button startButton;
	
	private String ipAddressText;
	private BluetoothStatus bluetoothStatus;
	private WifiStatus wifiStatus;
	
	private SharedVariable sh;

	private BluetoothUtil bu;
	private BluetoothDevice targetDevice = null;

	private BluetoothConnection bc;
	private WifiSocket ws = new WifiSocket();
	
	private final int SCREEN_ON = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
			|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
			|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;	//6815744
	
	private enum BluetoothStatus{
		ERROR("Bluetooth接続に失敗しました"),
		CONNECTING("Bluetooth接続 : 接続中"),
		CONNECTED("Bluetooth接続 : OK");
				
		private String message;
		
		private BluetoothStatus(String message){
			this.message = message;
		}
		
		public String toString(){
			return this.message;
		}
	}
	
	private enum WifiStatus{
		ERROR("Wi-Fi接続に失敗しました"),
		CONNECTING("Wi-Fi接続 : 接続待機中"),
		CONNECTED("Wi-Fi接続 : OK");
				
		private String message;
		
		private WifiStatus(String message){
			this.message = message;
		}
		
		public String toString(){
			return this.message;
		}
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.d("LOGICRA","LOGICRA01");
		getWindow().addFlags(SCREEN_ON);
		startConnect();
	}

	/***************************
	 * BluetoothとWi-Fiの通信確立
	 ***************************/
	private void startConnect() {
		ipAddressText = "プライベートIPアドレス : " + getIpAddress();
		bluetoothStatus = BluetoothStatus.CONNECTING;
		wifiStatus = WifiStatus.CONNECTING;
		bc = new BluetoothConnection();
		ws = new WifiSocket();
		/*
		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("接続中");
		progressDialog.setMessage(ipAddressText + "\n\n" + bluetoothStatus.toString() + "\n" + wifiStatus.toString());
		progressDialog.setCancelable(true);
		progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "キャンセル", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});
		progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			@Override
			public void onCancel(DialogInterface dialog) {
				if(ws != null){
					ws.stopServer();
					ws = null;
				}
				
				if(bc != null){
					bc.close();
					bc = null;
				}
			}
		});
		progressDialog.show();

		// Bluetooth接続スレッド
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 接続
				/*4/21 コメントアウト
				
				while(progressDialog.isShowing() && bluetoothStatus != BluetoothStatus.CONNECTED){
					bluetoothStatus = bc.connectToServer(targetDevice) ? BluetoothStatus.CONNECTED : BluetoothStatus.CONNECTING;
					
					if(bluetoothStatus == BluetoothStatus.CONNECTED)
						refreshProgressMessage(progressDialog);
					else
						Util.sleep(2000);
				}
			}
			
		}).start();
		*/
		
		// Wi-Fi接続スレッド
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 接続待機
				wifiStatus = ws.makeServer() ? WifiStatus.CONNECTED : WifiStatus.ERROR;
				if(wifiStatus == WifiStatus.CONNECTED){
					startInside();
				}
				//refreshProgressMessage(progressDialog);
			}
		}).start();
	}
	
	private void refreshProgressMessage(final ProgressDialog dialog){
		//Wake();
		handler.post(new Runnable() {
			@Override
			public void run() {
				synchronized (dialog) {
					if(!dialog.isShowing()){
						
					}
					else if(/*bluetoothStatus == BluetoothStatus.CONNECTED 4/22コメントアウト&& */wifiStatus == WifiStatus.CONNECTED){ //両方接続完了
						dialog.dismiss();
						startInside();
					}
					else if(bluetoothStatus == BluetoothStatus.ERROR){ //Bluetooth接続エラー
						dialog.cancel();
						showErrorDialog(bluetoothStatus.toString());
					}
					else if(wifiStatus == WifiStatus.ERROR){ //Wi-Fi接続エラー
						dialog.cancel();
						showErrorDialog(wifiStatus.toString());
					}
					else{ //接続中
						dialog.setMessage(ipAddressText + "\n\n" + bluetoothStatus.toString() + "\n" + wifiStatus.toString());
					}
				}
			}
		});
	}
	
	private void startInside(){
		SharedVariable sh = Util.getSharedVariable(this);
		sh.initialRingtone(this);
		sh.wifiSocket = this.ws;
		sh.bluetoothConnection = this.bc;
		startService(new Intent(this, InsideService.class));
		/*
		new DialogBuilder(this)
		.setIcon(android.R.drawable.ic_dialog_info)
		.setTitle("設定完了")
		.setMessage("設定が完了しました。")
		.setCanceledOnTouchOutside(false)
		.setOnDismissListener(new DialogBuilder.OnDismissListener(){
			@Override
			protected void onDismiss(CustomDialog dialog){
				finish();
			}
		})
		.setPositiveButton("OK", null)
		.show("設定完了");
		*/
	}

	/***************************
	 * プライベートIPアドレス取得
	 *
	 * @return 自身のプライベートIPアドレスを返す。
	 ***************************/
	private String getIpAddress() {
		WifiManager manager = (WifiManager) getSystemService(WIFI_SERVICE);
		WifiInfo info = manager.getConnectionInfo();
		
		// IPアドレスを取得
		int ipAdr = info.getIpAddress();
		return Util.getIpAddressText(ipAdr & 0xff, (ipAdr >> 8) & 0xff, (ipAdr >> 16) & 0xff, (ipAdr >> 24) & 0xff);
	}

	/***************************
	 * Bluetoothデバイス選択
	 ***************************/
	private void showBluetoothSelectDialog() {
		this.bu = new BluetoothUtil();
		
		if (!this.bu.isSpported()) // 非対応デバイス
			DialogBuilder.showErrorDialog(this, "Bluetooth非対応デバイスです。");
		else if (!this.bu.isEnabled()) // 設定無効
			DialogBuilder.showErrorDialog(this, "Bluetooth有効にしてください。");
		else if (this.bu.getPairingCount() == 0) // ペアリング済みデバイスなし
			DialogBuilder.showErrorDialog(this, "ペアリング済みのBluetooth設定がありません。");
		else {
			new DialogBuilder(this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle("Bluetoothデバイス選択")
				.setItems(bu.getDeviceNames(), new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						decideBluetoothDevice(bu.getDevices()[which]);
					}
				})
				.setNegativeButton("キャンセル", null)
				.show("Bluetoothデバイス選択");
		}
	}

	/***************************
	 * Bluetooth端末決定
	 * 
	 * @param bluetoothDevice 選択するBluetooth端末
	 ***************************/
	private void decideBluetoothDevice(BluetoothDevice bluetoothDevice) {
		this.bluetoothSettingButton.setText("Bluetooth設定\n\n対象 : " + bluetoothDevice.getName());
		this.targetDevice = bluetoothDevice;
		this.startButton.setEnabled(true);
	}
	
	/***************************
	 * 物理キー押下時
	 ***************************/
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK){
			startActivity(new Intent(this, StartActivity.class));
			finish();
			return true;
		}
		else
			return super.onKeyDown(keyCode, event);
	}
	
	private void showErrorDialog(String message){
		DialogBuilder.showErrorDialog(this, message);
	}
}

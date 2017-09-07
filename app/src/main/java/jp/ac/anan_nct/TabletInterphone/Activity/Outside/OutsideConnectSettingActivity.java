package jp.ac.anan_nct.TabletInterphone.Activity.Outside;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import jp.ac.anan_nct.TabletInterphone.Activity.StartActivity;
import jp.ac.anan_nct.TabletInterphone.Communication.BluetoothConnection;
import jp.ac.anan_nct.TabletInterphone.Communication.WifiSocket;
import jp.ac.anan_nct.TabletInterphone.Dialog.CustomDialog;
import jp.ac.anan_nct.TabletInterphone.Dialog.DialogBuilder;
import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.Service.OutsideService;
import jp.ac.anan_nct.TabletInterphone.SharedVariable;
import jp.ac.anan_nct.TabletInterphone.Utility.BluetoothUtil;
import jp.ac.anan_nct.TabletInterphone.Utility.Util;

public class OutsideConnectSettingActivity extends Activity {
	private static final Handler handler = new Handler();

	private Button KeyBoxBluetoothSettingButton;
	private Button wifiSettingButton;
	private Button startButton;

	private BluetoothConnection bc;
	private BluetoothUtil bu;
	private BluetoothDevice targetDevice = null;

	private BluetoothStatus bluetoothStatus;
	private WifiStatus wifiStatus;

	private WifiSocket ws;
	private SharedVariable sh;

	private String ipAddressText;
	private int id1;
	private int id2;
	private int id3;
	private int id4;
	private long id0;
	private long id00;

	private enum BluetoothStatus{
		ERROR("Bluetooth接続に失敗しました"),
		CONNECTING("Bluetooth接続 : 接続中"),
		CONNECTED("Bluetooth接続 : OK"),
		NODEVICE("Bluetooth接続 ： なし");

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
		setContentView(R.layout.activity_outside_connect_setting);

		KeyBoxBluetoothSettingButton = (Button) findViewById(R.id.KeyBoxBluetoothSettingButton);
		wifiSettingButton = (Button) findViewById(R.id.wifiSettingButton);
		startButton = (Button) findViewById(R.id.endPhoneCallButton);
		sh = (SharedVariable) this.getApplication();
		startButton.setEnabled(true);

		KeyBoxBluetoothSettingButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showBluetoothSelectDialog();
			}
		});

		wifiSettingButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showIpAddressDialog(true);
			}
		});

		startButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//startConnect();
				quickstart();
			}
		});
	}
	
	private void quickstart(){
		this.ipAddressText = ConfigRead();

		Log.d("idid","idid" + ipAddressText + "idid");

		id00 = Long.parseLong(ipAddressText);
		id1 = (int)(id00 / 1000000000l);
		id0 = id1 * 1000;
		id00 = Long.parseLong(ipAddressText);
		id00 = (id00 / 1000000l);
		id2 = (int)(id00 - id0);
		id0 = Long.parseLong(ipAddressText);
		id0 = (id0 / 1000l);
		id3 = (int)(id0 - (id00 * 1000l));
		id00 = Long.parseLong(ipAddressText);
		id4 = (int)(id00 - (id0 * 1000l));

		Log.d("idid","idid" + id1 + "idid");
		Log.d("idid","idid" + id2 + "idid");
		Log.d("idid","idid" + id3 + "idid");
		Log.d("idid","idid" + id4 + "idid");
		//this.ipAddressText = Util.getIpAddressText(192, 168, 111, 101);
		this.ipAddressText = Util.getIpAddressText(id1, id2, id3, id4);
		sh.selectBusinessFlag = 0x00;
		sh.sharedIPadressText = this.ipAddressText;
		startConnect();
		//startActivity(new Intent(this, OutsideConnectByBlueToothActivity.class));
		//finish();
	}

	private String ConfigRead(){
		String adress = "192168000013";

		String filePath = Environment.getExternalStorageDirectory() +"/TIConfig//TIConfig.txt";
		File file = new File(filePath);
		file.getParentFile().mkdir();

		FileInputStream fis;

		try{
			fis = new FileInputStream(file);
			byte[] readBytes = new byte[fis.available()];
			fis.read(readBytes);
			adress = new String(readBytes);
			fis.close();
		}catch(IOException e){
		}

		Log.d("adress","adressOf" + adress);

		return adress;

	}

	/***************************
	 * Wi-Fiの通信確立
	 ***************************/
	private void startConnect() {

		wifiStatus = WifiStatus.CONNECTING;
		bluetoothStatus = BluetoothStatus.CONNECTING;

		ws = new WifiSocket();
		bc = new BluetoothConnection();

		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setTitle("接続中");
		progressDialog.setMessage(bluetoothStatus.toString() + "\n" + wifiStatus.toString());
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
					ws.stopConnect();
					ws = null;
				}

				if(bc != null){
					bc.close();
					bc = null;
				}
				showErrorDialog("Wi-Fi,Bluetooth接続に失敗しました");
			}
		});
		progressDialog.show();

		// Bluetooth接続スレッド
		new Thread(new Runnable() {
			@Override
			public void run() {
				// 接続
				//4/21 コメントアウト
				if (targetDevice == null) {
					bluetoothStatus=BluetoothStatus.NODEVICE;
					bc=null;
					refreshProgressMessage(progressDialog);
				} else{
					while (progressDialog.isShowing() && bluetoothStatus != BluetoothStatus.CONNECTED) {
						bluetoothStatus = bc.connectToServer(targetDevice) ? BluetoothStatus.CONNECTED : BluetoothStatus.CONNECTING;

						if (bluetoothStatus == BluetoothStatus.CONNECTED)
							refreshProgressMessage(progressDialog);
						else
							Util.sleep(2000);
					}
				}
			}

		}).start();

		// Wi-Fi接続スレッド
		new Thread(new Runnable() {
			@Override
			public void run() {
				wifiStatus = ws.connectToServer(ipAddressText) ? WifiStatus.CONNECTED : WifiStatus.ERROR;

				refreshProgressMessage(progressDialog);
				/*
				// 接続
				if (ws.connectToServer(ipAddressText)){
					Log.d("Ouc","El01" + ipAddressText);
					progressDialog.dismiss();
					handler.post(new Runnable(){
						@Override
						public void run(){
							startOutside();
						}
					});
				}
				else{
					progressDialog.cancel();
				}*/
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
					else if((bluetoothStatus == BluetoothStatus.CONNECTED || bluetoothStatus == BluetoothStatus.NODEVICE )
							&& wifiStatus == WifiStatus.CONNECTED){ //両方接続完了
						dialog.dismiss();
						startOutside();
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
						dialog.setMessage(bluetoothStatus.toString() + "\n" + wifiStatus.toString());
					}
				}
			}
		});
	}

	private void startOutside(){
		SharedVariable sh = Util.getSharedVariable(this);
		sh.wifiSocket = this.ws;
		sh.keyBoxBluetoothConnection= this.bc;
		Log.d("OuC","El01");
		startService(new Intent(this, OutsideService.class));
		finish();
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

	private void showIpAddressDialog(boolean b) {
		final View view = LayoutInflater.from(this).inflate(R.layout.dialog_ipaddress, null);
		final NumberPicker[] ipAddressPicker = new NumberPicker[4];
		ipAddressPicker[0] = (NumberPicker) view.findViewById(R.id.ipAddressPicker1);
		ipAddressPicker[1] = (NumberPicker) view.findViewById(R.id.ipAddressPicker2);
		ipAddressPicker[2] = (NumberPicker) view.findViewById(R.id.ipAddressPicker3);
		ipAddressPicker[3] = (NumberPicker) view.findViewById(R.id.ipAddressPicker4);
		for (int i = 0; i < ipAddressPicker.length; i++) {
			ipAddressPicker[i].setMaxValue(255);
			ipAddressPicker[i].setMinValue(0);
		}

		ipAddressPicker[0].setValue(192);
		ipAddressPicker[1].setValue(168);
		ipAddressPicker[2].setValue(000);
		ipAddressPicker[3].setValue(013);

		new DialogBuilder(this)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setTitle("Wi-Fi接続設定")
			.setView(view)
			.setNegativeButton("キャンセル", null)
			.setPositiveButton("決定", new AlertDialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
                    int ip[]= new int[4];
                    for(int i=0; i<ip.length; i++){
                            ip[i]=ipAddressPicker[i].getValue();
                    }
					writeBusiness(ip);
				}
			}).show("Wi-Fi接続設定");
	}

	private void writeBusiness(int [] ip){
		//OutputStream out;
        String tmp="";
		for(int i=0; i<4;i++) {
			if (ip[i] == 0) {
				tmp = "000";
			} else if (ip[i] < 100 && ip[i] > 10) {
				tmp = "0" + String.valueOf(ip[i]);
			}else if(ip[i]<10){
				tmp = "00" + String.valueOf(ip[i]);
			}else{
                tmp=String.valueOf(ip[i]);
            }
            if(i==0)ipAddressText=tmp;
            else ipAddressText+=tmp;
        }
		String filePath = Environment.getExternalStorageDirectory() + "/TIConfig/TIConfig.txt";
		File file = new File(filePath);
		file.getParentFile().mkdir();

		FileOutputStream fos;

		try{
			fos = new FileOutputStream(file, false);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(ipAddressText);
			bw.flush();
			bw.close();

		}catch(Exception e){
			Log.d("Vic","Vic" + e);
		}
	}

	/***************************
	 * IPアドレス設定ダイアログ表示
	 ***************************/
	private void showIpAddressDialog() {
		final View view = LayoutInflater.from(this).inflate(R.layout.dialog_ipaddress, null);
		final NumberPicker[] ipAddressPicker = new NumberPicker[4];
		ipAddressPicker[0] = (NumberPicker) view.findViewById(R.id.ipAddressPicker1);
		ipAddressPicker[1] = (NumberPicker) view.findViewById(R.id.ipAddressPicker2);
		ipAddressPicker[2] = (NumberPicker) view.findViewById(R.id.ipAddressPicker3);
		ipAddressPicker[3] = (NumberPicker) view.findViewById(R.id.ipAddressPicker4);
		for (int i = 0; i < ipAddressPicker.length; i++) {
			ipAddressPicker[i].setMaxValue(255);
			ipAddressPicker[i].setMinValue(0);
		}

		ipAddressPicker[0].setValue(192);
		ipAddressPicker[1].setValue(168);
		ipAddressPicker[2].setValue(0);
		ipAddressPicker[3].setValue(13);

		new DialogBuilder(this)
			.setIcon(android.R.drawable.ic_dialog_info)
			.setTitle("Wi-Fi接続設定")
			.setView(view)
			.setNegativeButton("キャンセル", null)
			.setPositiveButton("決定", new AlertDialog.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					int ip1 = ipAddressPicker[0].getValue();
					int ip2 = ipAddressPicker[1].getValue();
					int ip3 = ipAddressPicker[2].getValue();
					int ip4 = ipAddressPicker[3].getValue();
					ipAddressText = Util.getIpAddressText(ip1, ip2, ip3, ip4);
					sh.sharedIPadressText = ipAddressText;
					sh.InitialImgArray();
					startButton.setEnabled(true);
					wifiSettingButton.setText("Wi-Fi設定\n\n" + ipAddressText);
				}
			}).show("Wi-Fi接続設定");
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
		this.KeyBoxBluetoothSettingButton.setText("Bluetooth設定\n\n対象 : " + bluetoothDevice.getName());
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

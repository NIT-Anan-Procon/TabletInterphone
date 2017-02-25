package jp.ac.anan_nct.TabletInterphone.Activity.Outside;

import java.io.IOException;

import jp.ac.anan_nct.TabletInterphone.Const;
import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.Activity.BaseActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.StartActivity;
import jp.ac.anan_nct.TabletInterphone.Communication.WifiSocket;
import jp.ac.anan_nct.TabletInterphone.Dialog.DialogBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

public class SelectBusinessActivity extends BaseActivity {
	
	private final int SCREEN_ON = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
			|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
			|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;	//6815744
	
	private int[] imgId = new int[4];
	private int nowPage = 0;
	
	private Bundle recreate;
	
	/*
	private final WakefulBroadcastReceiver receiver = new WakefulBroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent){
			if(intent.getAction().equals(Const.OUTSIDE_SELECTBUSINESS_ACTVITY_OPEN)){
				try{
					getWindow().addFlags(SCREEN_ON);
					Log.d("ictkiki","ictkiki02"  + SCREEN_ON);
					
				}
				catch(Exception e){
					Log.d("ictkiki","ictkiki01");
				}
				finally{
					WakefulBroadcastReceiver.completeWakefulIntent(intent);
				}
			}
		}
	};
	*/
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Bundle saved = savedInstanceState;
		recreate = saved;
		
		sharedVariable.isOutside = true;
		sharedVariable.menueFlag = true;
		LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(Const.OUTSIDE_SELECTBUSINESS_ACTVITY_OPEN));
		
		final WifiSocket wifiSocket = sharedVariable.wifiSocket;
				
		
		
		if(sharedVariable.selectBusinessFlag == 0x10){
			sharedVariable.setStBmOther(null);
			sharedVariable.selectBusinessFlag = 0x11;
			setContentView(R.layout.activity_outside_next_select_business);
			changePage();
		}
		
		if(sharedVariable.selectBusinessFlag == 0x00){
			sharedVariable.setStBmOther(null);
			setContentView(R.layout.outside_wait_visiter);
			sharedVariable.interval = 9;
			wifiSocket.writeObject(Const.INTERACTION_CAMERA_IDLE);
			findViewById(R.id.wait_visiter_Layout).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					try{
					wifiSocket.writeObject(Const.BUSINESS_PUSH_BELL);
					sharedVariable.selectBusinessFlag = 0x01;
					setContentView(R.layout.activity_outside_next_select_business);
					changePage();
					onCreate(saved);
					}
					catch(Exception e){
						sharedVariable.isOutside = false;
						startTIActivity(StartActivity.class);
					}
				}
			});
		}
		else{
			sharedVariable.interval = 0;
			wifiSocket.writeObject(Const.INTERACTION_CAMERA_ON);
		}
		
		try{
			findViewById(R.id.firstPurposeLayout).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					sendAndStartCallWait(imgId[0], wifiSocket);
				}
			});
		}catch(NullPointerException e ){}
		
		try{
			findViewById(R.id.secondPurposeLayout).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					sendAndStartCallWait(imgId[1], wifiSocket);
				}
			});
		}catch(NullPointerException e ){}
		
		try{
			findViewById(R.id.thirdPurposeLayout).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					sendAndStartCallWait(imgId[2], wifiSocket);
				}
			});
		}catch(NullPointerException e ){}
		
		try{
			findViewById(R.id.FourthPurposeLayout).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					sendAndStartCallWait(imgId[3], wifiSocket);
				}
			});
		}catch(NullPointerException e ){}
		
		try{	
			findViewById(R.id.nextPageLayout).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					nowPage += 1;
					changePage();
				}
			});
		}catch(NullPointerException e ){}
		
		try{	
			findViewById(R.id.prePageLayout).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					if(nowPage != 0){
						nowPage -= 1;
						changePage();
					}
				}
			});
		}catch(NullPointerException e ){}
		
		try{	
			findViewById(R.id.otherLayout).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					wifiSocket.writeObject(Const.BUSINESS_OTHER);
					startTIActivity(OtherActivity.class);
					//startTIActivity(TalkSelectActivity.class);
				}
			});
		}catch(NullPointerException e ){}

		try{	
			findViewById(R.id.visiterCameraLayout).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					startTIActivity(VisiterCameraActivity.class);
				}
			});
		}catch(NullPointerException e ){}
		
		try{	
			findViewById(R.id.visiterWriteLayout).setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					startTIActivity(WriteVisiterActivity.class);
				}
			});
		}catch(NullPointerException e ){}
		
		if((sharedVariable.selectBusinessFlag == 0x01) || (sharedVariable.selectBusinessFlag == 0x11)){
			sharedVariable.speak("ご用件は何ですか？選択してください。");
		}
		getWindow().addFlags(SCREEN_ON);
	}
	
	private void changePage(){
		String[] idAndText = new String[3];
		boolean flag = true;
		for(int i = 0; i < 4; i++){
			try{
				idAndText = (sharedVariable.ImgArray.get((nowPage*4) + i)).split(",.",0);
				changeImg(i, Integer.parseInt(idAndText[0]));
				changeText(i, idAndText[1]);
				imgId[i] = Integer.parseInt(idAndText[2]);
			}
			catch(IndexOutOfBoundsException e){
				if((i == 0) && flag){
					nowPage = 0;
					i = -1;
					flag = false;
					continue;
				}
				break;
			}
		}
		if(nowPage == 0){
			findViewById(R.id.prePageLayout).setVisibility(View.INVISIBLE);
		}
		else{
			findViewById(R.id.prePageLayout).setVisibility(View.VISIBLE);
		}
		if(nowPage == 1){
			findViewById(R.id.nextPageLayout).setVisibility(View.INVISIBLE);
		}
		else{
			findViewById(R.id.nextPageLayout).setVisibility(View.VISIBLE);
		}
	}
	
	private void changeText(int LayoutId, String text){
		TextView businessTextView;
		switch(LayoutId){
			case 0:
				businessTextView = (TextView)findViewById(R.id.firstPurposeText);
				businessTextView.setText(text);
				break;
			case 1:
				businessTextView = (TextView)findViewById(R.id.secondPurposeText);
				businessTextView.setText(text);
				break;
			case 2:
				businessTextView = (TextView)findViewById(R.id.thirdPurposeText);
				businessTextView.setText(text);
				break;
			case 3:
				businessTextView = (TextView)findViewById(R.id.FourthPurposeText);
				businessTextView.setText(text);
				break;	
			default:
				break;
		}
	}
	
	private void changeImg(int LayoutId, int drawableId){
		Resources r = getResources();
		Drawable img;
		ImageView Layout;
		switch(LayoutId){
			case 0:
				img = r.getDrawable(drawableId);
				Layout = (ImageView)findViewById(R.id.firstPurposeImage);
				Layout.setImageDrawable(img);
				break;
			case 1:
				img = r.getDrawable(drawableId);
				Layout = (ImageView)findViewById(R.id.secondPurposeImage);
				Layout.setImageDrawable(img);
				break;
			case 2:
				img = r.getDrawable(drawableId);
				Layout = (ImageView)findViewById(R.id.thirdPurposeImage);
				Layout.setImageDrawable(img);
				break;
			case 3:
				img = r.getDrawable(drawableId);
				Layout = (ImageView)findViewById(R.id.FourthPurposeImage);
				Layout.setImageDrawable(img);
				break;
			default:
				break;
		}
	}
	
	/*
	private void setImgId(int num1, int num2, int num3){
		imgId[0] = num1;
		imgId[1] = num2;
		imgId[2] = num3;
	}
	*/
	
	private void sendAndStartCallWait(int business, WifiSocket ws){
		ws.writeObject(business);
		startTIActivity(CallWaitActivity.class);
	}
	
	@Override
	protected void onResume(){
		super.onResume();
		//IntentFilter filter = new IntentFilter(Const.OUTSIDE_SELECTBUSINESS_ACTVITY_OPEN);
		//LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
		//Log.d("ictkiki","ictkiki03");
		if(sharedVariable.selectBusinessFlag == 0x21){
			sharedVariable.selectBusinessFlag = 0x11;
			setContentView(R.layout.activity_outside_next_select_business);
			changePage();
			sharedVariable.wifiSocket.writeObject(Const.BUSINESS_PUSH_BELL);
			onCreate(recreate);
		}	
		else{
			//getWindow().addFlags(SCREEN_ON);
		}
	}
	/*
	@Override
	protected void onPause(){
		getWindow().clearFlags(SCREEN_ON);
		super.onPause();
	}
	
	/***************************
	 * 物理キー押下時
	 ***************************/
	/*
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK){
			return true;
		}
		else
			return super.onKeyDown(keyCode, event);
	}	
	*/
}

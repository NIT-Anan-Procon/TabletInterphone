package jp.ac.anan_nct.TabletInterphone.Activity.Inside;

import jp.ac.anan_nct.TabletInterphone.Const;
import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.SharedVariable;
import jp.ac.anan_nct.TabletInterphone.Activity.BaseActivity;
import jp.ac.anan_nct.TabletInterphone.Dialog.CustomDialog;
import jp.ac.anan_nct.TabletInterphone.Dialog.DialogBuilder;
import jp.ac.anan_nct.TabletInterphone.Dialog.LeftDialogBuilder;
import jp.ac.anan_nct.TabletInterphone.Fragment.CameraViewFragment;
import jp.ac.anan_nct.TabletInterphone.Fragment.VisiterInteractionFragment;
import jp.ac.anan_nct.TabletInterphone.Serializable.WriteSerializable;
import jp.ac.anan_nct.TabletInterphone.Utility.Util;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;


public class VisiterCheckActivity extends BaseActivity {
	private int businessId = -1;
	private String moreBusiness = null;
	private WriteSerializable img = null;
	
	private CustomDialog visiterCheckDialog;
	private LeftDialogBuilder LdialogBuilder;
	
	private final int SCREEN_ON = WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
								|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
								|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD;	//6815744
	private int keyflag = 0;
	private boolean manekoFlag = false;

	private final WakefulBroadcastReceiver receiver = new WakefulBroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent){
			if(intent.getAction().equals(Const.INSIDE_VISITERCHECK_ACTVITY_OPEN)){
				try{
					
					//Window currentWindow = getWindow();
					getWindow().addFlags(SCREEN_ON);
					//keyflag++;
					Log.d("hahaha","hahaha02"  + getWindow().toString());
					/*
					Log.d("hahaha","hahaha03");
					currentWindow.addFlags(
							WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
							|WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
							);
					
					Log.d("hahaha","hahaha01");
					currentWindow.addFlags(
							WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON
							);
					*/
					
				}
				catch(Exception e){
					Log.d("hahaha","hahaha05");
				}
				finally{
					Log.d("hahaha","hahaha06");
					WakefulBroadcastReceiver.completeWakefulIntent(intent);
				}
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inside_visiter_check);
		sharedVariable.visiterCheckActivity = this;
		
		sharedVariable.vcFlag = true;
		sharedVariable.isOutside = false;
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.leftFragment, new VisiterInteractionFragment());
		ft.replace(R.id.rightFragment, new CameraViewFragment());
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
		
		/*
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_visit, null);
		visiterCheckDialog = new DialogBuilder(this)
		.setTitle("用件")
		.setView(view)
		.setPositiveButton("OK", null)
		.setOnDismissListener(new DialogBuilder.OnDismissListener() {
			@Override
			protected void onDismiss(CustomDialog dialog) {

				/*0429コメントアウト
				if(businessId == -1)
					sharedVariable.bluetoothConnection.write(Const.BLUETOOTH_CANCEL);
					*/
		/*	
				if(visiterCheckDialog != null)
					visiterCheckDialog = null;
			}
		})
		.setCanceledOnTouchOutside(false)
		.create();
		
		visiterCheckDialog.show(this, "訪問確認ダイアログ");
		*/
		
		IntentFilter filter = new IntentFilter(Const.INSIDE_VISITERCHECK_ACTVITY_OPEN);
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
		getWindow().addFlags(SCREEN_ON);
		Log.d("hahaha","hahaha01");
	}
	
	private void writeBusiness(int businessWId){
		//OutputStream out;
		Calendar calendar = Calendar.getInstance();
		String year = String.valueOf(calendar.get(Calendar.YEAR));
		String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
		String day = String.valueOf(calendar.get(Calendar.DATE));
		String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));
		String min = String.valueOf(calendar.get(Calendar.MINUTE));
		
		String filePath = Environment.getExternalStorageDirectory() + "/TILog/" +"/" + (year + "@" + month + ".txt");
		File file = new File(filePath);
		file.getParentFile().mkdir();
		
		FileOutputStream fos;
		
		ImageView cameraView = sharedVariable.cameraView;
		Bitmap cameraBM = ((BitmapDrawable)cameraView.getDrawable()).getBitmap();
		

		
		String pngFilePath = Environment.getExternalStorageDirectory() + "/TILog/" +"/" + (year + "_" + month + "_" + day + "_" + hour + "_" + min + ".png");
		File pngFile = new File(pngFilePath);
		
		
		
		try{
			fos = new FileOutputStream(pngFile);
			cameraBM.compress(CompressFormat.PNG, 100, fos);
			fos.close();
		}catch(Exception e){
			Log.d("Vic","Vic02" + e);
		}
		
		try{
			fos = new FileOutputStream(file, true);
			OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
			BufferedWriter bw = new BufferedWriter(osw);
			bw.write(("\r\n"+ day +"/"+ hour +"/" +min +"/"+ Util.getBusinessText(businessWId)));
			bw.flush();
			bw.close();
			
		}catch(Exception e){
			Log.d("Vic","Vic" + e);
		}
		/*
		try{
			out = openFileOutput((year + "@" + month + ".txt"),MODE_PRIVATE|MODE_APPEND);
			PrintWriter TIwriter = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));
			TIwriter.append("\r\n"+ day +";"+ min +":"+ Util.getBusinessText(businessWId));
			TIwriter.close();
		}catch(IOException e){
			Log.d("VisChe","writeerror");
		}
		*/
	}
	
	public void setBusiness(int businessId){
		this.businessId = businessId;
		noticeBusiness(); 
		if(businessId != Const.BUSINESS_PRE_SELECT){
			writeBusiness(businessId);
			showBusinessDialog(Util.getBusinessImageId(businessId), Util.getBusinessText(businessId));
		}
	}
	
	public void setImage(WriteSerializable img){
		this.img = img;
		noticeBusiness(); 
		showImageDialog(img);
	}
	
	public void setDisConnect(){
		this.businessId = Const.BUSINESS_OTHER;
		this.moreBusiness = "通信失敗";
		noticeBusiness();
		
		sharedVariable.writeReaction("通信失敗");
		
		showBusinessDialog(R.drawable.other, "警告：通信に失敗しました。再設定して下さい。");
	}

	public void setOtherBusiness(String otherBusiness){
		this.businessId = Const.BUSINESS_OTHER;
		this.moreBusiness = otherBusiness;
		noticeBusiness();
		
		sharedVariable.writeReaction("その他(" + otherBusiness + ")");
		
		showBusinessDialog(R.drawable.other, "その他(" + otherBusiness + ")");
	}
	
	public void setMoreBusiness(String moreBusiness){
		this.moreBusiness = moreBusiness;
		noticeBusiness();
		
		sharedVariable.writeReaction("詳細(" + moreBusiness + ")");
		
		showBusinessDialog(Util.getBusinessImageId(this.businessId), moreBusiness);
	}
	
	private void noticeBusiness(){
		Fragment currentFragment = this.getFragmentManager().findFragmentById(R.id.leftFragment);
		if(currentFragment instanceof VisiterInteractionFragment){
			VisiterInteractionFragment visiterInteractionFragment = (VisiterInteractionFragment)currentFragment;
			visiterInteractionFragment.loadBusiness(this.businessId, moreBusiness);
			moreBusiness = null;
		}
		else{
			VisiterInteractionFragment fragment = new VisiterInteractionFragment();
			Bundle args = new Bundle();
			args.putInt("BusinessID", this.businessId);
			args.putString("MoreBusiness", moreBusiness);
			fragment.setArguments(args);
			
			changeLeftFragment(fragment);
		}
	}
	
	public void changeLeftFragment(Fragment fragment){
		if(fragment instanceof VisiterInteractionFragment){
			Bundle args = fragment.getArguments();
			if(args == null)
				args = new Bundle();
			args.putInt("BusinessID", this.businessId);
			args.putString("MoreBusiness", moreBusiness);
			fragment.setArguments(args);
		}
		
		FragmentTransaction transaction = this.getFragmentManager().beginTransaction();
		transaction.replace(R.id.leftFragment, fragment);
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		transaction.commit();
	}
	
	public void setOtherBusinessAddImage(String otherBusiness){
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_write_visiter, null);
		TextView businessTextView = (TextView)view.findViewById(R.id.interactionTextView);
		ImageView businessImageView = (ImageView)view.findViewById(R.id.businessImageView);
		Bitmap bitmap = sharedVariable.getWserialOther().getBitmap();
		businessTextView.setText(otherBusiness);
		businessImageView.setImageBitmap(bitmap);
		businessImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		
		LdialogBuilder = new LeftDialogBuilder(this)
		.setTitle("用件")
		.setView(view)
		.setPositiveButton("参ります", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int witch){
				sharedVariable.wifiSocket.writeObject(Const.INTERACTION_WAIT);
				//7/20追加
				sharedVariable.writeReaction("参ります");
				//
			}
			
		})
		.setNegativeButton("結構です", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int witch){
				sharedVariable.wifiSocket.writeObject(Const.INTERACTION_GET_OUT);
				//7/20追加
				sharedVariable.writeReaction("結構です");
			}
			
		})
		.setNeutralButton("閉じる", null)
		.setInverseBackgroundForced(true)
		.setCanceledOnTouchOutside(false);

		if(visiterCheckDialog != null){
			visiterCheckDialog.dismiss();
			LdialogBuilder.setOnDismissListener(new LeftDialogBuilder.OnDismissListener() {
				@Override
				protected void onDismiss(CustomDialog dialog) {
					sharedVariable.bluetoothConnection.write(Const.BLUETOOTH_CANCEL);
				}
			});
		}
		
		LdialogBuilder.create().show(this, "用件確認ダイアログ");
	}
	
	private void showImageDialog(WriteSerializable img){
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_write_visiter, null);
		TextView businessTextView = (TextView)view.findViewById(R.id.interactionTextView);
		ImageView businessImageView = (ImageView)view.findViewById(R.id.businessImageView);
		Bitmap bitmap = img.getBitmap();
		businessTextView.setText("");
		businessImageView.setImageBitmap(bitmap);
		businessImageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
		
		LdialogBuilder = new LeftDialogBuilder(this)
		.setTitle("用件")
		.setView(view)
		.setPositiveButton("参ります", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int witch){
				sharedVariable.wifiSocket.writeObject(Const.INTERACTION_WAIT);
				//7/20追加
				sharedVariable.writeReaction("参ります");
				//
			}
			
		})
		.setNegativeButton("結構です", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int witch){
				sharedVariable.wifiSocket.writeObject(Const.INTERACTION_GET_OUT);
				//7/20追加
				sharedVariable.writeReaction("結構です");
			}
			
		})
		.setNeutralButton("閉じる", null)
		.setInverseBackgroundForced(true)
		.setCanceledOnTouchOutside(false);

		if(visiterCheckDialog != null){
			visiterCheckDialog.dismiss();
			LdialogBuilder.setOnDismissListener(new LeftDialogBuilder.OnDismissListener() {
				@Override
				protected void onDismiss(CustomDialog dialog) {
					sharedVariable.bluetoothConnection.write(Const.BLUETOOTH_CANCEL);
				}
			});
		}
		
		LdialogBuilder.create().show(this, "用件確認ダイアログ");
	}
	
	private void showBusinessDialog(int businessImageId, String businessText){
		View view = LayoutInflater.from(this).inflate(R.layout.dialog_business, null);
		TextView businessTextView = (TextView)view.findViewById(R.id.interactionTextView);
		ImageView businessImageView = (ImageView)view.findViewById(R.id.businessImageView);
		businessTextView.setText(businessText);
		businessImageView.setImageResource(businessImageId);
		
		LdialogBuilder = new LeftDialogBuilder(this)
		.setTitle("用件")
		.setView(view)
		.setPositiveButton("参ります", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int witch){
				sharedVariable.wifiSocket.writeObject(Const.INTERACTION_WAIT);
				//7/20追加
				sharedVariable.writeReaction("参ります");
				//
			}
			
		})
		.setNegativeButton("結構です", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int witch){
				sharedVariable.wifiSocket.writeObject(Const.INTERACTION_GET_OUT);
				//7/20追加
				sharedVariable.writeReaction("結構です");
			}
			
		})
		.setNeutralButton("閉じる", null)
		.setInverseBackgroundForced(true)
		.setCanceledOnTouchOutside(false);

		if(visiterCheckDialog != null){
			visiterCheckDialog.dismiss();
			LdialogBuilder.setOnDismissListener(new LeftDialogBuilder.OnDismissListener() {
				@Override
				protected void onDismiss(CustomDialog dialog) {
					sharedVariable.bluetoothConnection.write(Const.BLUETOOTH_CANCEL);
				}
			});
		}
		
		LdialogBuilder.create().show(this, "用件確認ダイアログ");
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
		Log.d("hahaha","hahaha00onSaveInstanceState");
    	super.onSaveInstanceState(outState);
    }
	
	@Override
	protected void onPause(){
		Log.d("hahaha","hahaha00onPause");
		getWindow().clearFlags(SCREEN_ON);
		//sharedVariable.wifiSocket.writeObject(Const.INTERACTION_FINISH);
		super.onPause();
	}
	
	@Override
    protected void onUserLeaveHint() {
		Log.d("hahaha","hahaha00onUserLeaveHint");
		/*
		Log.d("hahaha","hahaha03");
		getWindow().addFlags(SCREEN_ON);
		keyflag++;
		getWindow().clearFlags(SCREEN_ON);
		Log.d("hahaha","hahaha04" + String.valueOf(keyflag));
		keyflag--;
		sharedVariable.keyGuardStatus = true;
		*/
		super.onUserLeaveHint();
    }
	
	@Override
    public boolean onCreateThumbnail(Bitmap outBitmap, Canvas canvas) {
		Log.d("hahaha","hahaha00onCreateThumbnail");
		return super.onCreateThumbnail(outBitmap, canvas);
    }
	
	@Override
    public CharSequence onCreateDescription() {
		Log.d("hahaha","hahaha00onCreateDescription");
		return super.onCreateDescription();
    }
	
	@Override
	protected void onStop(){
		Log.d("hahaha","hahaha00Stop");
		//manekoFlag = false;
		super.onStop();
	}
	
	@Override
	protected void onDestroy(){
		Log.d("hahaha","hahaha00onDestroy");
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
		super.onDestroy();
	}
}

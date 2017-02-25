package jp.ac.anan_nct.TabletInterphone.Activity.Outside;

import jp.ac.anan_nct.TabletInterphone.PaintView;
import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.SharedVariable;
import jp.ac.anan_nct.TabletInterphone.Activity.BaseActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.Inside.VisiterCheckActivity;
import jp.ac.anan_nct.TabletInterphone.Fragment.CameraViewFragment;
import jp.ac.anan_nct.TabletInterphone.Fragment.OutsideCameraViewFragment;
import jp.ac.anan_nct.TabletInterphone.Fragment.VisiterInteractionFragment;
import jp.ac.anan_nct.TabletInterphone.Serializable.WriteSerializable;
import jp.ac.anan_nct.TabletInterphone.Utility.Util;
import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Handler;
import jp.ac.anan_nct.TabletInterphone.Const;


public class VisiterCameraActivity extends BaseActivity {
	
	private Bitmap bit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_outside_name_check);
		
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		//ft.replace(R.id.leftFragment, new VisiterInteractionFragment());
		ft.replace(R.id.rightFragment, new OutsideCameraViewFragment());
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.commit();
		
		findViewById(R.id.Button_namecheck).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				bit = sharedVariable.getoutBm();
				sharedVariable.setStBmOther(bit);
				sharedVariable.speak("撮影しました。戻るボタンを押してください。");
				sharedVariable.menueFlag = false;
				final TextView Ctext = (TextView) findViewById(R.id.textViewCamera);
				Ctext.setText("撮影しました。戻るボタンを押してください。");
				Ctext.setTextColor(Color.RED);
			}
		});
		
		findViewById(R.id.Button_back).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				if(sharedVariable.menueFlag){
					sharedVariable.selectBusinessFlag = 0x10;
					startTIActivity(SelectBusinessActivity.class);
				}
				else{
					startTIActivity(OtherActivity.class);
				}
			}
		});
		
		
		
	}

	protected String getTIActivity(){
		return "VisiterCameraActivity";
	}
	/*
	@Override
	protected void startTIActivity(Intent intent){
		super.startTIActivity(intent);
		
	}
	*/
}

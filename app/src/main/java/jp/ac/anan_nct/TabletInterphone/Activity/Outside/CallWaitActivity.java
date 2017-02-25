package jp.ac.anan_nct.TabletInterphone.Activity.Outside;

import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.SharedVariable;
import jp.ac.anan_nct.TabletInterphone.Activity.BaseActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.Inside.VisiterCheckActivity;
import jp.ac.anan_nct.TabletInterphone.Utility.Util;
import android.annotation.SuppressLint;
import android.content.Intent;
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
import android.os.Handler;
import jp.ac.anan_nct.TabletInterphone.Const;


public class CallWaitActivity extends BaseActivity {
	
	//0511追加
	private Timer waitTimer;					//タイマー用
	private MainTimerTask waitTimerTask;		//タイマタスククラス
	private int count = 0;
	private int endflag = 0;
	Handler m_handler = null;
	private int TurnTime = 0;
	private int HTurnTime = 0;
	private SharedVariable sh;

	//
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_outside_call_wait);
		
		//0511追加
		m_handler = new Handler();
		this.waitTimer = new Timer();
		//タスククラスインスタンス生成
		this.waitTimerTask = new MainTimerTask();				
		//タイマースケジュール設定＆開始
		this.waitTimer.schedule(waitTimerTask, 1000,1000);
		//
		sh = (SharedVariable) this.getApplication();
		
		sh.setTurnActivity();
		
		TurnTime = sh.TurnTime;
		HTurnTime = TurnTime/2;
		
		
		sharedVariable.speak("只今呼び出し中です、少々お待ちください。");
		findViewById(R.id.button_callwait_back).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				sharedVariable.selectBusinessFlag = 0x10;
				startTIActivity(SelectBusinessActivity.class);
			}
		});
		
	}
	
	//0511追加
	protected class MainTimerTask extends TimerTask {
	@Override
		public void run() {
			count++;
			if(count > (HTurnTime)){
				m_handler.post(new Runnable() {
	                @Override
	                public void run() {
	                	if(endflag == 0){
	                		setContentView(R.layout.activity_outside_absence);
	                		sharedVariable.speak("申し訳ありません、只今留守にしております。");
	                		endflag = 1;
	                	}
	                }
	            });
				if(count > TurnTime){
					waitTimer.cancel();
					sharedVariable.wifiSocket.writeObject(Const.BUSINESS_PRE_SELECT);
					sharedVariable.selectBusinessFlag = 0x00;
					startTIActivity(SelectBusinessActivity.class);
				}
			}
		}
	}
	
	@SuppressLint({ "WorldReadableFiles", "WorldWriteableFiles" })
	@SuppressWarnings("deprecation")
	private void writeActivity(String ActivityName){
		OutputStream out;
		Calendar calendar = Calendar.getInstance();
		String month = String.valueOf((calendar.get(Calendar.MONTH) + 1));
		String day = String.valueOf(calendar.get(Calendar.DATE));
		try{
			out = openFileOutput((month + "at" + day + ".txt"),MODE_PRIVATE|MODE_APPEND);
			PrintWriter TIwriter = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));
			TIwriter.append("\r\n" + ActivityName);
			TIwriter.close();
			Log.d("CaWa",(month + "at" + day + ".txt"));
		}catch(IOException e){
			Log.d("CaWa","writeerror");
		}
	}
	
	protected String getTIActivity(){
		return "CallWaitActivity";
	}
	
	
	@Override
	protected void startTIActivity(Class<?> cls){
		writeActivity(getTIActivity());
		super.startTIActivity(cls);
	}
	
	@Override
	protected void startTIActivity(Intent intent){
		super.startTIActivity(intent);
	}
	
	@Override
	protected void onPause(){
		super.onPause();
		waitTimer.cancel();	
	}
	//
	
}

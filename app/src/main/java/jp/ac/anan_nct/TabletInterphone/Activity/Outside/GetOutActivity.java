package jp.ac.anan_nct.TabletInterphone.Activity.Outside;

import jp.ac.anan_nct.TabletInterphone.Const;
import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.Activity.BaseActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.Outside.CallWaitActivity.MainTimerTask;
import android.os.Bundle;
import android.os.Handler;
import jp.ac.anan_nct.TabletInterphone.SharedVariable;

import java.util.Timer;
import java.util.TimerTask;

import jp.ac.anan_nct.TabletInterphone.Const;

public class GetOutActivity extends BaseActivity {
	
	//0511追加
	private Timer mainTimer;					//タイマー用
	private MainTimerTask mainTimerTask;		//タイマタスククラス
	private int count = 0;
	private int TurnTime = 0;
	private SharedVariable sh;
	//
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_outside_get_out);
		
		//0511追加
		this.mainTimer = new Timer();
		//タスククラスインスタンス生成
		this.mainTimerTask = new MainTimerTask();				
		//タイマースケジュール設定＆開始
		this.mainTimer.schedule(mainTimerTask, 1000,1000);
		//
		sh = (SharedVariable) this.getApplication();
		
		sh.setTurnActivity();
		
		TurnTime = sh.TurnTime;
		
		sharedVariable.speak("結構です、お帰りください。");
	}
	
	//0511追加
		public class MainTimerTask extends TimerTask {
		@Override
			public void run() {
				count++;
				if(count > TurnTime){
					mainTimer.cancel();
					sharedVariable.wifiSocket.writeObject(Const.BUSINESS_PRE_SELECT);
					sharedVariable.selectBusinessFlag = 0x00;
					startTIActivity(SelectBusinessActivity.class);
				}
			}
		}
		
		@Override
		protected void onPause(){
			super.onPause();
			mainTimer.cancel();	
		}
		
		
		//
	
}

package jp.ac.anan_nct.TabletInterphone.Activity.Outside;

import jp.ac.anan_nct.TabletInterphone.Const;
import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.Utility.Util;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

public class OutsidePhoneCallActivity extends Activity{
	private final BroadcastReceiver receiver = new BroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent){
			if(intent.getAction().equals(Const.OUTSIDE_PHONE_CALL_ACTIVITY_CLOSE))
				finish();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_outside_phone_call);
		
		Util.getSharedVariable(this).service.startTrackAndRecord();
		
		IntentFilter filter = new IntentFilter(Const.OUTSIDE_PHONE_CALL_ACTIVITY_CLOSE);
		LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
	}
	
	@Override
	protected void onDestroy(){
		LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
		Util.getSharedVariable(this).service.endTrackAndRecord();
		super.onDestroy();
	}
}

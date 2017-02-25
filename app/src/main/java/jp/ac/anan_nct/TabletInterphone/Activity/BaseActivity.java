package jp.ac.anan_nct.TabletInterphone.Activity;

import jp.ac.anan_nct.TabletInterphone.SharedVariable;
import jp.ac.anan_nct.TabletInterphone.Utility.Util;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

public abstract class BaseActivity extends Activity{
	protected SharedVariable sharedVariable;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		sharedVariable = Util.getSharedVariable(this);
		sharedVariable.setCurrentTIActivity(this);
	}
	
	protected void startActivity(Class<?> cls){
		startActivity(new Intent(this, cls));
	}
	
	protected void startTIActivity(Class<?> cls){
		startTIActivity(new Intent(this, cls));
	}
	
	protected void startTIActivity(Intent intent){
		sharedVariable.startTIActivity(intent);
	}
	
	/***************************
	 * 物理キー押下時
	 ***************************/
	///*
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event){
		if(event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK){
			if(!sharedVariable.isOutside){
			Log.d("KeyCode","KeyCode");
			moveTaskToBack(true);
			}
			return true;
		}
		else
			return super.onKeyDown(keyCode, event);
	}
	//*/
	
}

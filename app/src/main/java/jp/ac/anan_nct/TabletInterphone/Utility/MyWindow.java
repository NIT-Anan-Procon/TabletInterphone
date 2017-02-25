package jp.ac.anan_nct.TabletInterphone.Utility;

import android.content.Context;
import android.view.Window;

public abstract class MyWindow extends Window{

	public MyWindow(Context context) {
		super(context);
	}
	
	public int getFlags(){
		return super.getForcedWindowFlags();
	}

}

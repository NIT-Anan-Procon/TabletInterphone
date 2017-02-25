package jp.ac.anan_nct.TabletInterphone.Activity.Outside;

import jp.ac.anan_nct.TabletInterphone.Const;
import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.Activity.BaseActivity;
import android.os.Bundle;
import android.view.View;



public class WaitVisiterActivity extends BaseActivity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.outside_wait_visiter);
		
		findViewById(R.id.wait_visiter_Layout).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				sharedVariable.wifiSocket.writeObject(Const.BUSINESS_PRE_SELECT);
				startTIActivity(SelectBusinessActivity.class);
			}
		});
		
	}
	
}

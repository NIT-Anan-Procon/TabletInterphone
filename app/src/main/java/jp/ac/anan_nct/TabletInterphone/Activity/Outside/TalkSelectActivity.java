package jp.ac.anan_nct.TabletInterphone.Activity.Outside;

import jp.ac.anan_nct.TabletInterphone.Const;
import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.Activity.BaseActivity;
import android.os.Bundle;
import android.view.View;




public class TalkSelectActivity extends BaseActivity {
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_outside_selecting);
		
		findViewById(R.id.select_speak_Layout).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				startTIActivity(OtherActivity.class);
			}
		});
		
		findViewById(R.id.selact_write_Layout).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				startTIActivity(WriteVisiterActivity.class);
			}
		});
		
	}
	
}

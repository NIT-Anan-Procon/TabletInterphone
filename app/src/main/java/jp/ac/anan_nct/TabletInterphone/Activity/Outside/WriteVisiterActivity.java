package jp.ac.anan_nct.TabletInterphone.Activity.Outside;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import jp.ac.anan_nct.TabletInterphone.PaintView;
import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.Activity.BaseActivity;
import jp.ac.anan_nct.TabletInterphone.Serializable.WriteSerializable;

public class WriteVisiterActivity extends BaseActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_write);
		
		final PaintView paintView = (PaintView)findViewById(R.id.paintView);
		final Button sendButton = (Button) findViewById(R.id.sendButton);
		sendButton.setText("決定");
		
		findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				startTIActivity(OtherActivity.class);
				sharedVariable.setStBmOther(paintView.getBitmap());
			}
		});
		
		findViewById(R.id.delButton).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				paintView.clear();
			}
		});
		
		findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
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

	/*
	@Override
	public void onPause() {
		sendButton.setText("送信");
		super.onPause();
	}
	
	@Override
	protected int getLayoutId(){
		return R.layout.fragment_write;
	}
	*/
}
package jp.ac.anan_nct.TabletInterphone.Activity.Inside;

import jp.ac.anan_nct.TabletInterphone.Const;
import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.SharedVariable;
import jp.ac.anan_nct.TabletInterphone.Dialog.CustomDialog;
import jp.ac.anan_nct.TabletInterphone.Dialog.DialogBuilder;
import jp.ac.anan_nct.TabletInterphone.Utility.Util;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

public class InsideHomeSettingActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_inside_home_setting);
		
		final NumberPicker turnonNumberPicker = (NumberPicker)findViewById(R.id.turnonNumberPicker);
		final NumberPicker turnoffNumberPicker = (NumberPicker)findViewById(R.id.turnoffNumberPicker);
		final NumberPicker repeatNumberPicker = (NumberPicker)findViewById(R.id.repeatNumberPicker);
		
		turnonNumberPicker.setMaxValue(10);
		turnonNumberPicker.setMinValue(1);

		turnoffNumberPicker.setMaxValue(5);
		turnoffNumberPicker.setMinValue(1);

		repeatNumberPicker.setMaxValue(5);
		repeatNumberPicker.setMinValue(0);
		
		final SharedVariable sharedVariable = Util.getSharedVariable(this);

		findViewById(R.id.okButton).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				int turnon = turnonNumberPicker.getValue();
				int turnoff = turnoffNumberPicker.getValue();
				int repeat = repeatNumberPicker.getValue();
				
				sharedVariable.bluetoothConnection.write(Const.BLUETOOTH_JUDGE_HOME_TURNON | turnon);
				sharedVariable.bluetoothConnection.write(Const.BLUETOOTH_JUDGE_HOME_TURNOFF | turnoff);
				sharedVariable.bluetoothConnection.write(Const.BLUETOOTH_JUDGE_HOME_REPEAT | repeat);
		
				StringBuilder sb = new StringBuilder(35)
				.append("点灯時間:")
				.append(turnon)
				.append("秒\n消灯時間:")
				.append(turnoff)
				.append("秒\n繰り返し回数:")
				.append(repeat)
				.append("回\n\n設定しました");
				
				showDialog("回転灯制御設定完了", sb.toString());
			}
		});
		
		findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				finish();
			}
		});
	}
	
	private void showDialog(String title, String message){
		new DialogBuilder(this)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton("OK", null)
		.setOnDismissListener(new DialogBuilder.OnDismissListener() {
			@Override
			protected void onDismiss(CustomDialog dialog) {
				finish();
			}
		})
		.setCanceledOnTouchOutside(false)
		.create()
		.show(this, "モード設定ダイアログ");
	}
}

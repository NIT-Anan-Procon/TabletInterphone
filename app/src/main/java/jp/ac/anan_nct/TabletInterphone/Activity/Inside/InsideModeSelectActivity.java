package jp.ac.anan_nct.TabletInterphone.Activity.Inside;

import jp.ac.anan_nct.TabletInterphone.Const;
import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.SharedVariable;
import jp.ac.anan_nct.TabletInterphone.Activity.Inside.VisiterLogActivity;
import jp.ac.anan_nct.TabletInterphone.Dialog.CustomDialog;
import jp.ac.anan_nct.TabletInterphone.Dialog.DialogBuilder;
import jp.ac.anan_nct.TabletInterphone.Service.InsideService;
import jp.ac.anan_nct.TabletInterphone.Utility.Util;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InsideModeSelectActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		final SharedVariable sharedVariable = Util.getSharedVariable(this);
		if(sharedVariable.service == null){
			showFinishDialog("エラー", "タブレットインターホンシステムが起動されていません");
			return;
		}
		else if(!(sharedVariable.service instanceof InsideService)){
			showFinishDialog("エラー", "タブレットインターホンシステムが屋内モードで起動されていません");
			return;
		}
		
		setContentView(R.layout.activity_inside_mode_select);
		
		findViewById(R.id.homeModeLayout).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				sharedVariable.bluetoothConnection.write(Const.BLUETOOTH_MODE_HOME);
				showDialog("モード設定完了", "在宅モードに設定しました");
			}
		});
		
		findViewById(R.id.nightModeLayout).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				sharedVariable.bluetoothConnection.write(Const.BLUETOOTH_MODE_NIGHT);
				showDialog("モード設定完了", "夜間モードに設定しました");
			}
		});
		
		findViewById(R.id.outdoorModeLayout).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				sharedVariable.bluetoothConnection.write(Const.BLUETOOTH_MODE_OUTDOOR);
				showDialog("モード設定完了", "外出モードに設定しました");
			}
		});
		
		findViewById(R.id.settingLayout).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				/*
				startActivity(InsideSettingActivity.class);
				*/
				startActivity(VisiterLogActivity.class);
			}
		});
	}
	
	private void showDialog(String title, String message){
		new DialogBuilder(this)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton("OK", null)
		.setCanceledOnTouchOutside(false)
		.create()
		.show(this, "モード設定ダイアログ");
	}
	
	private void showFinishDialog(String title, String message){
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
		.show(this, "終了ダイアログ");
	}
	
	private void startActivity(Class<?> cls){
		startActivity(new Intent(this, cls));
	}
}

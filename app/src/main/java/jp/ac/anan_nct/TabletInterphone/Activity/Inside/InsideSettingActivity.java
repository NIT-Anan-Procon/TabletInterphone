package jp.ac.anan_nct.TabletInterphone.Activity.Inside;

import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.Dialog.DialogBuilder;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class InsideSettingActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_inside_setting);
		
		findViewById(R.id.patliteControlLayout).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				showSelectModeDialog();
			}
		});
		
		findViewById(R.id.absenceMessageLayout).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){

			}
		});
	}
	
	private void showSelectModeDialog(){
		new DialogBuilder(this)
		.setTitle("設定を変更するモードを選択してください")
		.setItems(new String[]{"在宅モード", "夜間モード"}, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				if(which == 0)
					startActivity(InsideHomeSettingActivity.class);
				else
					startActivity(InsideNightSettingActivity.class);
			}
		})
		.setCanceledOnTouchOutside(false)
		.create()
		.show(this, "モード選択ダイアログ");
	}
	
	private void startActivity(Class<?> cls){
		startActivity(new Intent(this, cls));
	}
}

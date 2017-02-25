package jp.ac.anan_nct.TabletInterphone.Activity.Inside;

import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.Activity.BaseActivity;
import jp.ac.anan_nct.TabletInterphone.Dialog.DialogBuilder;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;



public class InsidePopDialogActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_inside_dialog);
		Log.d("usasa","usasa03");
		DialogBuilder dialogBuilder = new DialogBuilder(this)
		.setTitle("用件")
		.setPositiveButton("確認", new DialogInterface.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int witch){
				startTIActivity(VisiterCheckActivity.class);
			}
			
		})
		.setNegativeButton("閉じる", null)
		.setCanceledOnTouchOutside(false);
		Log.d("usasa","usasa04");
		dialogBuilder.create().show(this, "用件確認ダイアログ");
		
		

	}
	
	
}


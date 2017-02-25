package jp.ac.anan_nct.TabletInterphone.Activity;

import jp.ac.anan_nct.TabletInterphone.Dialog.CustomDialog;
import jp.ac.anan_nct.TabletInterphone.Dialog.DialogBuilder;
import jp.ac.anan_nct.TabletInterphone.Utility.Util;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;

public class ServiceCancelDialogActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		showDialog();
	}
	
	private void showDialog(){
		new DialogBuilder(this)
			.setTitle("終了確認")
			.setMessage("Tablet Interphoneを終了しますか？")
			.setPositiveButton("はい", new DialogInterface.OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which){
					Util.getSharedVariable(ServiceCancelDialogActivity.this).service.stopService();
				}
			})
			.setNegativeButton("いいえ", null)
			.setCanceledOnTouchOutside(false)
			.setOnDismissListener(new DialogBuilder.OnDismissListener(){
				@Override
				public void onDismiss(CustomDialog dialog){
					finish();
				}
			})
			.create()
			.show(this, "ダイアログアクティビティ");
	}
}

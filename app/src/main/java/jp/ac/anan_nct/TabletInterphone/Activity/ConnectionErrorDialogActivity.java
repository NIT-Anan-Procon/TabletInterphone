package jp.ac.anan_nct.TabletInterphone.Activity;

import jp.ac.anan_nct.TabletInterphone.Dialog.CustomDialog;
import jp.ac.anan_nct.TabletInterphone.Dialog.DialogBuilder;
import android.app.Activity;
import android.os.Bundle;

public class ConnectionErrorDialogActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		showDialog("通信エラー", "通信が切断されました。");
	}
	
	private void showDialog(String title, String message){
		new DialogBuilder(this)
			.setTitle(title)
			.setMessage(message)
			.setPositiveButton("OK", null)
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

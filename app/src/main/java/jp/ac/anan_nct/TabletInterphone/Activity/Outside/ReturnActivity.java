package jp.ac.anan_nct.TabletInterphone.Activity.Outside;

import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.Activity.BaseActivity;
import jp.ac.anan_nct.TabletInterphone.Communication.WifiSocket;
import jp.ac.anan_nct.TabletInterphone.Serializable.MessageSerializable;
import jp.ac.anan_nct.TabletInterphone.Serializable.MessageSerializable.MessageType;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;

public class ReturnActivity extends BaseActivity {
	//onActivityResult()での判別用定数
	private static final int RECOGNIZE_SPEECH = R.layout.activity_outside_return;
	
	private EditText sendEditText;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); //起動時にキーボードを立ち上げない
		setContentView(R.layout.activity_outside_return);

		sendEditText = (EditText)findViewById(R.id.sendEditText);
		
		findViewById(R.id.mikeLayout).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startRecognizeSpeech();
			}
		});
		
		final WifiSocket wifiSocket = sharedVariable.wifiSocket;
		findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String returnBusiness = sendEditText.getText().toString();
				if(!returnBusiness.isEmpty()){
					wifiSocket.writeObject(new MessageSerializable(MessageType.MORE, returnBusiness));
					startTIActivity(CallWaitActivity.class);
				}
			}
		});
		
		sharedVariable.speak("具体的にお話しください。");
	}
	
	/***************************
	 * 音声認識開始
	 *
	 * @param requestCode 音声認識の用途
	 * @return 音声認識アプリケーションがインストールされていない場合、falseを返す。
	 ***************************/
	private boolean startRecognizeSpeech() {
		Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH) // ACTION_WEB_SEARCH
			.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
			.putExtra(RecognizerIntent.EXTRA_PROMPT, "マイクに向かって話してください");

		try{
			startActivityForResult(intent, RECOGNIZE_SPEECH);
		}
		catch (ActivityNotFoundException e) {
			return false;
		}
		return true;
	}

	/***************************
	 * 別アクティビティからの結果
	 ***************************/
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode != RESULT_OK || requestCode != RECOGNIZE_SPEECH)
			return;

		sendEditText.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
	}
}

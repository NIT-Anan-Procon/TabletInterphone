package jp.ac.anan_nct.TabletInterphone.Activity.Outside;

import jp.ac.anan_nct.TabletInterphone.Const;
import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.Activity.BaseActivity;
import jp.ac.anan_nct.TabletInterphone.Communication.WifiSocket;
import jp.ac.anan_nct.TabletInterphone.Serializable.MessageSerializable;
import jp.ac.anan_nct.TabletInterphone.Serializable.WriteSerializable;
import jp.ac.anan_nct.TabletInterphone.Serializable.MessageSerializable.MessageType;
import jp.ac.anan_nct.TabletInterphone.Utility.Util;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class OtherActivity extends BaseActivity {
	//onActivityResult()での判別用定数
	private static final int RECOGNIZE_SPEECH = R.layout.activity_outside_other;
	
	private EditText sendEditText;
	private Button sendButton;
	private Bitmap bit;
	private boolean commandmode = false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE); //起動時にキーボードを立ち上げる
		setContentView(R.layout.activity_outside_other_select);
		sharedVariable.menueFlag = false;

		sendEditText = (EditText)findViewById(R.id.sendEditText);		
		sendButton = (Button)findViewById(R.id.sendButton);
		
		if(Boolean.getBoolean(((SpannableStringBuilder)sendEditText.getText()).toString())){
			sendButton.setEnabled(true);
		}
		
		
			try{
				bit = sharedVariable.getStBmOther();
				sharedVariable.cameraViewOutsideOther = (ImageView)findViewById(R.id.addImageView);
				((ImageView)findViewById(R.id.addImageView)).setImageBitmap(bit);
				
			}
			catch(NullPointerException e){
			
			}
		findViewById(R.id.WriteButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startTIActivity(WriteVisiterActivity.class);
			}
		});
		
		findViewById(R.id.CameraButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startTIActivity(VisiterCameraActivity.class);
			}
		});
		
		findViewById(R.id.VoiceButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				startRecognizeSpeech();
			}
		});
		
		final WifiSocket wifiSocket = sharedVariable.wifiSocket;
		sendButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				String otherBusiness = sendEditText.getText().toString();
				if(otherBusiness.equals("CommandMode")){
					commandmode = !commandmode;
				}
				if(commandmode){
					try{
						sharedVariable.interval = Integer.parseInt(otherBusiness);
					}
					catch(NumberFormatException e){
						
					}
				}
				else{
					if(sharedVariable.resource != null){
						bit = null;
					}
					if(bit != null){
						WriteSerializable writeSerializable = new WriteSerializable(bit);
						sharedVariable.wifiSocket.writeObject(Const.OTHER_FRAG_SET);
						Util.sleep(340);
						sharedVariable.wifiSocket.writeObject(writeSerializable);
						Util.sleep(340);
						wifiSocket.writeObject(new MessageSerializable(MessageType.OTHER, otherBusiness));
						startTIActivity(CallWaitActivity.class);
					}
					else if(!otherBusiness.isEmpty()){
						wifiSocket.writeObject(new MessageSerializable(MessageType.OTHER, otherBusiness));
						startTIActivity(CallWaitActivity.class);
					}
					else if(otherBusiness.isEmpty()){
						sharedVariable.speak("用件を入力してから、送信を押してください。");
					}
				}
			}
		});
		
		findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				sharedVariable.selectBusinessFlag = 0x10;
				startTIActivity(SelectBusinessActivity.class);
			}
		});
		
		sharedVariable.speak("用件をキーボードで入力するか、、右の項目で入力して、、送信、、を押してください。");
		//sharedVariable.speak("用件を音声入力、またはキーボード入力で入力してください。");
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

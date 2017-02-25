package jp.ac.anan_nct.TabletInterphone.Fragment;

import android.view.View;
import android.widget.Button;
import jp.ac.anan_nct.TabletInterphone.Const;
import jp.ac.anan_nct.TabletInterphone.R;

public class VoiceChatFragment extends BaseFragment{
	
	private boolean License = true;
	
	@Override
	protected void onCreate(View view){
		Button duoButton = (Button)view.findViewById(R.id.phoneCallDuoButton);

		duoButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(License == true){
					try{
					License = false;

					sharedVariable.phoneCallStatus(Const.VOICE_CHAT_DUAL);
					sharedVariable.service.endTrackAndRecord();
					sharedVariable.wifiSocket.writeObject(Const.VOICE_CHAT_DUAL);
					sharedVariable.service.startTrackAndRecord(Const.VOICE_CHAT_DUAL);
					
					License = true;
					}
					catch(NullPointerException e){
						License = true;
					}
				}
			}
		});

		
		Button soloButton = (Button)view.findViewById(R.id.phoneCallSoloButton);
		soloButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				if(License == true){
					try{
					License = false;

					sharedVariable.phoneCallStatus(Const.VOICE_CHAT_LISTEN_ONLY);
					sharedVariable.service.endTrackAndRecord();
					sharedVariable.wifiSocket.writeObject(Const.VOICE_CHAT_SPEAK_ONLY);
					sharedVariable.service.startTrackAndRecord(Const.VOICE_CHAT_LISTEN_ONLY);
					
					License = true;
					}
					catch(NullPointerException e){
						License = true;
					}
				}
			}
		});
		
		Button endButton = (Button)view.findViewById(R.id.phoneCallEndButton);
		endButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				try{
					if(License == true){
						License = false;

						sharedVariable.phoneCallStatus(Const.INTERACTION_PHONE_CALL_END);
						sharedVariable.wifiSocket.writeObject(Const.INTERACTION_PHONE_CALL_END);
						sharedVariable.service.endTrackAndRecord();
						
						License = true;
					}
				}
				catch(NullPointerException e){
					License = true;
				}
			}
		});
		
		Button backButton = (Button)view.findViewById(R.id.backButton);
		backButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				changeLeftFragment(new VisiterInteractionFragment());
			}
		});
		
		//sharedVariable.service.startTrackAndRecord();
	}
	
	@Override
	protected int getLayoutId(){
		return R.layout.fragment_voice_chat;
	}
}
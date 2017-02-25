package jp.ac.anan_nct.TabletInterphone.Fragment;

import android.view.View;
import android.widget.Button;
import jp.ac.anan_nct.TabletInterphone.Const;
import jp.ac.anan_nct.TabletInterphone.R;

public class PhoneCallFragment extends BaseFragment{
	@Override
	protected void onCreate(View view){
		Button backButton = (Button)view.findViewById(R.id.backButton);
		backButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				changeLeftFragment(new VisiterInteractionFragment());
			}
		});
		
		sharedVariable.service.startTrackAndRecord();
	}
	
	@Override
	public void onDestroy(){
		sharedVariable.wifiSocket.writeObject(Const.INTERACTION_PHONE_CALL_END);
		sharedVariable.service.endTrackAndRecord();
		
		super.onDestroy();
	}
	
	@Override
	protected int getLayoutId(){
		return R.layout.fragment_phone_call;
	}
}
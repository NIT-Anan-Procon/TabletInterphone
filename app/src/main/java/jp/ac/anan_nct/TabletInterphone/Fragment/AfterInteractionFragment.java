package jp.ac.anan_nct.TabletInterphone.Fragment;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import jp.ac.anan_nct.TabletInterphone.R;

public class AfterInteractionFragment extends BaseFragment{
	@Override
	protected void onCreate(View view){
		String interactionText = getArguments().getString("Interaction");
		if(interactionText != null){
			TextView interactionTextView = (TextView)view.findViewById(R.id.interactionTextView);
			interactionTextView.setText(interactionText);
		}
		
		Button backButton = (Button)view.findViewById(R.id.backButton);
		backButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				changeLeftFragment(new VisiterInteractionFragment());
			}
		});
	}
	
	@Override
	protected int getLayoutId(){
		return R.layout.fragment_after_interaction;
	}
}

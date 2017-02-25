package jp.ac.anan_nct.TabletInterphone.Fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import jp.ac.anan_nct.TabletInterphone.PaintView;
import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.Serializable.WriteSerializable;

public class WriteFragment extends BaseFragment{
	@Override
	protected void onCreate(View view){
		LinearLayout layout = (LinearLayout)getActivity().findViewById(R.id.leftLayout);
		((LinearLayout.LayoutParams)layout.getLayoutParams()).weight = 4;
		sharedVariable.writeStatus = true;
		
		final PaintView paintView = (PaintView)view.findViewById(R.id.paintView);

		view.findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				WriteSerializable writeSerializable = new WriteSerializable(paintView.getBitmap());
				sharedVariable.wifiSocket.writeObject(writeSerializable);
				AfterInteractionFragment fragment = new AfterInteractionFragment();
				Bundle args = new Bundle();
				args.putString("Interaction", "筆談");
				fragment.setArguments(args);
				changeLeftFragment(fragment);
			}
		});
		
		view.findViewById(R.id.delButton).setOnClickListener(new View.OnClickListener(){
		@Override
			public void onClick(View v){
				paintView.clear();
			}
		});
		
		view.findViewById(R.id.backButton).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				LinearLayout layout = (LinearLayout)getActivity().findViewById(R.id.leftLayout);
				((LinearLayout.LayoutParams)layout.getLayoutParams()).weight = 1;
				changeLeftFragment(new VisiterInteractionFragment());
			}
		});
	}
	
	@Override
	public void onPause() {
		sharedVariable.writeStatus = false;
		LinearLayout layout = (LinearLayout)getActivity().findViewById(R.id.leftLayout);
		((LinearLayout.LayoutParams)layout.getLayoutParams()).weight = 1;
		
		super.onPause();
	}

	@Override
	protected int getLayoutId(){
		return R.layout.fragment_write;
	}
}
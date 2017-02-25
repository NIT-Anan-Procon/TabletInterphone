package jp.ac.anan_nct.TabletInterphone.Fragment;

import jp.ac.anan_nct.TabletInterphone.SharedVariable;
import jp.ac.anan_nct.TabletInterphone.Activity.Inside.VisiterCheckActivity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

abstract public class BaseFragment extends Fragment{
	protected SharedVariable sharedVariable;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		sharedVariable = (SharedVariable)getActivity().getApplicationContext();
		View view = inflater.inflate(getLayoutId(), container, false);
		onCreate(view);
		return view;
	}
	
	abstract protected int getLayoutId();
	
	protected void onCreate(View view){}
	
	protected void changeLeftFragment(Fragment fragment){
		((VisiterCheckActivity)getActivity()).changeLeftFragment(fragment);
	}
	
}

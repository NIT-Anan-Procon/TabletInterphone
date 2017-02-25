package jp.ac.anan_nct.TabletInterphone.Fragment;

import android.view.View;
import android.widget.ImageView;
import jp.ac.anan_nct.TabletInterphone.R;

public class CameraViewFragment extends BaseFragment{
	@Override
	protected void onCreate(View view){
		sharedVariable.cameraView = (ImageView)view.findViewById(R.id.cameraView);
	}
	
	@Override
	protected int getLayoutId(){
		return R.layout.fragment_camera_view;
	}
}
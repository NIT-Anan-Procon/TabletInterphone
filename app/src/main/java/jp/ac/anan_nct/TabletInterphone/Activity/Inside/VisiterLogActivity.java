package jp.ac.anan_nct.TabletInterphone.Activity.Inside;

import jp.ac.anan_nct.TabletInterphone.Const;
import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.Activity.BaseActivity;
import jp.ac.anan_nct.TabletInterphone.Utility.Util;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


public class VisiterLogActivity extends BaseActivity {
	
	private Button backButton;
	
	private void LogFileRead(ArrayList<TextView> array){

		String s;
		
		String filePath = Getfilename();
		
		//String filePath = Environment.getExternalStorageDirectory() +"/TILog/" +"/" + ("2016@11.txt");
		File file = new File(filePath);
		file.getParentFile().mkdir();
		
		FileInputStream fis;
		BufferedReader in = null;
		try{
			fis = new FileInputStream(file);
			in = new BufferedReader(new InputStreamReader(fis));
			while((s = in.readLine()) != null){
				addLogList(array, s);
			}
			in.close();
			fis.close();		
		}catch(IOException e){
		}
		
	}
	
	private void addLogList(ArrayList<TextView> array, String data){
		TextView logText = new TextView(this);
		
		logText.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		
		logText.setText(data);
		array.add(logText);
		Log.d("LOGVL","LOGVL03" + logText.toString());
	}
	
	protected String Getfilename(){
		String filePath =Environment.getExternalStorageDirectory() +"/TILog/" +"/" + (String.valueOf(sharedVariable.getLogYear()) + "@" + String.valueOf(sharedVariable.getLogMonth()) + ".txt");
		return filePath;
	}
	
	private void createLogList(){
		LinearLayout logLayout = (LinearLayout)findViewById(R.id.logLayout);
		
		ArrayList<TextView> logArray = new ArrayList<TextView>();
		LogFileRead(logArray);
		
		Log.d("LOGVL","LOGVL01" + logArray.size());
		try{
		for(int i = 0; logArray.size() > i;i++){
			logLayout.addView(logArray.get(i));
			Log.d("LOGVL","LOGVL02" + i);
		}
		}catch(Exception e){
			Log.d("LOGVL","LOGVL05");
		}
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		boolean manekoFlag = false;
		try{
			manekoFlag = intent.getStringExtra("maneko").equals("maneko");
		}
		catch(Exception e){
			
		}
		if(manekoFlag){
			sharedVariable.wifiSocket.writeObject(Const.INTERACTION_LIGHT_ON);
			startTIActivity(VisiterCheckActivity.class);
		}
		else{
		setContentView(R.layout.activity_inside_log_visiter);
		createLogList();
		
		backButton = (Button) findViewById(R.id.Button_log_back);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startTIActivity(InsideConnectSettingActivity.class);
			}
		});
		}
	}
	
}
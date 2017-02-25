package jp.ac.anan_nct.TabletInterphone.Activity.Inside;

import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.SharedVariable;
import jp.ac.anan_nct.TabletInterphone.Activity.BaseActivity;
import jp.ac.anan_nct.TabletInterphone.Utility.Util;
import android.os.Bundle;

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
import android.widget.NumberPicker;
import android.widget.TextView;


public class LogListActivity extends BaseActivity {
	
	private NumberPicker NumYear;
	private NumberPicker NumMonth;
	
	private Button backButton;
	private Button enterButton;
	
	private Calendar calendar = Calendar.getInstance();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.log_list);
		NumYear = (NumberPicker) findViewById(R.id.numberPickerYear);
		NumYear.setMaxValue(2525);
		NumYear.setMinValue(1867);
		NumYear.setValue(calendar.get(Calendar.YEAR));
		
		NumMonth = (NumberPicker) findViewById(R.id.numberPickerMonth);
		NumMonth.setMaxValue(12);
		NumMonth.setMinValue(1);
		NumMonth.setValue(calendar.get(Calendar.MONTH) + 1); 
		
		enterButton = (Button) findViewById(R.id.Button_log_enter);
		enterButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sharedVariable.setLogTime(NumYear.getValue(),NumMonth.getValue());
				startTIActivity(VisiterLogActivity.class);
			}
		});
		
		backButton = (Button) findViewById(R.id.Button_log_back);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startTIActivity(InsideConnectSettingActivity.class);
			}
		});
	}
	
}
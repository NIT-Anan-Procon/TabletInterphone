package jp.ac.anan_nct.TabletInterphone.Activity.Outside;

import java.util.ArrayList;

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
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WhoActivity extends BaseActivity {

    private EditText sendEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outside_who);

        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);

        createResidentList();

    }

    private void createResidentList() {
        LinearLayout whoLayout = (LinearLayout) findViewById(R.id.whoLayout);

        ArrayList<Button> residentArray = new ArrayList<Button>();
        addResidentList(residentArray, "だれでも");
        addResidentList(residentArray, "父");
        addResidentList(residentArray, "母");
        addResidentList(residentArray, "息子");
        addResidentList(residentArray, "娘");

        for (int i = 0; residentArray.size() > i; i++) {
            whoLayout.addView(residentArray.get(i));
        }

    }

    private void addResidentList(ArrayList<Button> resArray, String name) {
        Button residentname = new Button(this);

        residentname.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));

        residentname.setText(name);
        resArray.add(residentname);
    }

}

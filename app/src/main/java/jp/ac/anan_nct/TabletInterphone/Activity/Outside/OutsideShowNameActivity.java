package jp.ac.anan_nct.TabletInterphone.Activity.Outside;

import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.SharedVariable;
import jp.ac.anan_nct.TabletInterphone.Activity.BaseActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.Inside.VisiterCheckActivity;
import jp.ac.anan_nct.TabletInterphone.Fragment.CameraViewFragment;
import jp.ac.anan_nct.TabletInterphone.Fragment.OutsideCameraViewFragment;
import jp.ac.anan_nct.TabletInterphone.Fragment.VisiterInteractionFragment;
import jp.ac.anan_nct.TabletInterphone.Serializable.MessageSerializable;
import jp.ac.anan_nct.TabletInterphone.Serializable.WriteSerializable;
import jp.ac.anan_nct.TabletInterphone.Serializable.MessageSerializable.MessageType;
import jp.ac.anan_nct.TabletInterphone.Utility.Util;

import android.annotation.SuppressLint;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Handler;

import jp.ac.anan_nct.TabletInterphone.Const;


public class OutsideShowNameActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outside_name_check);

        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);

        Button BackButton = (Button) findViewById(R.id.Button_back);

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        //ft.replace(R.id.leftFragment, new VisiterInteractionFragment());
        ft.replace(R.id.rightFragment, new OutsideCameraViewFragment());
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();

        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedVariable.wifiSocket.writeObject(new MessageSerializable(MessageType.OTHER, "持ってない"));
                sharedVariable.selectBusinessFlag = 0x10;
                startTIActivity(SelectBusinessActivity.class);
            }
        });
        BackButton.setText("持ってない");
        sharedVariable.speak("名札を見せて、撮影ボタンを押してください");
        findViewById(R.id.Button_namecheck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sharedVariable.setStBm(sharedVariable.getoutBm());

                try {
                    WriteSerializable writeSerializable = new WriteSerializable(sharedVariable.getStBm());
                    sharedVariable.wifiSocket.writeObject(Const.CAMERA_FRAG_SET);
                    Util.sleep(34);
                    sharedVariable.wifiSocket.writeObject(writeSerializable);
                    Util.sleep(34);
                    sharedVariable.speak("撮影しました。少々お待ちください。");
                    final TextView Ctext = (TextView) findViewById(R.id.textViewCamera);
                    Ctext.setText("撮影しました。少々お待ちください。");
                    Ctext.setTextColor(Color.RED);
                } catch (Exception e) {
                    return;
                }
            }
        });


    }

    protected String getTIActivity() {
        return "OutsideShowNameActivity";
    }

    @Override
    protected void startTIActivity(Intent intent) {
        super.startTIActivity(intent);
    }

}

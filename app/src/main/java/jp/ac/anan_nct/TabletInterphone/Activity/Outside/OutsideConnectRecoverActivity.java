package jp.ac.anan_nct.TabletInterphone.Activity.Outside;

import jp.ac.anan_nct.TabletInterphone.Const;

import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.SharedVariable;
import jp.ac.anan_nct.TabletInterphone.Activity.StartActivity;
import jp.ac.anan_nct.TabletInterphone.Communication.WifiSocket;
import jp.ac.anan_nct.TabletInterphone.Dialog.CustomDialog;
import jp.ac.anan_nct.TabletInterphone.Dialog.DialogBuilder;
import jp.ac.anan_nct.TabletInterphone.Service.OutsideService;
import jp.ac.anan_nct.TabletInterphone.Utility.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Layout;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.util.Log;

public class OutsideConnectRecoverActivity extends Activity {
    private static final Handler handler = new Handler();

    private Button wifiSettingButton;
    private Button startButton;


    private WifiSocket ws;
    private SharedVariable sh;


    private String ipAddressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outside_conect_retry);

        wifiSettingButton = (Button) findViewById(R.id.wifiSettingButton);
        startButton = (Button) findViewById(R.id.endPhoneCallButton);
        sh = (SharedVariable) this.getApplication();

        wifiSettingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIpAddressDialog();
            }
        });

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        wifiSettingButton.setVisibility(View.INVISIBLE);
        startButton.setVisibility(View.INVISIBLE);


        wifiSettingButton.setText("終了");
        this.ipAddressText = sh.sharedIPadressText;
        sh.selectBusinessFlag = 0x00;

        //Intent disConnect = new Intent("WiFiDisConnection");
        //sendcast(disConnect);

        startConnect();
        Log.d("OCRA", "OCRA03");
    }

    /***************************
     * Wi-Fiの通信確立
     ***************************/
    private void startConnect() {
        ws = new WifiSocket();
        Log.d("OCRA", "OCRA06");
        /*final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("接続中");
        progressDialog.setMessage("Wi-Fi接続 : 接続中");
        progressDialog.setCancelable(true);
        progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "キャンセル", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                ws.stopConnect();
                Log.d("OCRA", "OCRA07");
                showErrorDialog("Wi-Fi接続に失敗しました");
            }
        });
        progressDialog.show();*/
        Log.d("OCRA", "OCRA08");

        // Wi-Fi接続スレッド
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (ws.connectToServer(ipAddressText)) {
                    Log.d("Ouc", "El01" + ipAddressText);
                    //progressDialog.dismiss();
                    Log.d("connect", "connect01");
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            startOutside();
                        }
                    });
                } else {
                    Log.d("OCRA", "OCRA09");
                    //progressDialog.dismiss();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            restartConnect();
                        }
                    });
                }
            }
        }).start();
    }

    private void restartConnect() {
        Log.d("OCRA", "OCRA04");
        ws.stopConnect();
        Util.sleep(4000);
        startConnect();
    }

    private void startOutside() {
        SharedVariable sh = Util.getSharedVariable(this);
        sh.wifiSocket = this.ws;
        Log.d("OCRA", "OCRA05");
        startService(new Intent(this, OutsideService.class));
        finish();

        //startActivity(new Intent(this, SelectBusinessActivity.class));
    }

    /***************************
     * IPアドレス設定ダイアログ表示
     ***************************/
    private void showIpAddressDialog() {
        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_ipaddress, null);
        final NumberPicker[] ipAddressPicker = new NumberPicker[4];
        ipAddressPicker[0] = (NumberPicker) view.findViewById(R.id.ipAddressPicker1);
        ipAddressPicker[1] = (NumberPicker) view.findViewById(R.id.ipAddressPicker2);
        ipAddressPicker[2] = (NumberPicker) view.findViewById(R.id.ipAddressPicker3);
        ipAddressPicker[3] = (NumberPicker) view.findViewById(R.id.ipAddressPicker4);
        for (int i = 0; i < ipAddressPicker.length; i++) {
            ipAddressPicker[i].setMaxValue(255);
            ipAddressPicker[i].setMinValue(0);
        }

        ipAddressPicker[0].setValue(192);
        ipAddressPicker[1].setValue(168);
        ipAddressPicker[2].setValue(111);
        ipAddressPicker[3].setValue(101);

        new DialogBuilder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("Wi-Fi接続設定")
                .setView(view)
                .setNegativeButton("キャンセル", null)
                .setPositiveButton("決定", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int ip1 = ipAddressPicker[0].getValue();
                        int ip2 = ipAddressPicker[1].getValue();
                        int ip3 = ipAddressPicker[2].getValue();
                        int ip4 = ipAddressPicker[3].getValue();
                        ipAddressText = Util.getIpAddressText(ip1, ip2, ip3, ip4);
                        sh.sharedIPadressText = ipAddressText;
                        startButton.setEnabled(true);
                        wifiSettingButton.setText("Wi-Fi設定\n\n" + ipAddressText);
                    }
                }).show("Wi-Fi接続設定");
    }

    /***************************
     * 物理キー押下時
     ***************************/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(new Intent(this, StartActivity.class));
            finish();
            return true;
        } else
            return super.onKeyDown(keyCode, event);
    }

    private void showErrorDialog(String message) {
        DialogBuilder.showErrorDialog(this, message);
    }
}

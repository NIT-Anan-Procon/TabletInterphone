package jp.ac.anan_nct.TabletInterphone.Activity.Inside;

import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.Dialog.DialogBuilder;
import jp.ac.anan_nct.TabletInterphone.Serializable.MessageSerializable;
import jp.ac.anan_nct.TabletInterphone.Serializable.MessageSerializable.MessageType;
import jp.ac.anan_nct.TabletInterphone.SharedVariable;
import jp.ac.anan_nct.TabletInterphone.Utility.Util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

public class InsideSettingActivity extends Activity {

    SharedVariable sharedVariable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_inside_setting);

        sharedVariable= Util.getSharedVariable(this);

        findViewById(R.id.patliteControlLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelectModeDialog();
            }
        });

        findViewById(R.id.absenceMessageLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeAbsenceMessage();
            }
        });
    }

    private void showSelectModeDialog() {
        new DialogBuilder(this)
                .setTitle("設定を変更するモードを選択してください")
                .setItems(new String[]{"在宅モード", "夜間モード"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0)
                            startActivity(InsideHomeSettingActivity.class);
                        else
                            startActivity(InsideNightSettingActivity.class);
                    }
                })
                .setCanceledOnTouchOutside(false)
                .create()
                .show(this, "モード選択ダイアログ");
    }

    private void makeAbsenceMessage() {
        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_absencemessage, null);
        final EditText absenceMessage = (EditText) view.findViewById(R.id.absenceEditText);

        new DialogBuilder(this)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle("留守メッセージ登録")
                .setView(view)
                .setNegativeButton("キャンセル", null)
                .setPositiveButton("決定", new AlertDialog.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String abcenceBusiness ="no message";
                        if (!abcenceBusiness.isEmpty()) {
                            abcenceBusiness = absenceMessage.getText().toString();
                            sharedVariable.wifiSocket.writeObject(new MessageSerializable(MessageType.ABSENCE, abcenceBusiness));
                            showDialog("メッセージ登録完了", "留守メッセージを設定しました");
                        }
                    }
                }).show("留守メッセージ登録");
    }

    private void showDialog(String title, String message) {
        new DialogBuilder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .setCanceledOnTouchOutside(false)
                .create()
                .show(this, "留守メッセージ登録ダイアログ");
    }

    private void startActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
    }
}

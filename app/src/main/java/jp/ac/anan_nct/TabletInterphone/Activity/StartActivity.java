package jp.ac.anan_nct.TabletInterphone.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;

import jp.ac.anan_nct.TabletInterphone.Activity.Inside.InsideConnectSettingActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.Outside.OutsideConnectSettingActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.Outside.SelectBusinessActivity;
import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.SharedVariable;

public class StartActivity extends Activity {

    private SharedVariable sh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.RECORD_AUDIO,
                android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        sh = (SharedVariable) this.getApplication();

        if (sh.isOutside == true) {
            sh.selectBusinessFlag = 0x00;
            startActivity(SelectBusinessActivity.class);
        }

        setContentView(R.layout.activity_start);

        Button insideButton = (Button) findViewById(R.id.insideButton);
        Button outsideButton = (Button) findViewById(R.id.outsideButton);

        insideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(InsideConnectSettingActivity.class);
            }
        });

        outsideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(OutsideConnectSettingActivity.class);
            }
        });


    }

    private void startActivity(Class<?> cls) {
        startActivity(new Intent(this, cls));
        finish();
    }
}

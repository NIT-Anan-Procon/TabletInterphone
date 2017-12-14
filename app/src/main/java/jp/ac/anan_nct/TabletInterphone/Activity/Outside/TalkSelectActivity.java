package jp.ac.anan_nct.TabletInterphone.Activity.Outside;

import jp.ac.anan_nct.TabletInterphone.Const;
import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.Activity.BaseActivity;

import android.os.Bundle;
import android.view.View;


public class TalkSelectActivity extends BaseActivity {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outside_selecting);

        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);

        findViewById(R.id.select_speak_Layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTIActivity(OtherActivity.class);
            }
        });

        findViewById(R.id.selact_write_Layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startTIActivity(WriteVisiterActivity.class);
            }
        });

    }

}

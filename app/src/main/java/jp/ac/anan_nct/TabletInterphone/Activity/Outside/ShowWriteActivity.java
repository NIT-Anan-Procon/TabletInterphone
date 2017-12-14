package jp.ac.anan_nct.TabletInterphone.Activity.Outside;

import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.Activity.BaseActivity;
import jp.ac.anan_nct.TabletInterphone.Serializable.WriteSerializable;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ShowWriteActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outside_show_write);

        View decor = this.getWindow().getDecorView();
        decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);

        Bitmap bitmap = ((WriteSerializable) getIntent().getSerializableExtra("WriteImage")).getBitmap();
        ((ImageView) findViewById(R.id.writeImageView)).setImageBitmap(bitmap);

        findViewById(R.id.returnButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //0927変更
                //startTIActivity(ReturnActivity.class);
                startTIActivity(OtherActivity.class);
            }
        });
    }
}

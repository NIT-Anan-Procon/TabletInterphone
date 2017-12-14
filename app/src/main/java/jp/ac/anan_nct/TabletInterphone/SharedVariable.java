package jp.ac.anan_nct.TabletInterphone;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import jp.ac.anan_nct.TabletInterphone.Activity.BaseActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.StartActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.Inside.VisiterCheckActivity;
import jp.ac.anan_nct.TabletInterphone.Activity.Outside.OutsidePhoneCallActivity;
import jp.ac.anan_nct.TabletInterphone.Communication.BluetoothConnection;
import jp.ac.anan_nct.TabletInterphone.Communication.WifiSocket;
import jp.ac.anan_nct.TabletInterphone.Fragment.VisiterInteractionFragment;
import jp.ac.anan_nct.TabletInterphone.Serializable.WriteSerializable;
import jp.ac.anan_nct.TabletInterphone.Service.BaseService;
import jp.ac.anan_nct.TabletInterphone.Utility.Util;

import android.app.Activity;
import android.app.Application;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.ToneGenerator;
import android.net.Uri;
import android.os.Environment;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class SharedVariable extends Application {
    public BluetoothConnection bluetoothConnection;
    public BluetoothConnection keyBoxBluetoothConnection;
    public BluetoothDevice targetDevice = null;
    public WifiSocket wifiSocket;
    public ImageView cameraView;
    public ImageView cameraViewOutside;
    public ImageView cameraViewOutsideOther;

    public BaseService service;
    public OutsidePhoneCallActivity outsidePhoneCallActivity;
    //private BaseActivity currentTIActivity;
    public VisiterCheckActivity visiterCheckActivity;

    private TextToSpeech textToSpeech;

    //// 5/16追加////////////////////////
    public VisiterInteractionFragment ViInFr;
    public BaseActivity currentTIActivity;
    public Activity startActivity;
    private int LogYear;
    private int LogMonth;
    public int TurnTime;
    public Bitmap outBm;
    public Bitmap StBm;
    public byte[] StBmOther;
    private int CameraFrag = 0;
    private int OtherFrag = 0;
    private WriteSerializable Wserial;
    private WriteSerializable WserialOther;
    private ImageView img;
    public Resources resource;
    public int selectBusinessFlag = 0x00;
    private Uri uri;
    public Ringtone ringtone;
    public Window shareWindow;
    public boolean keyGuardStatus = false;
    public boolean writeStatus = false;
    public boolean isOutside = false;
    public String sharedIPadressText;
    public int interval = 0;
    public ArrayList<String> ImgArray = new ArrayList<String>();
    public boolean menueFlag = true;
    public boolean vcFlag = false;
    public int selectBusiness = Const.BUSINESS_OTHER;
    public String absenceMessage = "no message";

    private int Status = 0x020005;


    ////////////////////////////////////////////////////////////////
    //9/30追加
    //通話状態記録メソッド
    ///////////////////////////////////////////////////////////////
    public int phoneCallStatus(int status) {
        if (status != 0) {
            Status = status;
        }
        return Status;
    }
    ///////////////////////////////////////////////////////////////


    public synchronized void sleep(long msec) {
        try {
            wait(msec);
        } catch (InterruptedException e) {
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //屋外側ボタン設定
    //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void InitialImgArray() {
        ImgArray.add(String.valueOf(R.drawable.delivery_service) + ",.宅配・郵便,." + String.valueOf(Const.BUSINESS_DELIVERY_SERVICE));
        ImgArray.add(String.valueOf(R.drawable.check) + ",.水道・ガス点検,." + String.valueOf(Const.BUSINESS_CHECK));
        ImgArray.add(String.valueOf(R.drawable.money) + ",.集金,." + String.valueOf(Const.BUSINESS_MONEY_COLLECT));
        ImgArray.add(String.valueOf(R.drawable.refp) + ",.遊びに来た,." + String.valueOf(Const.BUSINESS_NEIGHBOR));
        ImgArray.add(String.valueOf(R.drawable.sales) + ",.訪問販売,." + String.valueOf(Const.BUSINESS_SALES));
        ImgArray.add(String.valueOf(R.drawable.comu) + ",.地域活動・回覧板,." + String.valueOf(Const.BUSINESS_CIRCULAR_NOTICE));
        ImgArray.add(String.valueOf(R.drawable.republic) + ",.県・市町村,." + String.valueOf(Const.BUSINESS_CITY_SERVICE));
        //ImgArray.add(String.valueOf(R.drawable.reds) + ",.地域活動,." + String.valueOf(Const.BUSINESS_NEIGHBOR));
        ImgArray.add(String.valueOf(R.drawable.redays) + ",.送迎,." + String.valueOf(Const.BUSINESS_DAY_SERVICE));
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void initialRingtone(Context context) {
        uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(context, uri);
    }

    public void setWserial(WriteSerializable i) {
        Wserial = i;
    }

    public WriteSerializable getWserial() {
        return Wserial;
    }

    public void setWserialOther(WriteSerializable i) {
        WserialOther = i;
    }

    public WriteSerializable getWserialOther() {
        return WserialOther;
    }

    public void setCameraFrag() {
        CameraFrag = 1;
    }

    public void resetCameraFrag() {
        CameraFrag = 0;
    }

    public int getOtherFrag() {
        return OtherFrag;
    }

    public void setOtherFrag() {
        OtherFrag = 1;
    }

    public void resetOtherFrag() {
        OtherFrag = 0;
    }

    public int getCameraFrag() {
        return CameraFrag;
    }


    public void setStBm(Bitmap bitmap) {
        StBm = bitmap;
    }

    public Bitmap getStBm() {
        if (StBm != null) {
            return StBm;
        } else {
            return Bitmap.createBitmap(1, 1, Config.RGB_565);
        }
    }

    public void setStBmOther(Bitmap bitmap) {
        try {
            resource = null;
            StBmOther = Util.bitmapEncode(bitmap);
        } catch (NullPointerException e) {
            resource = getResources();
            StBmOther = Util.bitmapEncode(BitmapFactory.decodeResource(resource, R.drawable.message));

        }
    }

    public Bitmap getStBmOther() {
        try {
            return Util.bitmapDecode(StBmOther);
        } catch (NullPointerException e) {

            return null;
        }
    }

    public void setoutBm(Bitmap bitmap) {
        outBm = bitmap;
    }

    public Bitmap getoutBm() {
        return outBm;
    }

    public void setTurnActivity() {
        TurnTime = 120;
    }

    public void setTurnActivity(int num) {
        TurnTime = num;
    }

    public void setLogTime(int year, int month) {
        LogYear = year;
        LogMonth = month;
    }

    public int getLogYear() {
        return LogYear;
    }

    public int getLogMonth() {
        return LogMonth;
    }

    public void ChangeButtonColor(Button button) {
        try {
            button.setBackgroundColor(Color.GREEN);
        } catch (NullPointerException e) {

        }
    }

////////////////////////////////////

    public void setCurrentTIActivity(BaseActivity activity) {
        this.currentTIActivity = activity;
    }

    public synchronized void startTIActivity(Intent intent) {
        if (currentTIActivity != null)
            currentTIActivity.finish();

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void speak(final String message) {
        if (textToSpeech == null) {
            textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    textToSpeech.setLanguage(Locale.JAPANESE);
                    textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
                }
            });
            return;
        } else if (textToSpeech.isSpeaking()) {
            textToSpeech.stop();
        }
        Log.d("speech", "speech" + message);
        textToSpeech.speak(message, TextToSpeech.QUEUE_FLUSH, null);
    }

    public void textToSpeechShutdown() {
        if (textToSpeech != null) {
            if (textToSpeech.isSpeaking()) {
                textToSpeech.stop();
            }
            Log.d("speech", "speechdown");
            textToSpeech.shutdown();
        }
    }

    public void writeReaction(String text) {
        OutputStream out;
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);

        String filePath = Environment.getExternalStorageDirectory() + "/TILog/" + "/" + (year + "@" + month + ".txt");
        File file = new File(filePath);
        file.getParentFile().mkdir();

        FileOutputStream fos;

        try {
            fos = new FileOutputStream(file, true);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(" > " + text);
            bw.flush();
            bw.close();

        } catch (Exception e) {
        }

        try {
            out = openFileOutput((year + "@" + month + ".txt"), MODE_PRIVATE | MODE_APPEND);
            PrintWriter TIwriter = new PrintWriter(new OutputStreamWriter(out, "UTF-8"));
            TIwriter.append(" > " + text);
            TIwriter.close();
        } catch (IOException e) {
            Log.d("VisChe", "writeerror");
        }
    }
}

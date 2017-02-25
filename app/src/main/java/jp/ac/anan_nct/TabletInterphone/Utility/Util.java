package jp.ac.anan_nct.TabletInterphone.Utility;

import java.io.ByteArrayOutputStream;
import java.util.Locale;

import jp.ac.anan_nct.TabletInterphone.Const;
import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.SharedVariable;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.Service;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;

public class Util {
	/***************************
	 * スリープ処理
	 * 
	 * @param ms スリープ時間
	 ***************************/
	public static void sleep(int ms){
		try {
			Thread.sleep(ms);
		}
		catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/***************************
	 * 変数共有用のSharedVariableインスタンスを取得
	 * 
	 * @param activity 取得に用いるActivity
	 ***************************/
	public static SharedVariable getSharedVariable(Activity activity){
		return (SharedVariable)activity.getApplication();
	}
	
	/***************************
	 * 変数共有用のSharedVariableインスタンスを取得
	 * 
	 * @param service 取得に用いるService
	 ***************************/
	public static SharedVariable getSharedVariable(Service service){
		return (SharedVariable)service.getApplication();
	}
	
	/***************************
	 * IPアドレスをテキストに整形
	 * 
	 * @param ip1 IPアドレスの1オクテット目
	 * @param ip2 IPアドレスの2オクテット目
	 * @param ip3 IPアドレスの3オクテット目
	 * @param ip4 IPアドレスの4オクテット目
	 ***************************/
	public static String getIpAddressText(int ip1, int ip2, int ip3, int ip4){
		return String.format(Locale.JAPANESE, "%d.%d.%d.%d", ip1, ip2, ip3, ip4);
	}
	
	/***************************
	 * Bitmapをbyte配列にエンコード
	 * 
	 * @param bitmap エンコードするBitmap
	 ***************************/
	public static final byte[] bitmapEncode(Bitmap bitmap){
		//Bitmapをjpegデータのbyte配列に変換
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(CompressFormat.JPEG, 30, baos);
		return baos.toByteArray();
		
		//ByteArrayOutputStreamは、flushもcloseも不要
	}
	
	/***************************
	 * byte配列をBitmapにデコード
	 * 
	 * @param bitmapBytes デコードするbyte配列
	 ***************************/
	public static final Bitmap bitmapDecode(byte[] bitmapBytes){
		return BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
	}
	
	/***************************
	 * Notification.Builderから、使用しているAndroidバージョンに適した方法でNotificationを取得する。
	 * 
	 * @param builder Notificationを取得したいNotification.Builder
	 * @return 取得したNotificationを返す。
	 ***************************/
	@SuppressLint("NewApi")
	@SuppressWarnings("deprecation")
	public static Notification getNotification(Notification.Builder builder){
		// Androidバージョンによって生成方法を分ける、getNotification()メソッドはAPI16から非推奨
		if(VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN)
			return builder.build();
		else
			return builder.getNotification();
	}
	
	public static int getBusinessImageId(int businessId){
		switch(businessId){
		case Const.BUSINESS_DELIVERY_SERVICE:
			return R.drawable.delivery_service;
		case Const.BUSINESS_CIRCULAR_NOTICE:
			return R.drawable.circular_notice;
		case Const.BUSINESS_MONEY_COLLECT:
			return R.drawable.money;
		case Const.BUSINESS_SALES:
			return R.drawable.sales;
		case Const.BUSINESS_CHECK:
			return R.drawable.check;
		case Const.BUSINESS_OTHER:
			return R.drawable.other;
		case Const.BUSINESS_CITY_SERVICE:
			return R.drawable.republic;
		case Const.BUSINESS_NEIGHBOR:
			return R.drawable.refp;
		case Const.BUSINESS_DAY_SERVICE:
			return R.drawable.redays;
		case Const.BUSINESS_COMUNITY:
			return R.drawable.reds;
		case Const.BUSINESS_OTHER_SECOND:
			return R.drawable.other;
		default:
			return -1;
		}
	}
	
	public static String getBusinessText(int businessId){
		switch(businessId){
		case Const.BUSINESS_DELIVERY_SERVICE:
			return "宅配・郵便";
		case Const.BUSINESS_CIRCULAR_NOTICE:
			return "地域活動・回覧板";
		case Const.BUSINESS_MONEY_COLLECT:
			return "集金";
		case Const.BUSINESS_SALES:
			return"訪問販売";
		case Const.BUSINESS_CHECK:
			return "水道・ガス点検";
		case Const.BUSINESS_OTHER:
			return "その他(詳細入力中)";
		case Const.BUSINESS_CITY_SERVICE:
			return "県・市町村";
		case Const.BUSINESS_NEIGHBOR:
			return "遊びに来た";
		case Const.BUSINESS_DAY_SERVICE:
			return "送迎";
		case Const.BUSINESS_COMUNITY:
			return "地域活動";
		case Const.BUSINESS_OTHER_SECOND:
			return "その他(詳細入力中)";
		default:
			return null;
		}
	}
}

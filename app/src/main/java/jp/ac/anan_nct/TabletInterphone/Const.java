package jp.ac.anan_nct.TabletInterphone;

public class Const{
	public static final int BLUETOOTH_JUDGE_MASK =	0xE0; //判別ビットマスク
	public static final int BLUETOOTH_DATA_MASK =	0x1F; //データビットマスク
	
	public static final int BLUETOOTH_JUDGE_CANCEL_VISIT =	0x00; //判別ビット(訪問・キャンセル)
	public static final int BLUETOOTH_JUDGE_MODE_SELECT =	0x20; //判別ビット(モード選択)
	public static final int BLUETOOTH_JUDGE_HOME_TURNON =	0x40; //判別ビット(在宅モード点灯時間)
	public static final int BLUETOOTH_JUDGE_HOME_TURNOFF =	0x60; //判別ビット(在宅モード消灯時間)
	public static final int BLUETOOTH_JUDGE_HOME_REPEAT =	0x80; //判別ビット(在宅モード繰り返し時間)
	public static final int BLUETOOTH_JUDGE_NIGHT_TURNON =	0xA0; //判別ビット(夜間モード点灯時間)
	public static final int BLUETOOTH_JUDGE_NIGHT_TURNOFF =	0xC0; //判別ビット(夜間モード消灯時間)
	public static final int BLUETOOTH_JUDGE_NIGHT_REPEAT =	0xE0; //判別ビット(夜間モード繰り返し時間)
	
	public static final int BLUETOOTH_VISIT =			BLUETOOTH_JUDGE_CANCEL_VISIT | 1; //訪問
	public static final int BLUETOOTH_CANCEL =			BLUETOOTH_JUDGE_CANCEL_VISIT | 2; //キャンセル
	public static final int BLUETOOTH_MODE_HOME =		BLUETOOTH_JUDGE_MODE_SELECT | 0;  //在宅モード
	public static final int BLUETOOTH_MODE_OUTDOOR =	BLUETOOTH_JUDGE_MODE_SELECT | 1;  //外出モード
	public static final int BLUETOOTH_MODE_NIGHT =		BLUETOOTH_JUDGE_MODE_SELECT | 2;  //夜間モード

	
	//用件
	public static final int BUSINESS_DELIVERY_SERVICE	= 0x010001; //宅配便
	public static final int BUSINESS_CIRCULAR_NOTICE	= 0x010002; //回覧板
	public static final int BUSINESS_MONEY_COLLECT		= 0x010003; //集金
	public static final int BUSINESS_SALES				= 0x010004; //訪問販売
	public static final int BUSINESS_CHECK				= 0x010005; //点検
	public static final int BUSINESS_OTHER				= 0x010006; //その他
	public static final int BUSINESS_PRE_SELECT			= 0x010007; //選択中
	public static final int BUSINESS_CITY_SERVICE		= 0x010008; //市町村
	public static final int BUSINESS_NEIGHBOR			= 0x010009; //お土産
	public static final int BUSINESS_DAY_SERVICE		= 0x01000A; //介護施設
	public static final int BUSINESS_COMUNITY			= 0x01000B; //地域活動
	public static final int BUSINESS_OTHER_SECOND		= 0x01000C; //その他2
	public static final int BUSINESS_PUSH_BELL			= 0x01000D; //選択中
	
	
	
	//対応
	public static final int INTERACTION_WAIT 			= 0x020001; //少々お待ちください
	public static final int INTERACTION_GET_OUT 		= 0x020002; //結構です
	public static final int INTERACTION_MORE 			= 0x020003; //詳しく
	public static final int INTERACTION_PHONE_CALL 		= 0x020004; //通話
	public static final int INTERACTION_PHONE_CALL_END 	= 0x020005; //通話終了
	public static final int INTERACTION_SHOW_NAME 		= 0x020006; //名刺
	public static final int INTERACTION_FINISH	 		= 0x020007; //応対終了
	public static final int INTERACTION_CONNECTION_CHECK= 0x020008; //通信確認
	public static final int INTERACTION_CAMERA_ON		= 0x020009; //カメラON
	public static final int INTERACTION_STOP			= 0x02000A; //停止
	public static final int INTERACTION_CAMERA_IDLE		= 0x02000B; //カメラIDLE
	public static final int INTERACTION_CAMERA_OFF		= 0x02000C; //カメラOFF
	public static final int INTERACTION_LIGHT_ON		= 0x02000D; //カメラOFF
	
	
	//フラグ
	public static final int CAMERA_FRAG_SET 			= 0x080001; //屋外画像送信
	public static final int OTHER_FRAG_SET				= 0x080002; //その他画像送信
	public static final int VOICE_CHAT_DUAL				= 0x080004; //双方向音声通話
	public static final int VOICE_CHAT_LISTEN_ONLY		= 0x080005; //聞くのみ
	public static final int VOICE_CHAT_SPEAK_ONLY		= 0x080006; //話すのみ
	
	
	//画像受信
	public static final int RECEIVE_IMAGE				= 0x0FF000; //画像受信
	

	
	public static final String INTENT_KEY_BUSINESS = "Business"; //用件
	public static final String INTENT_KEY_MESSAGE = "Message"; //メッセージ

	public static final String OUTSIDE_PHONE_CALL_ACTIVITY_CLOSE = "OutsidePhoneCallActivityClose";//通話終了
	public static final String INSIDE_VISITERCHECK_ACTVITY_OPEN = "VisiteCheckActivityOpen";//訪問
	public static final String INSIDE_CAMERA_CHANGE = "InterractionCameraChange";//通話終了
	public static final String OUTSIDE_SELECTBUSINESS_ACTVITY_OPEN = "SeledtBusinessActivityOpen";
}

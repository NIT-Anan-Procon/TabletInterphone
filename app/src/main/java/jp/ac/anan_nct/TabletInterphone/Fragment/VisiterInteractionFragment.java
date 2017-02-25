package jp.ac.anan_nct.TabletInterphone.Fragment;

import jp.ac.anan_nct.TabletInterphone.Const;
import jp.ac.anan_nct.TabletInterphone.R;
import jp.ac.anan_nct.TabletInterphone.Activity.Inside.VisiterCheckActivity;
import jp.ac.anan_nct.TabletInterphone.Utility.Util;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Calendar;

public class VisiterInteractionFragment extends BaseFragment{
	private TextView businessTextView;
	
	private boolean isMikeTouch = false;
	private boolean isSpeakerClick = false;
	private boolean License = true;
	private int cameraStatus = 0;
	
	private View cameraLayout;
	
	@Override
	protected void onCreate(View view){
		businessTextView = (TextView)view.findViewById(R.id.interactionTextView);
		//changePackman(view);
		changeSpeaker(view);
		changeCamera(view, sharedVariable.interval);
		cameraLayout = view.findViewById(R.id.cameraLayout);
		
		loadBusiness();
		
		view.findViewById(R.id.waitLayout).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v){
				sharedVariable.wifiSocket.writeObject(Const.INTERACTION_WAIT);
				
				//7/20追加
				sharedVariable.writeReaction("参ります");
				//
				
				Log.d("Vis",String.valueOf(sharedVariable.wifiSocket.readObject()));
				AfterInteractionFragment fragment = new AfterInteractionFragment();
				Bundle args = new Bundle();
				args.putString("Interaction", "只今参ります。");
				fragment.setArguments(args);
				changeLeftFragment(fragment);
			}
		});
		
		view.findViewById(R.id.getOutLayout).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){

				sharedVariable.wifiSocket.writeObject(Const.INTERACTION_GET_OUT);
				
				//7/20追加
				sharedVariable.writeReaction("結構です");
				//
				
				AfterInteractionFragment fragment = new AfterInteractionFragment();
				Bundle args = new Bundle();
				args.putString("Interaction", "結構です。お帰りください。");
				fragment.setArguments(args);
				changeLeftFragment(fragment);
			}
		});
		
		view.findViewById(R.id.moreLayout).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				sharedVariable.wifiSocket.writeObject(Const.INTERACTION_MORE);
				AfterInteractionFragment fragment = new AfterInteractionFragment();
				
				//7/20追加
				sharedVariable.writeReaction("具体的に");
				//
				
				Bundle args = new Bundle();
				args.putString("Interaction", "具体的にお話ください。" + "\n\n選択中");
				fragment.setArguments(args);
				changeLeftFragment(fragment);
			}
		});
		
		cameraLayout.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				if(!isMikeTouch && License){
					License = false;
					switch(cameraStatus){
						case -1:
							sharedVariable.wifiSocket.writeObject(Const.INTERACTION_CAMERA_IDLE);
							changeCamera(view,1);
							//sharedVariable.interval = 1;
							break;
						case 1:
							sharedVariable.wifiSocket.writeObject(Const.INTERACTION_CAMERA_ON);
							changeCamera(view,0);
							//sharedVariable.interval = 0;
							break;
						case 0: 
							sharedVariable.wifiSocket.writeObject(Const.INTERACTION_CAMERA_OFF);
							changeCamera(view,-1);
							//sharedVariable.interval = -1;
							break;
						default:
							Log.d("ellor","ellor" + cameraStatus);
							break;
					}
					License = true;
				}
			}
		});
		
		view.findViewById(R.id.shutDownLayout).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				sharedVariable.wifiSocket.writeObject(Const.INTERACTION_STOP);
				sharedVariable.service.stopService(666);
			}
		});
		
		view.findViewById(R.id.shutDownLayout).setVisibility(view.INVISIBLE);
		
		//8/31追加
		
		view.findViewById(R.id.nameCheckLayout).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				try{
					sharedVariable.visiterCheckActivity.setImage(sharedVariable.getWserial());
				}
				catch(NullPointerException e){
					
				}
			}
		});
		
		view.findViewById(R.id.nameLayout).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){

				sharedVariable.wifiSocket.writeObject(Const.INTERACTION_SHOW_NAME);
				
				sharedVariable.writeReaction("名刺");
				
				AfterInteractionFragment fragment = new AfterInteractionFragment();
				Bundle args = new Bundle();
				args.putString("Interaction", "名刺・名札を見せてください");
				fragment.setArguments(args);
				changeLeftFragment(fragment);
			}
		});
		
		view.findViewById(R.id.finishLayout).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				sharedVariable.wifiSocket.writeObject(Const.INTERACTION_FINISH);
					//AfterInteractionFragment fragment = new AfterInteractionFragment();
					//Bundle args = new Bundle();
					//args.putString("Interaction", "通信失敗：再設定してください。");
					//fragment.setArguments(args);
					//changeLeftFragment(fragment);
				changeCamera(cameraLayout,1);
				//sharedVariable.interval = -1;
			}
		});
		
		view.findViewById(R.id.LightLayout).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				sharedVariable.wifiSocket.writeObject(Const.INTERACTION_LIGHT_ON);
			}
		});
		
		/*
		view.findViewById(R.id.voiceChatLayout).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				//sharedVariable.wifiSocket.writeObject(Const.INTERACTION_PHONE_CALL);
				//sharedVariable.service.startTrackAndRecord();
				changeLeftFragment(new VoiceChatFragment());
				//changeLeftFragment(new PhoneCallFragment());
			}
		});
		*/
		
		view.findViewById(R.id.speakerLayout).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				if(!isMikeTouch && License){
					License = false;
					switch(sharedVariable.phoneCallStatus(0)){
						case Const.INTERACTION_PHONE_CALL_END:
							try{
							sharedVariable.service.endTrackAndRecord();
							sharedVariable.wifiSocket.writeObject(Const.VOICE_CHAT_SPEAK_ONLY);
							sharedVariable.service.startTrackAndRecord(Const.VOICE_CHAT_LISTEN_ONLY);
							sharedVariable.phoneCallStatus(Const.VOICE_CHAT_LISTEN_ONLY);

							changeSpeaker(view);
							}
							catch(Exception e){
								
							}
							break;
						case Const.VOICE_CHAT_LISTEN_ONLY:
							try{
							sharedVariable.phoneCallStatus(Const.INTERACTION_PHONE_CALL_END);
							sharedVariable.wifiSocket.writeObject(Const.INTERACTION_PHONE_CALL_END);
							sharedVariable.service.endTrackAndRecord();
							sharedVariable.phoneCallStatus(Const.INTERACTION_PHONE_CALL_END);

							changeSpeaker(view);
							}
							catch(Exception e){
								
							}
							break;	
						default:
							break;
							
					
					}
					License = true;
				}
			}
		});
		
		view.findViewById(R.id.mikeLayout).setOnTouchListener(new View.OnTouchListener(){
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				if(License){
					switch(event.getAction() & MotionEvent.ACTION_MASK){
						case MotionEvent.ACTION_DOWN:
							isMikeTouch = true;
							try{
								sharedVariable.service.endTrackAndRecord();
								if(isSpeakerClick){
									sharedVariable.wifiSocket.writeObject(Const.VOICE_CHAT_DUAL);
									sharedVariable.service.startTrackAndRecord(Const.VOICE_CHAT_DUAL);
								}
								else{
									sharedVariable.wifiSocket.writeObject(Const.VOICE_CHAT_LISTEN_ONLY);
									sharedVariable.service.startTrackAndRecord(Const.VOICE_CHAT_SPEAK_ONLY);
								}
							}
							catch(Exception e){
								Log.d("ontouch","ontouch03");
							}
							changeMike(view , true);
							break;
						case MotionEvent.ACTION_UP:
							try{
								if(isSpeakerClick){
									sharedVariable.service.endTrackAndRecord();
									sharedVariable.wifiSocket.writeObject(Const.VOICE_CHAT_SPEAK_ONLY);
									sharedVariable.service.startTrackAndRecord(Const.VOICE_CHAT_LISTEN_ONLY);
									
								}
								else{
									sharedVariable.wifiSocket.writeObject(Const.INTERACTION_PHONE_CALL_END);
									sharedVariable.service.endTrackAndRecord();
									
								}
							}
							catch(Exception e){
								Log.d("ontouch","ontouch04");
							}
							isMikeTouch = false;
							changeMike(view , false);
							break;
						default:
							break;
					}
				}
				return true;
			}
		});
		
		
		view.findViewById(R.id.writeLayout).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				changeLeftFragment(new WriteFragment());
			}
		});
		
		IntentFilter filter = new IntentFilter(Const.INSIDE_CAMERA_CHANGE);
		LocalBroadcastManager.getInstance(sharedVariable.currentTIActivity).registerReceiver(receiver, filter);
		
	}
	
	public void loadBusiness(){
		Bundle arguments = getArguments();
		
		if(arguments != null)
			loadBusiness(arguments.getInt("BusinessID", -1), arguments.getString("MoreBusiness", null));
	}
	
	public void loadBusiness(int businessId, String moreBusiness){
		if(businessId == -1){
			if(moreBusiness != null)
				businessTextView.setText("選択中(" + moreBusiness + ")");
		}
		else{
			
			if(moreBusiness == null)
				businessTextView.setText(Util.getBusinessText(businessId));
			else if(businessId == Const.BUSINESS_OTHER)
				businessTextView.setText("その他　" + moreBusiness + ")");
			else if((businessId == Const.BUSINESS_PRE_SELECT) && (!sharedVariable.writeStatus))
				businessTextView.setText("選択中・・・");
			else
				businessTextView.setText(Util.getBusinessText(businessId) + "(" + moreBusiness + ")");
		}
	}
	
	public void setBusiness(String business){
		businessTextView.setText(business);
	}
	
	@Override
	public void onPause(){
		sharedVariable.interval = cameraStatus;
		super.onPause();
	}
	
	@Override
	protected int getLayoutId(){
		return R.layout.fragment_visiter_interaction_icons;
		//return R.layout.fragment_visiter_interaction;
	}
	
	/////////////////////////////////////////////////////////
	//9/30追加
	//通話状態表示画像変更メソッド
	////////////////////////////////////////////////////////
	/*
	private void changePackman(View view){
		Resources r = getResources();
		Drawable mike;
		Drawable speaker;

		switch(sharedVariable.phoneCallStatus(0)){
		case Const.VOICE_CHAT_DUAL:
			mike = r.getDrawable(R.drawable.microphone_on);
			speaker = r.getDrawable(R.drawable.speaker_on);
			break;
		case Const.VOICE_CHAT_LISTEN_ONLY:
			mike = r.getDrawable(R.drawable.microphone_off);
			speaker = r.getDrawable(R.drawable.speaker_on);
			break;
		case Const.INTERACTION_PHONE_CALL_END:
			mike = r.getDrawable(R.drawable.microphone_off);
			speaker = r.getDrawable(R.drawable.speaker_off);
			break;
		default:
			mike = r.getDrawable(R.drawable.microphone_off);
			speaker = r.getDrawable(R.drawable.speaker_off);
			break;
		}
		ImageView mikeImg = (ImageView)view.findViewById(R.id.mike);
		ImageView speakerImg = (ImageView)view.findViewById(R.id.speaker);
		mikeImg.setImageDrawable(mike);
		speakerImg.setImageDrawable(speaker);
	}
	*/
	
	private void changeSpeaker(View view){
		Resources r = getResources();
		Drawable speaker;
		switch(sharedVariable.phoneCallStatus(0)){
			case Const.VOICE_CHAT_LISTEN_ONLY:
				speaker = r.getDrawable(R.drawable.speaker_on);
				isSpeakerClick = true;
				break;
			case Const.INTERACTION_PHONE_CALL_END:
				speaker = r.getDrawable(R.drawable.speaker_off);
				isSpeakerClick = false;
				break;
			default:
				speaker = r.getDrawable(R.drawable.speaker_off);
				break;
		}
		ImageView speakerImg = (ImageView)view.findViewById(R.id.speaker);
		speakerImg.setImageDrawable(speaker);
	}
	
	private void changeMike(View view, boolean boo){
		Resources r = getResources();
		Drawable mike;
		if(boo){
			mike = r.getDrawable(R.drawable.microphone_on);
		}
		else{
			mike = r.getDrawable(R.drawable.microphone_off);
		}
		ImageView mikeImg = (ImageView)view.findViewById(R.id.mike);
		mikeImg.setImageDrawable(mike);
	}
	
	/*
	public void changeCamera(int interval){
		View view = viewForCamera;
		Resources r = getResources();
		Drawable camera;
		if(interval < 0){
			camera = r.getDrawable(R.drawable.camera_off); 
			cameraStatus = -1;
		}
		else if(interval > 0){
			camera = r.getDrawable(R.drawable.camera_idle);
			cameraStatus = 1;
		}
		else{
			camera = r.getDrawable(R.drawable.camera_on); 
			cameraStatus = 0;
		}
		ImageView cameraImg = (ImageView)view.findViewById(R.id.camera);
		cameraImg.setImageDrawable(camera);
	*/
	
	private void changeCamera(View view, int interval){
		Resources r = getResources();
		Drawable camera;
		if(interval < 0){
			camera = r.getDrawable(R.drawable.camera_off); 
			cameraStatus = -1;
		}
		else if(interval > 0){
			camera = r.getDrawable(R.drawable.camera_idle);
			cameraStatus = 1;
		}
		else{
			camera = r.getDrawable(R.drawable.camera_on); 
			cameraStatus = 0;
		}
		ImageView cameraImg = (ImageView)view.findViewById(R.id.camera);
		cameraImg.setImageDrawable(camera);
	}
	
	private final WakefulBroadcastReceiver receiver = new WakefulBroadcastReceiver(){
		@Override
		public void onReceive(Context context, Intent intent){
			if(intent.getAction().equals(Const.INSIDE_CAMERA_CHANGE)){
				try{
					changeCamera(cameraLayout,sharedVariable.interval);
				}
				catch(Exception e){
				}
				finally{
					WakefulBroadcastReceiver.completeWakefulIntent(intent);
				}
			}
		}
	};
		
	//*////////////////////////////////////////////////////////////
}

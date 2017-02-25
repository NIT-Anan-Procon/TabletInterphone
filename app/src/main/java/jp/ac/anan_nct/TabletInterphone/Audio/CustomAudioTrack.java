package jp.ac.anan_nct.TabletInterphone.Audio;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import android.media.AudioFormat;
import android.media.AudioManager;

public class CustomAudioTrack extends android.media.AudioTrack{
	private static final int SAMPLE_RATE = 8000;
	private static final int OUT_CHANNEL = AudioFormat.CHANNEL_OUT_MONO;
	private static final int AUDIO_FORMAT = AudioFormat.ENCODING_PCM_16BIT;

	private BlockingQueue<byte[]> queue;
	private Thread playThread;
	
	public CustomAudioTrack(){
		super(AudioManager.STREAM_MUSIC, SAMPLE_RATE, OUT_CHANNEL, AUDIO_FORMAT, SAMPLE_RATE, CustomAudioTrack.MODE_STREAM);
	}
	
	@Override
	public void play(){
		super.play();
		
		queue = new LinkedBlockingQueue<byte[]>();
		playThread = new Thread(playRunnable);
		playThread.start();
	}
	
	private final Runnable playRunnable = new Runnable(){
		@Override
		public void run(){
			playRunnableMethod();
		}
	};
	
	private void playRunnableMethod(){
		BlockingQueue<byte[]> queue = this.queue;
		try{
			for(;;){
				byte[] audioData = queue.take();
				super.write(audioData, 0, audioData.length);
			}
		}
		catch(InterruptedException e){}
	}
	
	public void write(byte[] audioData){
		queue.offer(audioData);
	}
	
	@Override
	public void stop(){
		playThread.interrupt();
		playThread = null;
		
		try{
			super.stop();
		}
		catch(IllegalStateException e){}

		queue.clear();
		queue = null;
	}
}

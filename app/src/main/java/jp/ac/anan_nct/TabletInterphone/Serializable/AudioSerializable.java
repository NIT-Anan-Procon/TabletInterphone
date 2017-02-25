package jp.ac.anan_nct.TabletInterphone.Serializable;

import java.io.Serializable;

public class AudioSerializable implements Serializable{
	private static final long serialVersionUID = 1L;
	private byte[] audioBytes;

	public AudioSerializable(){}
	public AudioSerializable(byte[] audioBytes){
		this.audioBytes = audioBytes;
	}

	public byte[] getAudio(){
		return audioBytes;
	}
}

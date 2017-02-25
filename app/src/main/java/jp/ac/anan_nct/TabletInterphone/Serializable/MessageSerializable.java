package jp.ac.anan_nct.TabletInterphone.Serializable;

import java.io.Serializable;

public class MessageSerializable implements Serializable{
	private static final long serialVersionUID = 1L;
	private MessageType messageType;
	private String message;

	public enum MessageType{
		OTHER, MORE
	}
	
	public MessageSerializable(){}
	public MessageSerializable(MessageType messageType, String message){
		this.messageType = messageType;
		this.message = message;
	}

	public MessageType getMessageType(){
		return messageType;
	}
	
	public String getMessage(){
		return message;
	}
}

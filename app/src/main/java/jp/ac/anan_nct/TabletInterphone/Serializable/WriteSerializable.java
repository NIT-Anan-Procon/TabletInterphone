package jp.ac.anan_nct.TabletInterphone.Serializable;

import java.io.Serializable;

import jp.ac.anan_nct.TabletInterphone.Utility.Util;
import android.graphics.Bitmap;

public class WriteSerializable implements Serializable{
	private static final long serialVersionUID = 1L;
	private byte[] writeBytes;

	public WriteSerializable(){}
	public WriteSerializable(Bitmap bitmap){
		this.writeBytes = Util.bitmapEncode(bitmap);
	}

	public Bitmap getBitmap(){
		return Util.bitmapDecode(writeBytes);
	}
}

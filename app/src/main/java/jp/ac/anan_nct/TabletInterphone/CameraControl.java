package jp.ac.anan_nct.TabletInterphone;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraControl{
	private Camera camera; //カメラ
	private int previewWidth = 2000; //プレビュー横幅
	private int previewHeight = 2000; //プレビュー縦幅
	private OnCaptureListener listener; //キャプチャ時のリスナー
	
	private byte[] previewBytes; //プレビュー用配列
	private int[] rgb; //プレビュー変換用配列
	private Bitmap bitmap; //プレビュー用Bitmap
	
	
	public interface OnCaptureListener{
		public abstract void onCapture(Bitmap bitmap);
	}
	
	
	/***************************
	 * コンストラクタ
	 ***************************/
	public CameraControl(SurfaceView sv) {
        //カメラ取得
		camera = Camera.open(getFrontCameraId());

		//カメラ情報取得
		Camera.Parameters p = camera.getParameters();

		//対応しているなら、自動フォーカス設定
		if(p.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE))
			p.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
		
		//プレビューの形式をYUV420SPに設定
		p.setPreviewFormat(ImageFormat.NV21);
		
		//プレビューサイズの決定
		setPreviewSize(p);

		//カメラ設定適用
		camera.setParameters(p);

		//プレビュー方向設定
		//camera.setDisplayOrientation(90);

		//プレビューコールバック用バイト配列用意
        previewBytes = new byte[previewWidth * previewHeight * ImageFormat.getBitsPerPixel(ImageFormat.NV21) / 8];
        rgb = new int[previewWidth * previewHeight];
        bitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888); //ARGB8888で空のビットマップ作成
        
        //これを設定しないとNexusでプレビュー時のコールバックが呼び出されないみたい
        SurfaceHolder holder = sv.getHolder();
        try {
			camera.setPreviewDisplay(holder);
		} catch (IOException e) {}
        holder.addCallback(new SurfaceHolder.Callback() {
			@Override
			public void surfaceCreated(SurfaceHolder holder) {
				try {
					camera.setPreviewDisplay(holder);
				} catch (IOException e) {}
			}
			
			@Override
			public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
			
			@Override
			public void surfaceDestroyed(SurfaceHolder holder) {}
		});

		//プレビュースタート
		camera.startPreview();

		//写真撮影用コールバック設定
		camera.setPreviewCallbackWithBuffer(new Camera.PreviewCallback() {
			@Override
			public void onPreviewFrame(byte[] data, Camera camera) {
				decodeYUV420SP(rgb, data, previewWidth, previewHeight); //変換
				bitmap.setPixels(rgb, 0, previewWidth, 0, 0, previewWidth, previewHeight); //変換した画素からビットマップにセット

				//リスナー実行
				if(listener != null)
					listener.onCapture(bitmap);
			}
		});
	}
	
	
	/***************************
	 * フロントカメラのIDを取得
	 * 
	 * @return フロントカメラのIDを返す。フロントカメラが存在しない場合は0を返す。
	 ***************************/
	private int getFrontCameraId(){
		int cameraId = 0;
		
		//全カメラを調べて、フロントカメラを取得
        for (int i = 0, numberOfCameras = Camera.getNumberOfCameras(); i < numberOfCameras; i++) {
        	//カメラ情報取得
    	    CameraInfo cameraInfo = new CameraInfo();
            Camera.getCameraInfo(i, cameraInfo);
            
            //フロントカメラならIDを返す
            if (cameraInfo.facing == CameraInfo.CAMERA_FACING_FRONT) {
            	cameraId = i;
                break;
            }
        }
        
        return cameraId;
	}
	
	
	/***************************
	 * プレビューサイズを決定し設定する（横幅320以上の最小プレビューサイズにする）
	 * 
	 * @param カメラパラメータ
	 ***************************/
	private void setPreviewSize(Camera.Parameters param){
		for(Size size : param.getSupportedPreviewSizes()){
			if(size.width == 320 && size.width == 240){
				
			}
		}
		
		//対応プレビューサイズ取得
		for(Size size : param.getSupportedPreviewSizes()){
			if(size.width == 320 && size.width == 240){
				previewWidth = 320;
				previewHeight = 240;
				break;
			}
			if(size.width >= 320 && previewWidth > size.width){
				previewWidth = size.width;
				previewHeight = size.height;
			}
		}

		//プレビューサイズ適用
		param.setPreviewSize(previewWidth, previewHeight);
	}


	/***************************
	 * カメラの解放
	 ***************************/
	public void release() {
		camera.setPreviewCallbackWithBuffer(null);
		camera.stopPreview();
		camera.release();
	}


	/***************************
	 * 無音での写真撮影
	 ***************************/
	public void capture(){
		camera.addCallbackBuffer(previewBytes);
	}
	
	
	/***************************
	 * 写真撮影時のリスナーを設定
	 *
	 * @param listener 写真が撮られた際のリスナー
	 ***************************/
	public void setOnCaptureListener(OnCaptureListener listener){
		this.listener = listener;
	}


	/***************************
	 * YUV420形式のbyte配列をRGB形式のbyte配列に変換
	 *
	 * @param rgb RGB形式の変換結果が格納されるbyte配列
	 * @param yuv420sp YUV420形式のbyte配列
	 * @param width 画像の横幅
	 * @param height 画像の縦幅
	 ***************************/
    private final void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {
    	final int frameSize = width * height;

    	for (int j = 0, yp = 0; j < height; j++) {
    		int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
    		for (int i = 0; i < width; i++, yp++) {
    			int y = (0xff & ((int) yuv420sp[yp])) - 16;
    			if (y < 0) y = 0;
    			if ((i & 1) == 0) {
    				v = (0xff & yuv420sp[uvp++]) - 128;
    				u = (0xff & yuv420sp[uvp++]) - 128;
    			}
    			int y1192 = 1192 * y;
    			int r = (y1192 + 1634 * v);
    			int g = (y1192 - 833 * v - 400 * u);
    			int b = (y1192 + 2066 * u);
    			if (r < 0) r = 0; else if (r > 262143) r = 262143;
    			if (g < 0) g = 0; else if (g > 262143) g = 262143;
    			if (b < 0) b = 0; else if (b > 262143) b = 262143;
    			rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
    		}
    	}
    }
}

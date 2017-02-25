package jp.ac.anan_nct.TabletInterphone;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class PaintView extends View{
	private OnTouchUpListener onTouchUpListener;
	private Path path = null;
	private Bitmap bitmap = null;
	private Paint paint;
	
	// コールバック用
	public static interface OnTouchUpListener{
		public abstract void onTouchUp();
	}
	
	public void setOnTouchUpListener(OnTouchUpListener onTouchUpListener){
		this.onTouchUpListener = onTouchUpListener;
	}
	
	/***************************
	 * コンストラクタ
	 ***************************/
	public PaintView(Context context){
		super(context);
		init();
	}
	
	/***************************
	 * コンストラクタ
	 ***************************/
	public PaintView(Context context, AttributeSet attrs){
		super(context, attrs);
		init();
	}
	
	/***************************
	 * コンストラクタ
	 ***************************/
	public PaintView(Context context, AttributeSet attrs, int defStyle){
		super(context, attrs, defStyle);
		init();
	}
	
	/***************************
	 * 初期化
	 ***************************/
	private void init(){
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setColor(Color.BLACK);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(5);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeJoin(Paint.Join.ROUND);
	}
	
	/***************************
	 * 全消し
	 ***************************/
	public void clear(){
		bitmap = null;
		path = null;
		invalidate();
	}
	
	/***************************
	 * 画像設定
	 ***************************/
	public void setBitmap(Bitmap bitmap){
		if(this.bitmap != null)
			this.bitmap.recycle();
		
		this.bitmap = bitmap;
		path = null;
		invalidate();
	}
	
	/***************************
	 * 画像取得
	 ***************************/
	public Bitmap getBitmap(){
		setDrawingCacheEnabled(true);
		Bitmap bitmap = Bitmap.createBitmap(getDrawingCache());
		setDrawingCacheEnabled(false);
		return bitmap;
	}
	
	/***************************
	 * タッチイベント
	 ***************************/
	@Override
	public boolean onTouchEvent(MotionEvent motionevent){
		try{
			switch(motionevent.getAction()){
			case MotionEvent.ACTION_DOWN:
				path = new Path();
				path.moveTo(motionevent.getX(), motionevent.getY());
				break;
			
			case MotionEvent.ACTION_MOVE:
				path.lineTo(motionevent.getX(), motionevent.getY());
				invalidate();
				break;
			
			case MotionEvent.ACTION_UP:
				path.lineTo(motionevent.getX(), motionevent.getY());
				setDrawingCacheEnabled(true);
				bitmap = Bitmap.createBitmap(getDrawingCache());
				setDrawingCacheEnabled(false);
				invalidate();
				
				if(onTouchUpListener != null)
					onTouchUpListener.onTouchUp();
				
				break;
			}
		}
		catch(Exception e){
			
		}
		return true;
	}
	
	/***************************
	 * 描画
	 ***************************/
	@Override
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		if(bitmap != null)
			canvas.drawBitmap(bitmap, 0, 0, null);
		
		if(path != null)
			canvas.drawPath(path, paint);
	}
}

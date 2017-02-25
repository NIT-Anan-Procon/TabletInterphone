package jp.ac.anan_nct.TabletInterphone.Dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;

public class LeftDialogBuilder{
	abstract public static class OnCancelListener{
		abstract protected void onCancel(CustomDialog dialog);
	}
	
	abstract public static class OnDismissListener{
		abstract protected void onDismiss(CustomDialog dialog);
	}
	
	private final Activity activity;
	private final AlertDialog.Builder builder;
	private OnCancelListener onCancelListener = null;
	private OnDismissListener onDismissListener = null;
	private boolean canceledOnTouchOutside = false;
	
	private Display display;
	
	private CustomDialog blueDialog;
	
	public LeftDialogBuilder(Activity activity){
		this.activity = activity;
		display = activity.getWindowManager().getDefaultDisplay();
		builder = new AlertDialog.Builder(activity,1);
	}
	
	public LeftDialogBuilder setIcon(int iconId){
		builder.setIcon(iconId);
		return this;
	}
	
	public LeftDialogBuilder setTitle(CharSequence title){
		builder.setTitle(title);
		return this;
	}
	
	public LeftDialogBuilder setTitle(int titleId){
		builder.setTitle(titleId);
		return this;
	}
	
	public LeftDialogBuilder setMessage(String message){
		builder.setMessage(message);
		return this;
	}
	
	public LeftDialogBuilder setMessage(int messageId){
		builder.setMessage(messageId);
		return this;
	}
	
	public LeftDialogBuilder setView(View view){
		builder.setView(view);
		return this;
	}
	
	public LeftDialogBuilder setItems(CharSequence[] items, OnClickListener listener){
		builder.setItems(items, listener);
		return this;
	}
	
	public LeftDialogBuilder setItems(int itemsId, OnClickListener listener){
		builder.setItems(itemsId, listener);
		return this;
	}
	
	public LeftDialogBuilder setPositiveButton(CharSequence text, OnClickListener listener){
		builder.setPositiveButton(text, listener);
		return this;
	}
	
	public LeftDialogBuilder setPositiveButton(int textId, OnClickListener listener){
		builder.setPositiveButton(textId, listener);
		return this;
	}
	
	public LeftDialogBuilder setNegativeButton(CharSequence text, OnClickListener listener){
		builder.setNegativeButton(text, listener);
		return this;
	}
	
	public LeftDialogBuilder setNegativeButton(int textId, OnClickListener listener){
		builder.setNegativeButton(textId, listener);
		return this;
	}
	
	public LeftDialogBuilder setNeutralButton(CharSequence text, OnClickListener listener){
		builder.setNeutralButton(text, listener);
		return this;
	}
	
	public LeftDialogBuilder setNeutralButton(int textId, OnClickListener listener){
		builder.setNeutralButton(textId, listener);
		return this;
	}
	
	public LeftDialogBuilder setOnCancelListener(OnCancelListener onCancelListener){
		this.onCancelListener = onCancelListener;
		return this;
	}
	
	public LeftDialogBuilder setOnDismissListener(OnDismissListener onDismissListener){
		this.onDismissListener = onDismissListener;
		return this;
	}
	
	public LeftDialogBuilder setCanceledOnTouchOutside(boolean cancel){
		canceledOnTouchOutside = cancel;
		return this;
	}
	
	public LeftDialogBuilder setInverseBackgroundForced(boolean forceInverseBackground) {
		builder.setInverseBackgroundForced(forceInverseBackground);
        return this;
    }
	
	public CustomDialog create(){
		final AlertDialog leftdialog = builder.create();
		WindowManager.LayoutParams lp = leftdialog.getWindow().getAttributes();
		lp.gravity = Gravity.LEFT;
		leftdialog.getWindow().setFlags(0, lp.FLAG_DIM_BEHIND);
		Point p = new Point();
		display.getSize(p);
		lp.width = (int)(p.x * 0.4);
		
		leftdialog.getWindow().setAttributes(lp);
		leftdialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
		final OnCancelListener onCancelListener = this.onCancelListener;
		final OnDismissListener onDismissListener = this.onDismissListener;
		
		blueDialog = new CustomDialog(){
			@Override
			public Dialog makeDialog(){
				return leftdialog;
			}
			
			@Override
			public void onCancel(CustomDialog dialog){
				if(onCancelListener != null)
					onCancelListener.onCancel(dialog);
			}
			
			@Override
			public void onDismiss(CustomDialog dialog){
				if(onDismissListener != null)
					onDismissListener.onDismiss(dialog);
			}
		};
		return blueDialog;
	}
	
	public void show(String tag){
		if(blueDialog == null)
			create();

		blueDialog.show(activity, tag);
	}
	
	public static void showErrorDialog(Activity activity, String message){
		new DialogBuilder(activity)
			.setIcon(android.R.drawable.ic_dialog_alert)
			.setTitle("エラー")
			.setMessage(message)
			.setPositiveButton("OK", null)
			.show("エラー");
	}
}

/**
 * 项目名称: CoreLibrary
 * 文件名称: BaseActivity.java
 * 创建时间: 2014/3/28
 * Copyright: 2014 www.fantasee.cn Inc. All rights reserved.
 */
package cn.core.mobile.library.activity;

import cn.core.library.mobile.R;
import cn.core.mobile.library.widgets.BaseProgressDialog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * 所有页面activity的抽象基类
 * 
 * @author Will.Wu </br> Create at 2014年2月19日 下午5:31:35
 * @version 1.0
 */
public abstract class BaseActivity extends FragmentActivity implements OnClickListener {
	private BaseProgressDialog mProgressDialog = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		init();
		fillView();
		setListener();
		loadData();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			finish();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		super.startActivityForResult(intent, requestCode);
		overridePendingTransition(R.anim.base_push_right_in, R.anim.base_push_left_out);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		if (mProgressDialog != null && mProgressDialog.isShowing() && mProgressDialog.cancelable()) {
			cancelProgressDialog();
			return;
		}
		super.finish();
		overridePendingTransition(R.anim.base_push_left_in, R.anim.base_push_right_out);
	}

	/**
	 * your <code>setContentView(layoutResID)</code> in here.
	 */
	protected abstract void init();

	/**
	 * your <code>findViewById(int id)</code> in here.
	 */
	protected void fillView() {
	}

	protected void setListener() {
	}

	protected void loadData() {
	}

	/**
	 * @author Will.Wu
	 */
	public void stopProgressDialog() {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.stop();
		}
		mProgressDialog = null;
	}

	protected void cancelProgressDialog() {
		if (mProgressDialog.cancel()) {
			mProgressDialog = null;
		}
	}

	public void showProgressDialog(BaseProgressDialog.OnCancelListener cancelListener, boolean cancelable, String msg) {
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			return;
		}
		mProgressDialog = new BaseProgressDialog(this, msg);
		if (cancelListener != null) {
			mProgressDialog.setOnCancelListener(cancelListener);
		}
		mProgressDialog.setCancelable(cancelable);
		mProgressDialog.show();
	}

	public void showProgressDialog(boolean cancelable, String msg) {
		showProgressDialog(null, cancelable, msg);
	}

	public void showProgressDialog(String msg) {
		showProgressDialog(true, msg);
	}

	public void showProgressDialog(boolean cancelable) {
		showProgressDialog(cancelable, "");
	}

	/**
	 * 检查网络连接
	 * 
	 * @return
	 */
	public boolean checkInternetConnection() {
		NetworkInfo info = null;
		if (info == null) {
			ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
			info = manager.getActiveNetworkInfo();
		}
		if (info != null && info.isAvailable()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param title
	 * @param msg
	 * @param oklistener
	 * @param cancellistener
	 */
	protected void showDialogForPrompt(String title, String msg, DialogInterface.OnClickListener oklistener, DialogInterface.OnClickListener cancellistener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this).setCancelable(false).setPositiveButton(getString(R.string.base_progress_dialog_ok), oklistener);
		if (cancellistener != null) {
			builder.setNegativeButton(getString(R.string.base_progress_dialog_cancel), cancellistener);
		}
		AlertDialog dialog = builder.create();
		dialog.setTitle(title);
		dialog.setMessage(msg);
		dialog.show();
	}

	/**
	 * @param title
	 * @param msg
	 */
	protected void showDialogForPrompt(String title, String msg) {
		showDialogForPrompt(title, msg, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}

		}, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}

	public void exitApp() {
		String title = getString(R.string.base_progress_dialog_title);
		String msg = getString(R.string.base_exit_app);
		showDialogForPrompt(title, msg, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				System.exit(0);
			}

		}, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}
}

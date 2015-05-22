package com.yisi.yisiHome.baseServer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;

import com.yisi.yisiHome.R;
import com.yisi.yisiHome.baseEntity.EntityVersion;


public class UpdateService {

	/* 下载保存路径 */
	private String mSavePath;
	private boolean interceptFlag;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;
	private ProgressBar mProgress;
	private Thread downLoadThread;
	private AlertDialog downloadDialog = null;
	private Context context;
	private EntityVersion version;
	private String apkname = "";
	private String updateurl;
	int contentLength = 0;
	int progressValue = 0;

	public UpdateService(Context context) {
		this.context = context;
	}

	public void downLoad(EntityVersion version) {
		this.version = version;
		apkname = version.getApkname();
		showDownloadDialog();
		startDownLoad();
	}
	//*********************************************
	private void showDownloadDialog() {
		final AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.show();
		Window win = dialog.getWindow();
		final LayoutInflater inflater = LayoutInflater.from(context);
		View v = inflater.inflate(R.layout.frame_update_progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progress);
		win.setContentView(v);
		Button cancle = (Button) win.findViewById(R.id.undownload);
		cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				interceptFlag = true;
			}
		});
		downloadDialog = dialog;
		downloadDialog.show();
	}

	private void startDownLoad() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	

	private Handler mHandler = new Handler(new Handler.Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progressValue);
				break;
			case DOWN_OVER:
				installApk();
				break;
			default:
				break;
			}
			return false;
		}
	}) ;

	private InputStream getInputStream() {
		updateurl = version.getApkUrl();
		try {
			URL url = new URL(updateurl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.connect();
			contentLength = conn.getContentLength();
			return conn.getInputStream();
		} catch (Exception e) {
		}
		return null;
	}

	private File getFile() {
		String sdpath = "";
		// 判断SD卡是否存在，并且是否具有读写权限
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) { // 获得存储卡的路径
			sdpath = Environment.getExternalStorageDirectory() + "/";
		} else {
			sdpath = Environment.getDataDirectory() + "/";
		}
		mSavePath = sdpath + "download";
		File path = new File(mSavePath);
		if (!path.exists()) {
			path.mkdir();
		}
		File file = new File(mSavePath + "/" + version.getApkname());
		if (file.exists()) {
			file.deleteOnExit();
		}
		return file;
	}

	private void writeFile(File apkFile, InputStream is) {
		int count = 0;
		byte buf[] = new byte[1024];
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(apkFile);
			do {
				int numread = is.read(buf);
				count += numread;
				progressValue = (int) (((float) count / contentLength) * 100);
				// 更新进度
				mHandler.sendEmptyMessage(DOWN_UPDATE);
				if (numread <= 0) {
					// 下载完成通知安装
					mHandler.sendEmptyMessage(DOWN_OVER);
					break;
				}
				fos.write(buf, 0, numread);
			} while (!interceptFlag);// 点击取消就停止下载.
			downloadDialog.dismiss();

			fos.close();
			is.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 安装apk
	 */
	private void installApk() {
		File apkfile = new File(mSavePath, apkname);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		context.startActivity(i);
	}

	private Runnable mdownApkRunnable = new Runnable() {
		public void run() {
			InputStream is = getInputStream();
			File apkFile = getFile();
			writeFile(apkFile, is);
		}
	};

}

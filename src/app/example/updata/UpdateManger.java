package app.example.updata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import tx.ydd.app.R;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import app.example.application.MyApplication;

public class UpdateManger {
	// 应用程序Context
	private Context mContext;
	// 提示消息
	private String updateMsg = "有最新的软件包，请下载！";
	// 下载安装包的网络路径,目前是QQ更新
	private String apkUrl = "http://softfile.3g.qq.com:8080/msoft/179/24659/43549/qq_hd_mini_1.4.apk";
	private Dialog noticeDialog;// 提示有软件更新的对话框
	private Dialog downloadDialog;// 下载对话框
	private static final String savePath = "/sdcard/updatedemo/";
	// 保存apk的文件夹
	private static final String saveFileName = savePath
			+ "UpdateDemoRelease.apk";
	// 进度条与通知UI刷新的handler和msg常量
	private ProgressBar mProgress;
	private static final int DOWN_UPDATE = 1;
	private static final int DOWN_OVER = 2;

	private int progress;// 当前进度

	private Thread downLoadThread;// 下载线程

	private boolean interceptFlag = false; // 用户取消下载

	private String content, url;

	// 通知处理刷新界面的handler
	private Handler mHandler = new Handler() {
		@SuppressLint("HandlerLeak")
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				break;
			case DOWN_OVER:
				installApk();
				break;
			}
			super.handleMessage(msg);
		}
	};

	public UpdateManger(Context context) {
		this.mContext = context;
	}

	// 显示更新程序对话框，供主程序调用
	public void checkUpdateInfo() {

		// 同样，在读取SharedPreferences数据前要实例化出一个SharedPreferences对象
		SharedPreferences sharedPreferences = MyApplication.getAppContext()
				.getSharedPreferences("version", Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		url = sharedPreferences.getString("url", "");

		// 当取消对话框后进行操作一定的代码？取消对话框
		showDownloadDialog();

	}
	@SuppressLint("InflateParams")
	protected void showDownloadDialog() {

		Builder dialog = new AlertDialog.Builder(mContext);
		// .setIcon(R.drawable.)
		dialog.setTitle("正在下载新版本，请稍等...");
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.update, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progress);
		dialog.setView(v);
		dialog.setIcon(R.drawable.update);
		dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

				dialog.dismiss();
			}
		});
		downloadDialog = dialog.create();
		downloadDialog.show();
		downloadApk();

	}

	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	protected void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
				"application/vnd.android.package-archive");
		// File.toString()会返回路径信息
		mContext.startActivity(i);
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			URL url2;
			try {
				url2 = new URL(url);
				HttpURLConnection conn = (HttpURLConnection) url2
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream ins = conn.getInputStream();
				File file = new File(savePath);
				if (!file.exists()) {
					file.mkdir();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream outStream = new FileOutputStream(ApkFile);
				int count = 0;
				byte buf[] = new byte[1024];
				do {
					int numread = ins.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					// 下载进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					outStream.write(buf, 0, numread);
				} while (!interceptFlag);
				// 点击取消停止下载
				outStream.close();
				ins.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

}

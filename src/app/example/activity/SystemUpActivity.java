package app.example.activity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import app.example.Url.UrlPath;
import app.example.application.MyApplication;
import app.example.updata.UpdateManger;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class SystemUpActivity extends Activity {
	private TextView content;
	private Button update;
	private ImageButton back;
	String updatecontent;
	private String version;// 本地当前版本号
	boolean first = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.system_update);
		init();// 控件初始化

		boolean net = detect(SystemUpActivity.this);
		if (net) {
			version = getVersion();
			Float i = Float.parseFloat(version);
			submitAsyncHttpClientGet(i);
		}

		OnClickListener();
	}

	private void init() {

		content = (TextView) findViewById(R.id.content);
		back = (ImageButton) findViewById(R.id.back);
		update = (Button) findViewById(R.id.update);

	}

	private void OnClickListener() {
		// TODO Auto-generated method stub
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				SystemUpActivity.this.finish();

			}
		});

		update.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				boolean net = detect(SystemUpActivity.this);
				// TODO Auto-generated method stub
				if (net) {// first作为一个标志，判断系统是否有更新，有的话就弹出对话框，没有的话显示toast

					if (first) {
						UpdateManger mUpdateManger;
						mUpdateManger = new UpdateManger(SystemUpActivity.this);// 注意此处不能传入getApplicationContext();会报错，因为只有是一个Activity才可以添加窗体
						mUpdateManger.checkUpdateInfo();
					} else {

						Toast.makeText(SystemUpActivity.this,
								"该版本已是最新版本了哦^o^！", Toast.LENGTH_SHORT).show();
					}

				} else {

					Toast.makeText(SystemUpActivity.this, "网络不可用，请检查网络！",
							Toast.LENGTH_SHORT).show();

				}

			}
		});

	}

	public String getVersion() {
		try {

			PackageManager manager = SystemUpActivity.this.getPackageManager();
			PackageInfo info = manager.getPackageInfo(
					SystemUpActivity.this.getPackageName(), 0);
			String version = info.versionName;
			return version;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 从服务器获取版本号
	 */
	public void submitAsyncHttpClientGet(final Float version) {
		// 创建异步请求端对象
		AsyncHttpClient client = new AsyncHttpClient();
		String url = UrlPath.versionOperateApi + version;
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				String json = msg.getData().getString("responseBody");

				try {
					JSONObject item = new JSONObject(json);
					updatecontent = item.getString("content");
					content.setText(updatecontent);
					first = true;
					String url = item.getString("url");
					// 实例化SharedPreferences对象（第一步）
					SharedPreferences mySharedPreferences = MyApplication
							.getAppContext().getSharedPreferences("version",
									Activity.MODE_PRIVATE);
					// 实例化SharedPreferences.Editor对象（第二步）
					SharedPreferences.Editor editor1 = mySharedPreferences
							.edit();
					// 用putString的方法保存数据
					editor1.putString("url", url);
					// 提交当前数据
					editor1.commit();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		};

		// 发送get请求对象
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				Log.i("updata接收失败", "失败失败！！！" + arg0 + new String(arg2));
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] responseBody) {
				// TODO Auto-generated method stub
				Log.i("updata接收成功", "arg0--------" + arg0);

				if (arg0 != 204) {

					Toast.makeText(SystemUpActivity.this, "有最新版本了哦^o^！",
							Toast.LENGTH_SHORT).show();
					String iString = new String(responseBody);
					Bundle bundle = new Bundle();
					bundle.putString("responseBody", iString);
					Message message = new Message();
					message.setData(bundle);
					handler.sendMessage(message);

				}

			}
		});

	}

	/*
	 * 
	 * 判断系统网络
	 */
	public static boolean detect(Activity act) {

		ConnectivityManager manager = (ConnectivityManager) act
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);

		if (manager == null) {
			return false;
		}

		NetworkInfo networkinfo = manager.getActiveNetworkInfo();

		if (networkinfo == null || !networkinfo.isAvailable()) {
			return false;
		}

		return true;
	}

}

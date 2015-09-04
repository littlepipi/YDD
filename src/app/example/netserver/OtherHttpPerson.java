package app.example.netserver;

import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import app.example.Url.UrlPath;
import app.example.application.MyApplication;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

@SuppressLint("HandlerLeak")
public class OtherHttpPerson {
	private String phone_number;
	private String personcreated, personname, personportrait, personlocation;
	private String id;
	private String created;
	private String content;
	private String messageOrnews;
	private String kind;
	private int num;

	public OtherHttpPerson(String phone_number, String id, String created,
			String content, String messageOrnews, String kind) {
		this.phone_number = phone_number;
		this.id = id;
		this.created = created;
		this.content = content;
		this.messageOrnews = messageOrnews;
		this.kind = kind;
	}

	public void submitAsyncHttpClientGet() {
		// 创建异步请求端对象

		AsyncHttpClient client = new AsyncHttpClient();

		String urlr = UrlPath.getMemberApi;

		String url = urlr + "?" + "phone_number=" + phone_number;
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

				String json = msg.getData().getString("responseBody");

				try {
					JSONObject item = new JSONObject(json);

					personcreated = item.getString("created");
					personname = item.getString("name");
					personportrait = UrlPath.host + item.getString("portrait");
					personlocation = item.getString("location");

					SharedPreferences mySharedPreferences = MyApplication
							.getAppContext().getSharedPreferences(
									"OtherHttpPerson", Activity.MODE_PRIVATE);
					// 实例化SharedPreferences.Editor对象（第二步）
					SharedPreferences.Editor editor1 = mySharedPreferences
							.edit();
					// 用putString的方法保存数据

					editor1.putString("personname", personname);
					editor1.putString("content", content);
					List<Map<String, String>> list = MySQLiteMethodDetails
							.listCommentToMessage_Db();
					int total = list.size();

					if (total == 0) {
						MySQLiteMethodDetails.insertcommentToMessage_Db(
								messageOrnews, id, phone_number,
								personportrait, personname, created, content,
								kind);
						total = 1;
					} else {
						for (int i = 0; i < list.size(); i++) {
							String comments_id = list.get(i).get("comment_id");

							if (!(comments_id.equals(id))) {
								num = num + 1;

							}
						}

					}
					if (num == total) {

						MySQLiteMethodDetails.insertcommentToMessage_Db(
								messageOrnews, id, phone_number,
								personportrait, personname, created, content,
								kind);
					}

					// editor1.putString("personportrait", personportrait);
					// editor1.putString("personlocation", personlocation);
					// editor1.putString("personcreated", personcreated);
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

//				Log.i("OtherHttpPerson", "失败失败！！！");
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] responseBody) {
				// TODO Auto-generated method stub
//				Log.i("OtherHttpPerson", "成功成功!!!!");
				String iString = new String(responseBody);
				Bundle bundle = new Bundle();
				bundle.putString("responseBody", iString);
				Message message = new Message();
				message.setData(bundle);
				handler.sendMessage(message);

			}
		});
		return;

	}
}

package app.example.netserver;

import net.loonggg.utils.DateUtil;
import net.loonggg.utils.DateUtil.DateFormatWrongException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import app.example.Url.UrlPath;
import app.example.application.MyApplication;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpGetWordsLoading {

	private String phone_number;
	private String to_phone_number;
	private String token;
	private String start;
	private String end;
	private String id, created, from_person, content, if_read;
	private int i;

	public HttpGetWordsLoading(String to_phone_number, String start, String end) {

		SharedPreferences mySharedPreferencesphone = MyApplication
				.getAppContext().getSharedPreferences("phone_number",
						Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		phone_number = mySharedPreferencesphone.getString("phone_number", "")
				.toString().trim();
		SharedPreferences mySharedPreferencestoken = MyApplication
				.getAppContext().getSharedPreferences("token",
						Activity.MODE_PRIVATE);
		token = mySharedPreferencestoken.getString("token", "");
		this.to_phone_number = to_phone_number;
		this.start = start;
		this.end = end;

	}

	public boolean  submitAsyncHttpClientPost() {
		// TODO Auto-generated method stub

		// 发送请求到服务器
		AsyncHttpClient client = new AsyncHttpClient();
		String url = UrlPath.wordsGetApi;

		// 创建请求参数
		RequestParams params = new RequestParams();
		params.add("phone_number", phone_number);
		params.add("token", token);
		params.add("start", start);
		params.add("end", end);
		params.add("to_phone_number", to_phone_number);

		// 执行post方法
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				// TODO Auto-generated method stub
			
				
				try {

					JSONObject item = new JSONObject();
					JSONArray jsonArray = new JSONArray(new String(arg2));
					int a = jsonArray.length();
					SharedPreferences mySharedPreferences = MyApplication
							.getAppContext().getSharedPreferences(
									"HttpGetWordsLoading", Activity.MODE_PRIVATE);
					// 实例化SharedPreferences.Editor对象（第二步）
					SharedPreferences.Editor editor = mySharedPreferences.edit();
					// 用putString的方法保存数据
					editor.putInt("HttpGetWordsLoading", a);
					// 提交当前数据
					editor.commit();
					for (i = 0; i < jsonArray.length(); i++) {
						item = jsonArray.getJSONObject(i);
						id = item.getString("id");
						try {
							created = DateUtil.toSimpleDate(item
									.getString("created"));
						} catch (DateFormatWrongException e) {
							e.printStackTrace();
						}
						content = item.getString("content");
						from_person = item.getString("from_person");
						if_read = item.getString("if_read");
//						Log.i("Get Words", "--id--" + id + "--created--"
//								+ created + "--content--" + content
//								+ "--from_person--" + from_person
//								+ "--if_read--" + if_read);
						HttpGetWordsPersonLoading httpGetWordsPersonLoading = new HttpGetWordsPersonLoading(
								from_person);
						httpGetWordsPersonLoading.submitAsyncHttpClientGet(i, id,
								created, from_person, content, if_read);
						httpGetWordsPersonLoading.submitAsyncHttpClientGet();
					}

				} catch (JSONException e) {
					
					e.printStackTrace();
				}

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				SharedPreferences mySharedPreferences = MyApplication
						.getAppContext().getSharedPreferences(
								"HttpGetWordsLoading", Activity.MODE_PRIVATE);
				// 实例化SharedPreferences.Editor对象（第二步）
				SharedPreferences.Editor editor = mySharedPreferences.edit();
				// 用putString的方法保存数据
				editor.putInt("HttpGetWordsLoading", -1);
				// 提交当前数据
				editor.commit();
//				Log.i("失败", "" + arg0);
			}
		});
		return true;

	}
}
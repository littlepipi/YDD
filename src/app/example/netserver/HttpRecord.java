package app.example.netserver;

import net.loonggg.utils.DateTime;
import net.loonggg.utils.DateUtil;
import net.loonggg.utils.DateUtil.DateFormatWrongException;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;
import app.example.Url.UrlPath;
import app.example.application.MyApplication;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class HttpRecord {
	private JSONArray news;
	private JSONArray messages;
	private String phone_number;
	String id, what, losttime, lostlatitude, lostlangitude, kind, information,
			commitperson, contactphone, sharenumber, commentsnumber,
			followingnumber, location, created, title, comments, latlng,
			image1, image2, image3, time;

	boolean flag;
	protected JSONArray followers;

	public HttpRecord(String phone_number, boolean flag) {
		this.phone_number = phone_number;
		this.flag = flag;
	}

	@SuppressLint("HandlerLeak")
	public void submitAsyncHttpClientGet() {
		// 创建异步请求端对象

		AsyncHttpClient client = new AsyncHttpClient();

		String urlr = UrlPath.rocordOperateApi;

		String url = urlr + phone_number;

		// 发送get请求对象
		client.get(url, new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				SharedPreferences successPreferences = MyApplication
						.getAppContext().getSharedPreferences("RECORD",
								Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = successPreferences.edit();
				// 用putString的方法保存数据
				editor.putInt("RECORD", 1);
				// 提交当前数据
				editor.commit();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] responseBody) {
				// TODO Auto-generated method stub
				// Log.i("HttpRecord", "成功成功!!!!");
				String iString = new String(responseBody);
				SharedPreferences successPreferences = MyApplication
						.getAppContext().getSharedPreferences("RECORD",
								Activity.MODE_PRIVATE);
				SharedPreferences.Editor editor = successPreferences.edit();
				// 用putString的方法保存数据
				editor.putInt("RECORD", 0);
				// 提交当前数据
				editor.commit();
				try {
					JSONObject jsonObject = new JSONObject(iString);
					news = jsonObject.getJSONArray("news");
					messages = jsonObject.getJSONArray("messages");
					JSONObject jsonnews = new JSONObject();
					JSONObject jsonmessage = new JSONObject();
					for (int i = 0; i < news.length(); i++) {
						jsonnews = news.getJSONObject(i);
						id = jsonnews.getString("id");
						/*********************************************/

						HttpComments myHttpComments = new HttpComments(id,
								"news");
						myHttpComments.submitAsyncHttpClientGet();
						/*********************************************/
						String lostimagestart = jsonnews.getString("image1");
						String pathpopu = UrlPath.host;
						image1 = pathpopu + lostimagestart;
						String lostimagestart1 = jsonnews.getString("image2");
						image2 = pathpopu + lostimagestart1;
						String lostimagestart3 = jsonnews.getString("image3");
						image3 = pathpopu + lostimagestart3;
						title = jsonnews.getString("title");
						information = jsonnews.getString("information");
						commitperson = jsonnews.getString("commit_person");

						try {
							time = DateUtil.toSimpleDate(jsonnews
									.getString("created"));
						} catch (DateFormatWrongException e) {

							e.printStackTrace();
						}
						sharenumber = jsonnews.getString("reading_number");
						followingnumber = jsonnews
								.getString("following_number");
						followers = jsonnews.getJSONArray("followers");
						commentsnumber = "1";
						// for (int j = 0; j < followers.length(); j++) {
						//
						// String oneperson = followers.getString(j);
						// // System.out.println("返回点赞人：：：" + oneperson);
						// if (commitperson.equals(oneperson)) {
						// commentsnumber = "0";
						// }
						// }
						HttpRecordNewsPerson httpRecordNewsPerson = new HttpRecordNewsPerson(
								phone_number);
						httpRecordNewsPerson.submitAsyncHttpClientGet(i, id,
								time, information, commitperson, title, image1,
								image2, image3, sharenumber, commentsnumber,
								followingnumber, flag);
						httpRecordNewsPerson.submitAsyncHttpClientGet();

					}
					for (int i = 0; i < messages.length(); i++) {
						jsonmessage = messages.getJSONObject(i);
						id = jsonmessage.getString("id");
						/*********************************************/

						HttpComments myHttpComments = new HttpComments(id,
								"message");
						myHttpComments.submitAsyncHttpClientGet();
						/*********************************************/
						what = jsonmessage.getString("what");

						latlng = jsonmessage.getString("latlng");
						kind = jsonmessage.getString("kind");
						title = jsonmessage.getString("title");
						information = jsonmessage.getString("information");
						commitperson = jsonmessage.getString("commit_person");

						contactphone = jsonmessage.getString("contact_phone");
						sharenumber = jsonmessage.getString("share_number");
						followingnumber = jsonmessage
								.getString("reading_number");
						commentsnumber = jsonmessage
								.getString("comments_number");
						location = jsonmessage.getString("location");

						String lostimagestart = jsonmessage.getString("image1");
						String pathpopu = UrlPath.host;
						image1 = pathpopu + lostimagestart;
						String lostimagestart1 = jsonmessage
								.getString("image2");
						image2 = pathpopu + lostimagestart1;
						String lostimagestart3 = jsonmessage
								.getString("image3");
						image3 = pathpopu + lostimagestart3;

						try {
							losttime = DateTime.toSimpleDate(jsonmessage
									.getString("time"));
						} catch (net.loonggg.utils.DateTime.DateFormatWrongException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						try {
							created = DateUtil.toSimpleDate(jsonmessage
									.getString("created"));
						} catch (DateFormatWrongException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

						String[] sp = { "titude:", "gitude:" };// 关键字数组
						for (int k = 0; k < sp.length; k++) {
							String value = sp[k];// 取出一个关键字

							String ee = "," + value + ",";// 关键字前后加逗号
							latlng = latlng.replaceAll(value, ee);// 这句很关键，replaceAll有返回值，如不把返回值给vv，vv的值不会变
						}
						String[] result = latlng.split(",");// 按照逗号分割

						lostlatitude = result[2];
						lostlangitude = result[5];

						comments = null;
						HttpRecordMessagePerson httpRecordMessagePerson = new HttpRecordMessagePerson(
								commitperson);
						httpRecordMessagePerson.submitAsyncHttpClientGet(i, id,
								what, losttime, lostlatitude, lostlangitude,
								kind, information, commitperson, contactphone,
								sharenumber, commentsnumber, followingnumber,
								location, created, title, comments, image1,
								image2, image3, flag);
						httpRecordMessagePerson.submitAsyncHttpClientGet();

					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		return;

	}
}

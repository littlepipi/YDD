package app.example.netserver;

import java.util.List;
import java.util.Map;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import app.example.Url.UrlPath;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

public class HttpCommentsPerson {
	private String id;
	private String phone_number;
	private String comments_name, comments_head;
	private String comment_id;
	private String comment_phone;
	private String comments_time, comments_content;
	private int num = 0;

	public HttpCommentsPerson(String phone_number) {
		this.phone_number = phone_number;

	}

	public void submitAsyncHttpClientGet(String id, final String commentstime,
			final String commentscontent, String comment_id) {
		this.id = id;
		this.comments_time = commentstime;
		this.comments_content = commentscontent;
		this.comment_id = comment_id;

	}

	@SuppressLint("HandlerLeak")
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
					/**
					 * 判断添加数据库，关于comments_time的状态以及排序问题
					 */
					JSONObject item = new JSONObject(json);
					comment_phone = phone_number;
					comments_name = item.getString("name");
					comments_head = UrlPath.host + item.getString("portrait");
					List<Map<String, String>> llist = MySQLiteMethodDetails
							.listComment_Db();
					
					List<Map<String, String>> l2 = MySQLiteMethodDetails
							.list_NO();
					int NO2 = l2.size(); 
					int NO3 = 0;
//					Log.i("评论信息", "--" + id + "--" + comment_id + "--"
//							+ comment_phone + "--" + comments_head + "--"
//							+ comments_name + "--" + comments_time + "--"
//							+ comments_content);
					if (NO2 == 0) {
						MySQLiteMethodDetails.insert_NO(comment_id);
						NO2 = 1;
					} else {

						for (int i = 0; i < l2.size(); i++) {

							String comments_id = l2.get(i).get("NO");

							if (!(comments_id.equals(comment_id))) {
								NO3 = NO3 + 1;
							}
						}

					}

					if (NO3 == NO2) {

						MySQLiteMethodDetails.insert_NO(comment_id);

					}

					int total = llist.size();
					if (total == 0) {

						MySQLiteMethodDetails.insertcomment_Db(id, comment_id,
								comment_phone, comments_head, comments_name,
								comments_time, comments_content);
						total = 1;
					} else {
						for (int i = 0; i < llist.size(); i++) {
							/**
							 * 迭代本地数据库里的数据，与服务器返回的数据进行对比，
							 * 1、先进行本地服务器判断，当本地数据库数据comments_id为空时，
							 * 说明是这是之前为了引起视觉效果而添加的，并没有具体的comments_id， 所以下一步刪除，
							 * 2、再用本地数据库comments_id与服务器传过来的comments_id对比出，
							 * 迭代出本地数据库没有的数据， 3、将新数据导入本地数据库中保存
							 * 
							 * 
							 * 现在的问题，不同客户端本地数据库数据不同步问题 解决方案
							 * 1、本地数据库数据comments_id与服务器数据对比出
							 * ，本地数据库比服务器数据库多出的数据comments_id， 2、删除本地数据库多出的数据
							 */
							String comments_id = llist.get(i).get("comment_id");
							if (comments_id.equals("")) {

								int app_id = Integer.valueOf(
										llist.get(i).get("app_id")).intValue();
								MySQLiteMethodDetails.deleteComment_Db(app_id);
								num = num + 1;
							} else {
								if (!(comments_id.equals(comment_id))) {
									num = num + 1;
								}
							}

						}

						if (num == total) {

//							Log.i("已存入数据库信息--->", num + "--" + id + "--"
//									+ comment_id + "--" + comment_phone + "--"
//									+ comments_head + "--" + comments_name
//									+ "--" + comments_time + "--"
//									+ comments_content);
							MySQLiteMethodDetails.insertcomment_Db(id,
									comment_id, comment_phone, comments_head,
									comments_name, comments_time,
									comments_content);

						}

					}

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
//				Log.i("", "" + arg0 + new String(arg2));
//				Log.i("HttpCommentsPerson", "失败失败！！！");
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] responseBody) {
				// TODO Auto-generated method stub
//				Log.i("HttpCommentsPerson", "成功成功!!!!");
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

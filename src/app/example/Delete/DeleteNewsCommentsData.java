package app.example.Delete;

import org.apache.http.Header;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;
import app.example.Url.UrlPath;
import app.example.application.MyApplication;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class DeleteNewsCommentsData {

	public DeleteNewsCommentsData(String id, String comment_id) {
		SharedPreferences mySharedPreferencestoken = MyApplication
				.getAppContext().getSharedPreferences("token",
						Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		String token = mySharedPreferencestoken.getString("token", "")
				.toString().trim();
		SharedPreferences sharedPreferences = MyApplication.getAppContext()
				.getSharedPreferences("phone_number", Activity.MODE_PRIVATE);
		String phone_number = sharedPreferences.getString("phone_number", "")
				.toString().trim();
		// TODO Auto-generated constructor stub
		Log.i("Delete", "" + phone_number + "----" + id + "----" + token+"---"+comment_id);
		submitAsyncHttpClientPost(token, phone_number, id, "UNCOMMENT",
				comment_id);

	}

	protected void submitAsyncHttpClientPost(String token, String phone_number,
			String id, String operation, String comment_id) {
		// TODO Auto-generated method stub

		// final Handler handler = new Handler() {
		// @Override
		// public void handleMessage(Message msg) {
		// Toast.makeText(MyApplication.getAppContext(), "提交成功！",
		// Toast.LENGTH_LONG).show();
		//
		// }
		// };

		// 发送请求到服务器
		AsyncHttpClient client = new AsyncHttpClient();
		String url = UrlPath.newsOperateApi;
		RequestParams params = new RequestParams();
		params.put("token", token);
		params.put("phone_number", phone_number);
		params.put("operation", "UNCOMMENT");
		params.put("id", id);// 联系电话
		params.put("comment_id", comment_id);

		// 执行post方法
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				System.out.print("fanhui" + statusCode);

				System.out.print("删除评论成功");

				// 跳转activity必须放在主线程中
				// handler.sendEmptyMessage(0);
				/**************************************/
				System.out.print("fanhui" + responseBody);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// 打印错误信息
				System.out.print("删除评论失败");
				error.printStackTrace();

				System.out.print("fanhui" + responseBody);
				System.out.print("fanhui" + statusCode);

			}
		});
	}

}

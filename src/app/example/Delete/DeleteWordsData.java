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

public class DeleteWordsData {

	public DeleteWordsData(String id, String from_phone_number) {
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
		Log.i("Delete", "" + phone_number + "----" + id + "----" + token
				+ "---" + from_phone_number);
		submitAsyncHttpClientPost(token, phone_number, id, from_phone_number,
				phone_number);

	}

	protected void submitAsyncHttpClientPost(String token, String phone_number,
			String id, String from_phone_number, String to_phone_number) {
		// TODO Auto-generated method stub

		// 发送请求到服务器
		AsyncHttpClient client = new AsyncHttpClient();
		String url = UrlPath.wordsDeleteApi;
		RequestParams params = new RequestParams();
		params.put("token", token);
		params.put("phone_number", phone_number);
		params.put("from_phone_number", from_phone_number);
		params.put("id", id);// 联系电话
		params.add("to_phone_number", to_phone_number);

	
		// 执行post方法
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				System.out.print("留言删除返回码" + statusCode);
				System.out.print("删除评论成功");
				System.out.print("删除留言" + new String(responseBody));
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// 打印错误信息
				System.out.print("删除留言失败");
				System.out.print("删除留言" + new String(responseBody));
				System.out.print("留言删除返回码" + statusCode);
				error.printStackTrace();
			}
		});
	}

}

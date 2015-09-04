package app.example.activity;

import java.util.ArrayList;

import net.loonggg.utils.AddWordsMessage;
import net.loonggg.utils.AutoListView;
import net.loonggg.utils.AutoListView.ILoadListener;
import net.loonggg.utils.AutoListView.IReflashListener;
import net.loonggg.utils.DateUtil;
import net.loonggg.utils.DateUtil.DateFormatWrongException;
import net.loonggg.utils.NetManager;
import net.loonggg.utils.WordsAdapter;
import net.loonggg.utils.WordsAdapter.DeleteWords;
import net.loonggg.utils.WordsMessage;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import tx.ydd.app.R;
import android.R.interpolator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import app.example.Delete.DeleteWordsData;
import app.example.Url.UrlPath;
import app.example.application.MyApplication;
import app.example.netserver.HttpGetWords;
import app.example.netserver.HttpGetWordsLoading;
import app.example.netserver.HttpGetWordsPerson;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class MessageWords extends Activity implements IReflashListener,
		ILoadListener, OnItemClickListener, DeleteWords {

	private String id, created, if_read;
	private int i;
	LinearLayout linearLayout;
	EditText wordsEditText;
	Button wordsButton;
	private ImageButton backButton;
	private WordsAdapter adapter;
	private AutoListView listview;
	private String num = "20";
	int num2;
	boolean b = false;
	boolean flag = false;
	private String phone_number, token, from_person, Words_id, content,
			from_phone_person;
	private ArrayList<WordsMessage> wordsMessages = new ArrayList<WordsMessage>();
	private int HttpGetWords;
	private int width;
	private LinearLayout no_data_word;
	private LinearLayout no_net_word;

	@SuppressLint("InflateParams")
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_message_words);
		if (NetManager.isOpenNetwork(MessageWords.this)) {

			startReflash();
			no_net_word.setVisibility(View.GONE);
			submitAsyncHttpClientPost(phone_number, "0", "20");
		} else {
			no_net_word.setVisibility(View.VISIBLE);
		}

	}

	private void showList(final ArrayList<WordsMessage> wordsMessages) {
		if (adapter == null) {
			listview = (AutoListView) findViewById(R.id.fragment_message_words_listview);
			listview.setInterface((ILoadListener) this);
			listview.setInterface((IReflashListener) this);

			adapter = new WordsAdapter(MessageWords.this, wordsMessages);
			adapter.setInterface((DeleteWords) this);
			listview.setAdapter(adapter);
			listview.setOnItemClickListener(this);

		} else {
			adapter.onDateChange(wordsMessages);
		}
	}

	public void startReflash() {
		no_data_word = (LinearLayout) findViewById(R.id.no_data_word);
		no_net_word = (LinearLayout) findViewById(R.id.no_net_word);
		backButton = (ImageButton) findViewById(R.id.post_lost_message_back1);
		linearLayout = (LinearLayout) findViewById(R.id.words_comments_linearLayout);
		wordsEditText = (EditText) findViewById(R.id.words_comments_input_message);
		wordsButton = (Button) findViewById(R.id.words_comments_send_message);
		width = wordsButton.getLayoutParams().width;
		SharedPreferences MyHttpPerson = MyApplication.getAppContext()
				.getSharedPreferences("phone_number", Activity.MODE_PRIVATE);

		phone_number = MyHttpPerson.getString("phone_number", "");
		SharedPreferences mySharedPreferencestoken = getSharedPreferences(
				"token", Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		token = mySharedPreferencestoken.getString("token", "");
		SharedPreferences mySharedPreferences = MyApplication.getAppContext()
				.getSharedPreferences("HttpGetWords", Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		HttpGetWords = mySharedPreferences.getInt("HttpGetWords", 0);
		wordsMessages = new ArrayList<WordsMessage>();

	}

	@Override
	public void onReflash() {
		// TODO Auto-generated method stub\
		wordsMessages = new ArrayList<WordsMessage>();
//		Log.i("刷新", "刷新中");
		HttpGetWords httpGetWords = new HttpGetWords(phone_number, "0", "20");
		httpGetWords.submitAsyncHttpClientPost();

		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				SharedPreferences mySharedPreferences = MyApplication.getAppContext()
						.getSharedPreferences("HttpGetWords", Activity.MODE_PRIVATE);
				// 使用getString方法获得value，注意第2个参数是value的默认值
				int HttpGetWords = mySharedPreferences.getInt("HttpGetWords", 0);
					if (HttpGetWords > 0) {
						Toast.makeText(MessageWords.this, "刷新成功",
								Toast.LENGTH_SHORT).show();
						AddWordsMessage.AddWordsMessages(HttpGetWords, 0,
								wordsMessages);
						showList(wordsMessages);
					} else if (HttpGetWords == 0) {
						Toast.makeText(MessageWords.this, "已加载全部！！！",
								Toast.LENGTH_SHORT).show();
					} else if (HttpGetWords == -1) {
						Toast.makeText(MessageWords.this, "刷新失败",
								Toast.LENGTH_SHORT).show();
					}
					
					// 通知listview 刷新数据完毕；
					listview.reflashComplete();
//					Log.i("恭喜", "刷新成功");
					flag = true;

				
			}
		}, 2000);

	}

	@Override
	public void onLoad() {

		if (flag) {
			num = "20";

			flag = false;
		}
		num2 = Integer.parseInt(num);
		num2 = num2 + 20;

		HttpGetWordsLoading httpGetWordsLoading = new HttpGetWordsLoading(
				phone_number, num, String.valueOf(num2));
		httpGetWordsLoading.submitAsyncHttpClientPost();
		Handler handler = new Handler();
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 获取更多数据// 从登录获取令牌token
				SharedPreferences mySharedPreferencestoken = MyApplication
						.getAppContext().getSharedPreferences(
								"HttpGetWordsLoading", Activity.MODE_PRIVATE);
				// 使用getString方法获得value，注意第2个参数是value的默认值
				int HttpGetWordsLoading = mySharedPreferencestoken.getInt(
						"HttpGetWordsLoading", 1);

				// getLoadData();
				if (HttpGetWordsLoading > 0) {
					Toast.makeText(MessageWords.this, "加载成功",
							Toast.LENGTH_SHORT).show();
					AddWordsMessage.AddWordsMessages(HttpGetWordsLoading, 1,
							wordsMessages);
					// 更新listview显示；
					showList(wordsMessages);
					// 通知listview加载完毕
					
					int num3 = Integer.parseInt(String.valueOf(num2));
					num3 = num3 + 20;
					num = String.valueOf(num3);
					// 用于退出该页面时，清空SHareprefences保留的EditView 内容用到

				} else if (HttpGetWordsLoading == 0) {
					Toast.makeText(MessageWords.this, "已加载全部！！！",
							Toast.LENGTH_SHORT).show();
				}else if (HttpGetWordsLoading == 0) {
					Toast.makeText(MessageWords.this, "加载失败",
							Toast.LENGTH_SHORT).show();
				}

				listview.loadComplete();

			}
		}, 1000);
	}

	/****************************************************************/
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

		if (NetManager.isOpenNetwork(MessageWords.this)) {
			WordsMessage wordsMessage = wordsMessages.get(position - 1);

			from_phone_person = wordsMessage.getWords_from_person();
			wordsEditText.setHint("请回复留言信息");
			linearLayout.setVisibility(View.VISIBLE);
			InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.toggleSoftInput(0,
					InputMethodManager.HIDE_NOT_ALWAYS);

			wordsEditText.setEnabled(true);
			wordsEditText.setFocusable(true);
			wordsEditText.setFocusableInTouchMode(true);
			wordsEditText.requestFocus();
		} else {
			Toast.makeText(MessageWords.this, "暂无网络连接", Toast.LENGTH_SHORT)
					.show();
		}

	}

	public void Click() {
		wordsButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				content = wordsEditText.getText().toString();
				submitAsyncHttpClientPost(phone_number, token,
						from_phone_person, content);
				wordsEditText.setText("");
				linearLayout.setVisibility(View.GONE);
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(wordsEditText.getWindowToken(), 0);
				wordsEditText.setEnabled(true);
				wordsEditText.setFocusable(true);
				wordsEditText.setFocusableInTouchMode(true);
				wordsEditText.requestFocus();
			}
		});
		backButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	@Override
	public void ondeletewords(int position) {
		// TODO Auto-generated method stub
		ShowPickDialog(position);

	}

	private void ShowPickDialog(final int position) {
		// TODO Auto-generated method stub
		new AlertDialog.Builder(this)
				.setTitle("你是真的真的不要我了么??")
				.setNegativeButton("确定", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						WordsMessage wordsMessage = wordsMessages.get(position);
						Words_id = wordsMessage.getWords_id();
						from_person = wordsMessage.getWords_from_person();
						@SuppressWarnings("unused")
						DeleteWordsData deleteWords = new DeleteWordsData(
								Words_id, from_person);
						wordsMessages.remove(position);
						adapter.notifyDataSetChanged();
					}
				})
				.setPositiveButton("取消", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.dismiss();

					}
				}).show();
	}

	protected void submitAsyncHttpClientPost(String phone_number, String token,
			String to_phone_number, String content) {
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

		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {

			}
		};

		// 发送请求到服务器
		AsyncHttpClient client = new AsyncHttpClient();
		String url = UrlPath.wordsAddApi;

		// 创建请求参数
		RequestParams params = new RequestParams();

		params.put("token", token);

		params.put("phone_number", phone_number);

		params.put("to_phone_number", to_phone_number);

		params.put("content", content);

		// 执行post方法
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {

				Toast.makeText(MessageWords.this, "留言成功", Toast.LENGTH_SHORT)
						.show();
				// 跳转activity必须放在主线程中
				handler.sendEmptyMessage(0);

			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				// 打印错误信息
				error.printStackTrace();

////				Log.i("提交状态", "" + "提交失败");
//				String i = new String(responseBody);
//				Log.i("statusCode", "" + statusCode);
//				Log.i("responseBody", "" + i);

			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		finish();
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (isShouldHideInput(v, ev)) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
			return super.dispatchTouchEvent(ev);
		}
		// 必不可少，否则所有的组件都不会有TouchEvent了
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}

	public boolean isShouldHideInput(View v, MotionEvent event) {

		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			// 获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth() + width;
			;
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击的是输入框区域，保留点击EditText的事件

				return false;
			} else {
				linearLayout.setVisibility(View.GONE);
				wordsEditText.setText("");
				return true;
			}
		}
		return false;
	}

	public boolean submitAsyncHttpClientPost(String to_phone_number,
			String start, String end) {

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

		final Handler handler = new Handler() {

			@Override
			public void handleMessage(Message msg) {

				String json = msg.getData().getString("responseBody");
				try {

					JSONObject item = new JSONObject();
					JSONArray jsonArray = new JSONArray(json);
					int a = jsonArray.length();
					SharedPreferences mySharedPreferences = MyApplication
							.getAppContext().getSharedPreferences(
									"HttpGetWords", Activity.MODE_PRIVATE);
					// 实例化SharedPreferences.Editor对象（第二步）
					SharedPreferences.Editor editor = mySharedPreferences
							.edit();
					// 用putString的方法保存数据
					editor.putInt("HttpGetWords", a);
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
						HttpGetWordsPerson httpGetWordsPerson = new HttpGetWordsPerson(
								from_person);
						httpGetWordsPerson.submitAsyncHttpClientGet(i, id,
								created, from_person, content, if_read);
						httpGetWordsPerson.submitAsyncHttpClientGet();
					}
					Click();
					wordsMessages = new ArrayList<WordsMessage>();
					if (HttpGetWords != 0) {
						no_data_word.setVisibility(View.GONE);
						AddWordsMessage.AddWordsMessages(HttpGetWords, 0,
								wordsMessages);
					} else if (HttpGetWords == 0) {

						no_data_word.setVisibility(View.VISIBLE);
					}

					showList(wordsMessages);
				} catch (JSONException e) {

					e.printStackTrace();
				}
			}
		};

		// 执行post方法
		client.post(url, params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {

				String iString = new String(arg2);
				Bundle bundle = new Bundle();
				bundle.putString("responseBody", iString);
				Message message = new Message();
				message.setData(bundle);
				handler.sendMessage(message);

			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
//				Log.i("失败", "" + arg0);
			}
		});
		return true;

	}

}

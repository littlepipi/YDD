package net.loonggg.fragment;

import tx.ydd.app.R;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import app.example.activity.MessageComments;
import app.example.activity.MessageSystem;
import app.example.activity.MessageWords;
import app.example.application.MyApplication;

public class FragmentMessage extends Fragment {
	private String token;
	View view;
	RelativeLayout comment_rela, words_rela, system_rela;
	TextView comment_no, words_no, system_no;
	private String intS, intC, intW;
	ImageView comment_png_no, words_png_no, system_png_no;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_message, null);
		SharedPreferences mySharedPreferencestoken = getActivity()
				.getSharedPreferences("token", Activity.MODE_PRIVATE);
		// 使用getString方法获得value，注意第2个参数是value的默认值
		token = mySharedPreferencestoken.getString("token", "").toString()
				.trim();

		return view;
	}

	private void Receiver() {
		SharedPreferences system_ = MyApplication.getAppContext()
				.getSharedPreferences("system_inte", Activity.MODE_PRIVATE);
		intS = system_.getString("system_ints", "0").toString().trim();
		
		
		
		SharedPreferences comment_ = MyApplication.getAppContext()
				.getSharedPreferences("comment_inte", Activity.MODE_PRIVATE);
		intC = comment_.getString("comment_ints", "0").toString().trim();

		
		
	}

	public void init() {

		comment_no = (TextView) view.findViewById(R.id.comment_no);
		words_no = (TextView) view.findViewById(R.id.words_no);
		system_no = (TextView) view.findViewById(R.id.system_no);
		comment_png_no = (ImageView) view.findViewById(R.id.comment_png_no);
		words_png_no = (ImageView) view.findViewById(R.id.words_png_no);
		system_png_no = (ImageView) view.findViewById(R.id.system_png_no);
		if (!("0".equals(intC))) {
			comment_no.setVisibility(View.VISIBLE);
			comment_png_no.setVisibility(View.VISIBLE);
			comment_no.setText(intC);
		} else {
			comment_no.setVisibility(View.GONE);
			comment_png_no.setVisibility(View.GONE);
		}
		if (!("0".equals(intS))) {
			system_no.setVisibility(View.VISIBLE);
			system_png_no.setVisibility(View.VISIBLE);
			system_no.setText(intS);
		} else {
			system_no.setVisibility(View.GONE);
			system_png_no.setVisibility(View.GONE);
		}

		words_no.setVisibility(View.GONE);
		words_png_no.setVisibility(View.GONE);
		comment_rela = (RelativeLayout) view.findViewById(R.id.comment_rela);
		words_rela = (RelativeLayout) view.findViewById(R.id.words_rela);
		system_rela = (RelativeLayout) view.findViewById(R.id.system_rela);

	}

	public void Clicked() {

		words_rela.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("".equals(token)) {
					Toast.makeText(MyApplication.getAppContext(), "您还未登录哦",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(getActivity(),
							MessageWords.class);
					getActivity().startActivity(intent);

				}

			}
		});

		comment_rela.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if ("".equals(token)) {
					Toast.makeText(MyApplication.getAppContext(), "您还未登录哦",
							Toast.LENGTH_SHORT).show();
				} else {
					Intent intent = new Intent(getActivity(),
							MessageComments.class);
					getActivity().startActivity(intent);
				}
			}
		});
		system_rela.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				if ("".equals(token)) {
//					Toast.makeText(MyApplication.getAppContext(), "您还未登录哦",
//							Toast.LENGTH_SHORT).show();
//				} else {
					Intent intent = new Intent(getActivity(),
							MessageSystem.class);
					getActivity().startActivity(intent);
//				}
			}
		});

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

		Receiver();
		init();
		Clicked();

		super.onResume();
	}

}

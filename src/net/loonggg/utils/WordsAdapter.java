package net.loonggg.utils;

import java.util.ArrayList;

import tx.ydd.app.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import app.example.internet.picture.ImageLoader;

public class WordsAdapter extends BaseAdapter {
	ArrayList<WordsMessage> words_num;
	LayoutInflater inflater;
	public ImageLoader imageLoader;
	private String id, created, from_person, content, if_read, personname,
			personportrait;
	DeleteWords deleteWords;
	public WordsAdapter(Context context, ArrayList<WordsMessage> words_num) {
		this.words_num = words_num;
		this.inflater = LayoutInflater.from(context);
		imageLoader = new ImageLoader(context);

	}

	public void onDateChange(ArrayList<WordsMessage> words_num) {
		this.words_num = words_num;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return words_num.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return words_num.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void additem() {

	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final WordsMessage wordsMessage = words_num.get(position);
		final ViewHolder holder;

		id = wordsMessage.getWords_id();
		created = wordsMessage.getWordsTime();
		from_person = wordsMessage.getWords_from_person();
		content = wordsMessage.getWordsContent();
		personportrait = wordsMessage.getWordsHeader();
		personname = wordsMessage.getWordsNickname();
		if_read = wordsMessage.getWords_if_read();

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.fragment_message_words_item, null);
			holder.linearLayout = (LinearLayout) convertView
					.findViewById(R.id.fragment_message_words_item);

			holder.wordsheader = (ImageView) convertView
					.findViewById(R.id.fragment_message_words_item_header);
			holder.wordsnickname = (TextView) convertView
					.findViewById(R.id.fragment_message_words_item_nickname);
		
			holder.wordscontent = (TextView) convertView
					.findViewById(R.id.fragment_message_words_item_content);
			holder.wordstime = (TextView) convertView
					.findViewById(R.id.fragment_message_words_item_time);
   
			
			holder.wordsdelete = (TextView) convertView
					.findViewById(R.id.fragment_message_words_item_delete);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		imageLoader.DisplayImage(personportrait, holder.wordsheader);
		
		holder.wordsnickname.setText(personname);
		holder.wordstime.setText(created);
		holder.wordscontent.setText(content);
		holder.wordsdelete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteWords.ondeletewords(position);
			}
		});
		
		
		
		return convertView;
	}
	public interface DeleteWords {

		public void ondeletewords(int position);

	}

	public void setInterface(DeleteWords deleteWords) {

		this.deleteWords = deleteWords;
	}
	class ViewHolder {
		LinearLayout linearLayout;

		TextView wordscontent, wordstime, wordsnickname,
				wordsdelete;

		ImageView wordsheader;

	}
}

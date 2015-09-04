package net.loonggg.utils;

import tx.ydd.app.R;
import android.widget.TextView;

public class Lable {
	public static void lable(String kind, TextView textView) {

		if (kind != null) {
			switch (kind) {
			case "钱包":
				textView.setBackgroundResource(R.drawable.lable_perse_num_two);
				break;
			case "证件":
				textView.setBackgroundResource(R.drawable.lable_card_num_two);
				break;
			case "钥匙":
				textView.setBackgroundResource(R.drawable.lable_keys_num_two);
				break;
			case "数码":
				textView.setBackgroundResource(R.drawable.lable_digital_num_two);
				break;
			case "饰品":
				textView.setBackgroundResource(R.drawable.lable_ring_num_two);
				break;
			case "衣服":
				textView.setBackgroundResource(R.drawable.lable_clothes_num_two);
				break;
			case "文件":
				textView.setBackgroundResource(R.drawable.lable_file_num_two);
				break;
			case "书籍":
				textView.setBackgroundResource(R.drawable.label_book_num_two);
				break;
			case "宠物":
				textView.setBackgroundResource(R.drawable.lable_pat_num_two);
				break;
			case "找人":
				textView.setBackgroundResource(R.drawable.lable_people_num_two);
				break;
			case "车辆":
				textView.setBackgroundResource(R.drawable.lable_car_num_two);
				break;
			case "其他":
				textView.setBackgroundResource(R.drawable.lable_more_num_two);
				break;

			default:
				break;
			}
		}
	}
}

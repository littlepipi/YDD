package net.loonggg.utils;

import tx.ydd.app.R;
import android.widget.TextView;

public class LableHomePage {
	public static void lable(String kind, TextView textView) {

		if (kind != null) {
			switch (kind) {
			case "全部":
				textView.setBackgroundResource(R.drawable.all_ch);
				break;
			case "钱包":
				textView.setBackgroundResource(R.drawable.purse_ch);
				break;
			case "证件":
				textView.setBackgroundResource(R.drawable.car_ch);
				break;
			case "钥匙":
				textView.setBackgroundResource(R.drawable.keys_ch);
				break;
			case "数码":
				textView.setBackgroundResource(R.drawable.digital_ch);
				break;
			case "饰品":
				textView.setBackgroundResource(R.drawable.ring_ch);
				break;
			case "衣服":
				textView.setBackgroundResource(R.drawable.cloth_ch);
				break;
			case "文件":
				textView.setBackgroundResource(R.drawable.file_ch);
				break;
			case "书籍":
				textView.setBackgroundResource(R.drawable.book_ch);
				break;
			case "宠物":
				textView.setBackgroundResource(R.drawable.pat_ch);
				break;
			case "找人":
				textView.setBackgroundResource(R.drawable.people_ch);
				break;
			case "车辆":
				textView.setBackgroundResource(R.drawable.car_ch);
				break;
			case "其他":
				textView.setBackgroundResource(R.drawable.others_ch);
				break;

			default:
				break;
			}
		}
	}
}

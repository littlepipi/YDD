package net.loonggg.utils;

import tx.ydd.app.R;
import android.widget.TextView;

public class LableHomePage {
	public static void lable(String kind, TextView textView) {

		if (kind != null) {
			switch (kind) {
			case "ȫ��":
				textView.setBackgroundResource(R.drawable.all_ch);
				break;
			case "Ǯ��":
				textView.setBackgroundResource(R.drawable.purse_ch);
				break;
			case "֤��":
				textView.setBackgroundResource(R.drawable.car_ch);
				break;
			case "Կ��":
				textView.setBackgroundResource(R.drawable.keys_ch);
				break;
			case "����":
				textView.setBackgroundResource(R.drawable.digital_ch);
				break;
			case "��Ʒ":
				textView.setBackgroundResource(R.drawable.ring_ch);
				break;
			case "�·�":
				textView.setBackgroundResource(R.drawable.cloth_ch);
				break;
			case "�ļ�":
				textView.setBackgroundResource(R.drawable.file_ch);
				break;
			case "�鼮":
				textView.setBackgroundResource(R.drawable.book_ch);
				break;
			case "����":
				textView.setBackgroundResource(R.drawable.pat_ch);
				break;
			case "����":
				textView.setBackgroundResource(R.drawable.people_ch);
				break;
			case "����":
				textView.setBackgroundResource(R.drawable.car_ch);
				break;
			case "����":
				textView.setBackgroundResource(R.drawable.others_ch);
				break;

			default:
				break;
			}
		}
	}
}

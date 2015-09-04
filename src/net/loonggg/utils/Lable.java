package net.loonggg.utils;

import tx.ydd.app.R;
import android.widget.TextView;

public class Lable {
	public static void lable(String kind, TextView textView) {

		if (kind != null) {
			switch (kind) {
			case "Ǯ��":
				textView.setBackgroundResource(R.drawable.lable_perse_num_two);
				break;
			case "֤��":
				textView.setBackgroundResource(R.drawable.lable_card_num_two);
				break;
			case "Կ��":
				textView.setBackgroundResource(R.drawable.lable_keys_num_two);
				break;
			case "����":
				textView.setBackgroundResource(R.drawable.lable_digital_num_two);
				break;
			case "��Ʒ":
				textView.setBackgroundResource(R.drawable.lable_ring_num_two);
				break;
			case "�·�":
				textView.setBackgroundResource(R.drawable.lable_clothes_num_two);
				break;
			case "�ļ�":
				textView.setBackgroundResource(R.drawable.lable_file_num_two);
				break;
			case "�鼮":
				textView.setBackgroundResource(R.drawable.label_book_num_two);
				break;
			case "����":
				textView.setBackgroundResource(R.drawable.lable_pat_num_two);
				break;
			case "����":
				textView.setBackgroundResource(R.drawable.lable_people_num_two);
				break;
			case "����":
				textView.setBackgroundResource(R.drawable.lable_car_num_two);
				break;
			case "����":
				textView.setBackgroundResource(R.drawable.lable_more_num_two);
				break;

			default:
				break;
			}
		}
	}
}

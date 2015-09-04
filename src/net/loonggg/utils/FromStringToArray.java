package net.loonggg.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FromStringToArray {

	public static List<String> MyArray(String string) {
		List<String> digitList = null;
		digitList = new ArrayList<String>();
		/**
		 * [1,2,3,4......]----->Array
		 */
		if ("[]".equals(string) || string == null) {
			System.out.println(digitList);
		} else {
			Pattern p = Pattern.compile("[^0-9]");
			Matcher m = p.matcher(string);
			String result = m.replaceAll("");
			for (int i = 0; i < result.length(); i++) {
				digitList.add(result.substring(i, i + 1));

			}
			System.out.println(digitList);
		}

		return digitList;

	}

}

package net.loonggg.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateTime {
	private static final Pattern pat=Pattern.compile("(\\d{4})-(\\d{2})-(\\d{2})T(\\d{2}):(\\d{2}):(\\d{2})");
	
	public static String toSimpleDate(String input) throws DateFormatWrongException {
		Matcher mat=pat.matcher(input);
		if (!mat.find()) {
			throw new DateFormatWrongException(input);
		}
		StringBuilder builder=new StringBuilder();
		builder.append(mat.group(1)).append("-").append(mat.group(2)).append("-").append(mat.group(3)).append(" ").append(mat.group(4)).append(":").append(mat.group(5)).append(":").append(mat.group(6));
		return builder.toString();
	}
	
	public static class DateFormatWrongException extends Exception{

		public DateFormatWrongException(String string) {
			super(string);
		}

		private static final long serialVersionUID = 1L;
		
	}

}

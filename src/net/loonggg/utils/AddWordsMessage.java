package net.loonggg.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import app.example.netserver.MySQLiteMethodDetails;

public class AddWordsMessage {

	public static void AddWordsMessages(int num, int kind,
			ArrayList<WordsMessage> words_message) {

		// TODO Auto-generated constructor stub

		final List<Map<String, String>> list = MySQLiteMethodDetails
				.listWords_Db();
		if (kind == 0) {

			for (int i = 0; i <= num - 1; i++) {

				final WordsMessage wordsMessage = new WordsMessage();

				wordsMessage.setWords_id(list.get(i).get("id"));
				wordsMessage.setWordsTime(list.get(i).get("created"));
				wordsMessage.setWordsContent(list.get(i).get("content"));
				wordsMessage.setWords_from_person(list.get(i)
						.get("from_person"));
				wordsMessage.setWords_if_read(list.get(i).get("if_read"));
				wordsMessage.setWordsHeader(list.get(i).get("personportrait"));
				wordsMessage.setWordsNickname(list.get(i).get("personname"));

				words_message.add(wordsMessage);
				// }
			}
		} else if (kind == 1) {

			for (int i = 5; i <= num + 4; i++) {
				final WordsMessage wordsMessage = new WordsMessage();

				wordsMessage.setWords_id(list.get(i).get("id"));
				wordsMessage.setWordsTime(list.get(i).get("created"));
				wordsMessage.setWordsContent(list.get(i).get("content"));
				wordsMessage.setWords_from_person(list.get(i)
						.get("from_person"));
				wordsMessage.setWords_if_read(list.get(i).get("if_read"));
				wordsMessage.setWordsHeader(list.get(i).get("personportrait"));
				wordsMessage.setWordsNickname(list.get(i).get("personname"));

				words_message.add(wordsMessage);

			}
		}

	}

}

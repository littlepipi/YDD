package net.loonggg.utils;

public class WordsMessage {
	private String wordsheader;
	private String wordsnickname;
	private String wordstime;
	private String wordscontent;
	private String words_from_person;
	private String words_if_read;
	private String words_id;

	/**
	 * ����id
	 **/

	public String getWords_id() {
		return words_id;
	}

	public void setWords_id(String words_id) {
		this.words_id = words_id;
	}

	/**
	 * ������ͷ��
	 **/

	public String getWordsHeader() {
		return wordsheader;
	}

	public void setWordsHeader(String wordsheader) {
		this.wordsheader = wordsheader;
	}

	/**
	 * �������ǳ�
	 **/
	public String getWordsNickname() {
		return wordsnickname;
	}

	public void setWordsNickname(String wordsnickname) {
		this.wordsnickname = wordsnickname;
	}

	/**
	 * ����ʱ��
	 **/
	public String getWordsTime() {
		return wordstime;
	}

	public void setWordsTime(String wordstime) {
		this.wordstime = wordstime;
	}

	/**
	 * �����¼�
	 **/
	public String getWordsContent() {
		return wordscontent;
	}

	public void setWordsContent(String wordscontent) {
		this.wordscontent = wordscontent;
	}

	/**
	 * �����˺���
	 **/
	public String getWords_from_person() {
		return words_from_person;
	}

	public void setWords_from_person(String words_from_person) {
		this.words_from_person = words_from_person;
	}

	/**
	 * �����Ƿ��Ķ�
	 **/
	public String getWords_if_read() {
		return words_if_read;
	}

	public void setWords_if_read(String words_if_read) {
		this.words_if_read = words_if_read;
	}
}

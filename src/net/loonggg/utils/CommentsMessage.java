package net.loonggg.utils;

public class CommentsMessage {
	private String commentsheader;
	private String commentsnickname;
	private String commentstime;
	private String commentscontent;
	private String comment_id;
	private String comment_phone;
    private String app_id;
    private String id;
    private String comment_kind;
    /**
	 * ����id
	 **/

	public String get_id() {
		return id;
	}

	public void set_id(String id) {
		this.id = id;
	}
    
    /**
	 * ����app_id
	 **/

	public String getComment_app_id() {
		return app_id;
	}

	public void setComment_app_id(String app_id) {
		this.app_id = app_id;
	}
	/**
	 * ����id
	 **/

	public String getComment_id() {
		return comment_id;
	}

	public void setComment_id(String comment_id) {
		this.comment_id = comment_id;
	}

	/**
	 * �����ֻ���
	 **/

	public String getComment_phone() {
		return comment_phone;
	}

	public void setComment_phone(String comment_phone) {
		this.comment_phone = comment_phone;
	}

	/**
	 * ������ͷ��
	 **/

	public String getCommentsHeader() {
		return commentsheader;
	}

	public void setCommentsHeader(String commentsheader) {
		this.commentsheader = commentsheader;
	}

	/**
	 * �������ǳ�
	 **/
	public String getCommentsNickname() {
		return commentsnickname;
	}

	public void setCommentsNickname(String commentsnickname) {
		this.commentsnickname = commentsnickname;
	}

	/**
	 * ����ʱ��
	 **/
	public String getCommentsTime() {
		return commentstime;
	}

	public void setCommentsTime(String commentstime) {
		this.commentstime = commentstime;
	}

	/**
	 * �����¼�
	 **/
	public String getCommentsContent() {
		return commentscontent;
	}

	public void setCommentsContent(String commentscontent) {
		this.commentscontent = commentscontent;
	}
	/**
	 * ��������
	 **/
	public String getCommentkind() {
		return comment_kind;
	}

	public void setCommentskind(String comment_kind) {
		this.comment_kind = comment_kind;
	}
}

package net.loonggg.utils;

public class SystemMessage {
	private String content;
	private String created;	
    private String app_id;
    private String id;
 
    /**
	 * 评论id
	 **/

	public String get_id() {
		return id;
	}

	public void set_id(String id) {
		this.id = id;
	}
    
    /**
	 * 评论app_id
	 **/

	public String getapp_id() {
		return app_id;
	}

	public void setapp_id(String app_id) {
		this.app_id = app_id;
	}
	

	/**
	 * 系统时间
	 **/
	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	/**
	 * 系统传递事件
	 **/
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}

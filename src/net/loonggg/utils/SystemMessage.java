package net.loonggg.utils;

public class SystemMessage {
	private String content;
	private String created;	
    private String app_id;
    private String id;
 
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

	public String getapp_id() {
		return app_id;
	}

	public void setapp_id(String app_id) {
		this.app_id = app_id;
	}
	

	/**
	 * ϵͳʱ��
	 **/
	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	/**
	 * ϵͳ�����¼�
	 **/
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}

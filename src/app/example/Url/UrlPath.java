package app.example.Url;

public class UrlPath {

	// public final static String host = "http://49.140.166.27:8000/";
	public final static String host = "http://123.56.150.30:8000/";
	public final static String registApi = host + "api/member/regist/";// 注册
	public final static String loginApi = host + "api/member/login/";// 登陆
	public final static String getMemberApi = host + "api/member/get/";// 获取成员信息
	public final static String patchApi = host + "api/member/patch/";// 修改成员信息
	public final static String messageOperateApi = host
			+ "api/messages/operate/";// 操作，评论等
	public final static String messageAddApi = host + "api/messages/add/";// 添加失物招领等信息
	public final static String messagesGetApi = host + "api/messages/get/";// 获取失物招领等信息
	public final static String commentGetApi = host + "api/comments/get/";// 获取评论
	public final static String logoutApi = host + "api/member/logout/";// 注销
	public final static String wordsAddApi = host + "api/words/add/";// 留言
	public final static String wordsGetApi = host + "api/words/get/";// 获取留言
	public final static String wordsDeleteApi = host + "api/words/delete/";// 删除留言
	public final static String getSystemMsgApi = host
			+ "api/system_messages/get/";// 获取系统信息
	public final static String newsAddApi = host + "api/news/add/";//
	public final static String newsGetApi = host + "api/news/get/";//
	public final static String newsOperateApi = host + "api/news/operate/";//
	public final static String feedbackOperateApi = host + "api/feedback/";// 意见反馈
	public final static String versionOperateApi = host + "api/update/get/"
			+ "?" + "version=";// 软件更新
	public final static String rocordOperateApi = host
			+ "api/member/commited/?phone_number=";
	public final static String identifyOperateApi = host
			+ "api/verify_phone/?phone_number=";
	public final static String exist = host + "api/member/exist/?phone_number=";
	public final static String replies = host + "api/replies/get/";
	public final static String singlemessageApi = host
			+ "api/messages/get_one/";
	public final static String singlenewsApi = host + "api/news/get_one/";
	public final static String schoolserverApi = host + "api/ambassador/get/";

}

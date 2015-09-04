package app.example.netserver;

public class PersonData {
	private String id;
	private String app_id;
	private String what;
	private String title;
	private String time;
	private String place;
	private String latitude;
	private String langitude;
	private String kind;
	private String information;
	private String commitperson;
	private String contactphone;
	private String sharenumber;
	private String commentsnumber;
	private String followingnumber;
	private String location;
	private String created;
	private String comments;
	
	private String image1;
	private String image2;
	private String image3, personcreated, personname, personportrait,
			personlocation;

	/**
	 * app_id
	 **/

	public String getapp_Id() {
		return app_id;
	}

	public void setapp_Id(String app_id) {
		this.app_id = app_id;
	}
	/**
	 * id
	 **/

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 标题
	 **/
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 名称
	 **/
	public String getWhat() {
		return what;
	}

	public void setWhat(String what) {
		this.what = what;
	}

	/**
	 * 时间
	 **/
	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time=time;
	}

	/**
	 * 地点
	 **/

	public String getPlace() {
		return place;
	}

	public void setPlace(String  place) {
		this.place = place;
	}

	/**
	 * 经度
	 **/
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude =latitude;
	}

	/**
	 * 纬度
	 **/
	public String getLangitude() {
		return  langitude;
	}

	public void setLangitude(String langitude) {
		this.langitude = langitude;
	}

	/**
	 * 物品详情
	 **/
	public String getInformation() {
		return information;
	}

	public void setInformation(String information) {
		this.information=information;
	}

	/**
	 * 物品类别
	 **/
	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind =kind;
	}

	/**
	 * 提交个人
	 **/
	public String getCommitPerson() {
		return commitperson;
	}

	public void setCommitPerson(String commitperson) {
		this.commitperson = commitperson;
	}

	/**
	 * 提交机构
	 **/
	public String getContactPhone() {
		return contactphone;
	}

	public void setContactPhone(String contactphone) {
		this.contactphone = contactphone;
	}

	/**
	 * 分享数目
	 **/
	public String getShareNumber() {
		return sharenumber;
	}

	public void setShareNumber(String sharenumber) {
		this.sharenumber = sharenumber;
	}

	/**
	 * 关注数目
	 **/
	public String getFollowingNumber() {
		return followingnumber;
	}

	public void setFollowingNumber(String followingnumber) {
		this.followingnumber = followingnumber;
	}

	/**
	 * 评论数目
	 **/
	public String getCommentsNumber() {
		return commentsnumber;
	}

	public void setCommentsNumber(String commentsnumber) {
		this.commentsnumber = commentsnumber;
	}

	/**
	 * 地点
	 **/
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * 时间
	 **/
	public String getCreated() {
		return created;
	}

	public void setCreated(String created) {
		this.created = created;
	}

	/**
	 * 评论内容
	 * 
	 * @return
	 */
	public String  getComments() {
		return comments;
	}

	public void setComments(String  comments) {
		this.comments = comments;
	}


	/**
	 * 失物图片1
	 * 
	 * @return
	 */
	public String getImage1() {
		return  image1;
	}

	public void setImage1(String image1) {
		this.image1 = image1;
	}

	/**
	 * 失物图片2
	 * 
	 * @return
	 */
	public String getImage2() {
		return  image2;
	}

	public void setImage2(String image2) {
		this.image2 =image2;
	}

	/**
	 * 失物图片3
	 * 
	 * @return
	 */
	public String getImage3() {
		return  image3;
	}

	public void setImage3(String image3) {
		this.image3 =image3;
	}

	/**
	 * 
	 * @return
	 */
	public String getPersoncreated() {
		return personcreated;
	}

	public void setPersoncreated(String personcreated) {
		this.personcreated = personcreated;
	}

	public String getPersonname() {
		return personname;
	}

	public void setPersonname(String personname) {
		this.personname = personname;
	}

	public String getPersonportrait() {
		return personportrait;
	}

	public void setPersonportrait(String personportrait) {
		this.personportrait = personportrait;
	}

	public String getPersonlocation() {
		return personlocation;
	}

	public void setPersonlocation(String personlocation) {
		this.personlocation = personlocation;
	}
	
	@Override
	public String toString() {
		StringBuilder builder=new StringBuilder();
		builder.append("por:").append(getPersonportrait()).append("\n");
		builder.append("name").append(getPersonname()).append("\n");
		return builder.toString();
	}
}

package idv.jiaming.models;

public class CommentObject {

	public static final String POI_TITLE_FIELD = "poi_title";
	public static final String COMMENT_USER_NAME_FIELD = "comment_user_name";
	public static final String COMMENT_URL_FIELD = "comment_url";
	public static final String COMMENT_TITLE_FIELD = "comment_title";
	public static final String COMMENT_DATE_FIELD = "comment_date";
	public static final String COMMENT_CONTENT_FIELD = "comment_content";
	public static final String COMMENT_RATING_FIELD = "comment_rating";
		
	private String poi_title;
	private String comment_user_name;
	private String comment_url;
	private String comment_title;
	private String comment_date;
	private String comment_content;
	private String comment_rating;
	
	public CommentObject(String poi_title, String comment_user_name, String comment_url, String comment_title, String comment_date, String comment_content, String comment_rating){
		this.setPoi_title(poi_title);
		this.setComment_user_name(comment_user_name);
		this.setComment_url(comment_url);
		this.setComment_title(comment_title);
		this.setComment_date(comment_date);
		this.setComment_content(comment_content);
		this.setComment_rating(comment_rating);
	}

	public String getPoi_title() {
		return poi_title;
	}

	public void setPoi_title(String poi_title) {
		this.poi_title = poi_title;
	}

	public String getComment_user_name() {
		return comment_user_name;
	}

	public void setComment_user_name(String comment_user_name) {
		this.comment_user_name = comment_user_name;
	}

	public String getComment_url() {
		return comment_url;
	}

	public void setComment_url(String comment_url) {
		this.comment_url = comment_url;
	}

	public String getComment_title() {
		return comment_title;
	}

	public void setComment_title(String comment_title) {
		this.comment_title = comment_title;
	}

	public String getComment_date() {
		return comment_date;
	}

	public void setComment_date(String comment_date) {
		this.comment_date = comment_date;
	}

	public String getComment_content() {
		return comment_content;
	}

	public void setComment_content(String comment_content) {
		this.comment_content = comment_content;
	}

	public String getComment_rating() {
		return comment_rating;
	}

	public void setComment_rating(String comment_rating) {
		this.comment_rating = comment_rating;
	}
		
}

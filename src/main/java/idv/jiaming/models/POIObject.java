package idv.jiaming.models;

public class POIObject {

	public static final String POI_VISTOR_RATING_FIELD = "poi_vistor_rating";
	public static final String POI_RATING_SUMMARY_FIELD = "poi_rating_summary";
	public static final String POI_TITLE_FIELD = "poi_title";
	public static final String POI_TELEPHONE_FIELD = "poi_telephone";
	public static final String POI_TYPE_FIELD = "poi_type";
	public static final String POI_URL_FIELD = "poi_url";
	public static final String POI_ADDRESS_FIELD = "poi_address";
	public static final String POI_SOURCE_FIELD = "poi_source";
	
	private String poi_vistor_rating;
	private String poi_rating_summary;
	private String poi_title;
	private String poi_telephone;
	private String poi_type;
	private String poi_url;
	private String poi_address;
	private String poi_source;
	
	public POIObject(String poi_vistor_rating, String poi_rating_summary, String poi_title, String poi_telephone, String poi_type, String poi_url, String poi_address){
		this.poi_vistor_rating = poi_vistor_rating;
		this.poi_rating_summary = poi_rating_summary;
		this.poi_title = poi_title;
		this.poi_telephone = poi_telephone;
		this.poi_type = poi_type;
		this.poi_url = poi_url;
		this.poi_address = poi_address;
	}
	
	public String getPoi_vistor_rating() {
		return poi_vistor_rating;
	}
	public void setPoi_vistor_rating(String poi_vistor_rating) {
		this.poi_vistor_rating = poi_vistor_rating;
	}
	public String getPoi_rating_summary() {
		return poi_rating_summary;
	}
	public void setPoi_rating_summary(String poi_rating_summary) {
		this.poi_rating_summary = poi_rating_summary;
	}
	public String getPoi_title() {
		return poi_title;
	}
	public void setPoi_title(String poi_title) {
		this.poi_title = poi_title;
	}
	public String getPoi_telephone() {
		return poi_telephone;
	}
	public void setPoi_telephone(String poi_telephone) {
		this.poi_telephone = poi_telephone;
	}
	public String getPoi_type() {
		return poi_type;
	}
	public void setPoi_type(String poi_type) {
		this.poi_type = poi_type;
	}
	public String getPoi_url() {
		return poi_url;
	}
	public void setPoi_url(String poi_url) {
		this.poi_url = poi_url;
	}
	public String getPoi_address() {
		return poi_address;
	}
	public void setPoi_address(String poi_address) {
		this.poi_address = poi_address;
	}

	public String getPoi_source() {
		return poi_source;
	}

	public void setPoi_source(String poi_source) {
		this.poi_source = poi_source;
	}

}

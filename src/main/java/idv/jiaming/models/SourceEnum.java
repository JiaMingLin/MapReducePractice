package idv.jiaming.models;

public enum SourceEnum {

	TRIP_ADVISOR("trip_advisor");
	
	private String value;
	private SourceEnum(String value){
		this.value = value;
	}
	public String getValue(){
		return this.value;
	}
}
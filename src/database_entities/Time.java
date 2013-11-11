package database_entities;

public class Time {
	int timeID;
	String description;
	public Time(int timeID, String description) {
		this.timeID = timeID;
		this.description = description;
	}
	public int getTimeID() {
		return timeID;
	}
	public void setTimeID(int timeID) {
		this.timeID = timeID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}

package com.swiftshot.funimals.models.database.entities;

public class Setting {
	int settingID;
	String description;
	
	public Setting(int settingID, String description) {
		this.settingID = settingID;
		this.description = description;
	}
	public int getSettingID() {
		return settingID;
	}
	public void setSettingID(int settingID) {
		this.settingID = settingID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
	
}

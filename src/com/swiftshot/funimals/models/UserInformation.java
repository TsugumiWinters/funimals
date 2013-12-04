package com.swiftshot.funimals.models;

public class UserInformation {
	
	private int image;
	private String name;
	private int age;
	private String level;
	
	public UserInformation(){
		image = 0;
		name = null;
		age = 6;
		level = "Prep";
	}
	
	public UserInformation(int image, String name, int age, String level) {
		this.image = image;
		this.name = name;
		this.age = age;
		this.level = level;
	}
	
	public int getImage() {
		return image;
	}
	
	public void setImage(int image) {
		this.image = image;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public int getAge() {
		return age;
	}
	
	public void setAge(int age) {
		this.age = age;
	}
	
	public String getLevel() {
		return level;
	}
	
	public void setLevel(String level) {
		this.level = level;
	}
	
}

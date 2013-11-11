package database_entities;

public class UserInformation {
	String Username;
	int Age;
	int Grade;
	
	public UserInformation(){}
	
	public UserInformation(String username, int age, int grade) {
		Username = username;
		Age = age;
		Grade = grade;
	}
	
	public String getUsername() {
		return Username;
	}
	public void setUsername(String username) {
		Username = username;
	}
	public int getAge() {
		return Age;
	}
	public void setAge(int age) {
		Age = age;
	}
	public int getGrade() {
		return Grade;
	}
	public void setGrade(int grade) {
		Grade = grade;
	}
}

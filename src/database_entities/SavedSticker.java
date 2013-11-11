package database_entities;

public class SavedSticker {
	private int StoryID;
	private String Type;
	private String Name;
	private float x;
	private float y;
	
	public SavedSticker(){}
	
	public SavedSticker(int StoryID, String Type, String Name, float x, float y)
	{
		this.StoryID = StoryID;
		this.Type = Type;
		this.Name = Name;
		this.x = x;
		this.y = y;
	}
	
	public int getStoryID() {
		return StoryID;
	}
	public void setStoryID(int storyID) {
		StoryID = storyID;
	}
	public String getType() {
		return Type;
	}
	public void setType(String type) {
		Type = type;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	
	
	
	
}

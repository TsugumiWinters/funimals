package database_entities;

public class StoryFile {
	int StoryID;
	String Username;
	String Title;
	String Story;
	private String Background;
	
	public StoryFile(){}

	public StoryFile(int storyID, String username, String title,
			String story, String background) {
		StoryID = storyID;
		Username = username;
		Title = title;
		Story = story;
		Background = background;
	}

	public int getStoryID() {
		return StoryID;
	}

	public void setStoryID(int storyID) {
		StoryID = storyID;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getStory() {
		return Story;
	}

	public void setStory(String story) {
		Story = story;
	}

	public String getBackground() {
		return Background;
	}

	public void setBackground(String background) {
		Background = background;
	}
	
	public String toString(){
		return Title;
	}
	
}

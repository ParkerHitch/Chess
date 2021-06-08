package game.util;

public enum Team {
	WHITE(ConsoleColors.YELLOW_BOLD),
	BLACK(ConsoleColors.BLUE_BOLD);
	
	String color;
	
	private Team(String color) {
		this.color = color;
	}
	
	public String getColor() {
		return this.color;
	}
	
}

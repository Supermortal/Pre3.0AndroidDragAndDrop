package com.namespacemedia.CS3270A6;

public class Player {
	
	private String playerName;
	private String playerPosition;

	public String getPlayerName() {
		return playerName;
	}
	
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
	}
	
	public String getPlayerPosition() {
		return playerPosition;
	}
	
	public void setPlayerPosition(String playerPosition) {
		this.playerPosition = playerPosition;
	}
	
	public String getFormattedPlayerName() {
		return playerName + "(" + playerPosition.toUpperCase() + ")";
	}

}

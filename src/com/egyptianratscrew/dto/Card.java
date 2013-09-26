package com.egyptianratscrew.dto;

public class Card {

	public String cardType;
	public int cardValue;
	public String cardSuit;
	public int tillFaceValue;
	
	public Card(String ct, int cv, String cs) {
		cardType = ct;
		cardValue = cv;
		cardSuit = cs;
		
		if (cardType.equalsIgnoreCase("jack")) {
			tillFaceValue = 1;
		}
		else if (cardType.equalsIgnoreCase("queen")) {
			tillFaceValue = 2;
		}
		else if (cardType.equalsIgnoreCase("king")) {
			tillFaceValue = 3;
		}
		else if (cardType.equalsIgnoreCase("ace")) {
			tillFaceValue = 4;
		}
		else {
			tillFaceValue = 0;
		}
	}
	
}

package com.egyptianratscrew.dto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Card Class
 * 
 * The Card class defines an individual card from the Card Deck
 * Each Card contains:
 * - A suit (Clubs, Spades, Diamonds, or Hearts)
 * - A type, which is either the number of the card, or the Jack, Queen, King, or Ace
 * - A value to determines how many points that card is worth
 * - An image name which relates to the .png file name for that card
 * - The resource id for the card png from drawable
 * - A hidden value to determine whether that card should be face down or up
 * - A Bitmap, which will be the card image
 * 
 * Hiding cards will be done here, as well as setting the card bitmap image
 * A method to draw a card to a Canvas is also set here
 * 
 * @author Edward McNealy
 * 
 */

public class Card {

	public String cardType;
	public int cardValue;
	public String cardSuit;
	public int tillFaceValue;
	public String imageName;
	public int resourceId;
	public boolean hidden = false;
	private Bitmap cardBitmap;

	private Context context;

	public Card() {

	}

	public Card(String type, int value, String suit, String image, Context con) {
		this.cardType = type;
		this.cardValue = value;
		this.cardSuit = suit;
		this.imageName = image;
		this.context = con;

		if (cardType.equalsIgnoreCase("jack")) {
			tillFaceValue = 1;
		} else if (cardType.equalsIgnoreCase("queen")) {
			tillFaceValue = 2;
		} else if (cardType.equalsIgnoreCase("king")) {
			tillFaceValue = 3;
		} else if (cardType.equalsIgnoreCase("ace")) {
			tillFaceValue = 4;
		} else {
			tillFaceValue = 0;
		}

		resourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
		cardBitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
	}

	public String getSuit() {
		return cardSuit;
	}

	public void setSuit(String suit) {
		this.cardSuit = suit;
	}

	public String getType() {
		return cardType;
	}

	public void setType(String type) {
		this.cardType = type;
	}

	public int getValue() {
		return cardValue;
	}

	public void setValue(int value) {
		this.cardValue = value;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public boolean isHidden() {
		return hidden;
	}

	public Bitmap getCardBitmap() {
		return cardBitmap;
	}

	public void setCardBitmap() {
		int cardId = 0;
		if (hidden) {
			cardId = context.getResources().getIdentifier("b2fv", "drawable", context.getPackageName());
		} else {
			cardId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
		}
		this.cardBitmap = BitmapFactory.decodeResource(context.getResources(), cardId);
	}

	public void drawCard(Canvas canvas) {
		canvas.drawBitmap(cardBitmap, 10, 10, null);
	}

	public String cardDescription() {
		String cardDescription = String.format("%s of %s", cardType, cardSuit);
		return cardDescription;
	}
}

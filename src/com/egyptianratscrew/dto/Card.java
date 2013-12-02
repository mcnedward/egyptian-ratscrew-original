package com.egyptianratscrew.dto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

/**
 * Card Class
 * <p>
 * The Card class defines an individual card from the Card Deck.
 * <p>
 * Each Card contains:<br>
 * - A suit (Clubs, Spades, Diamonds, or Hearts).<br>
 * - A type, which is either the number of the card, or the Jack, Queen, King, or Ace.<br>
 * - A value to determines how many points that card is worth.<br>
 * - An image name which relates to the .png file name for that card.<br>
 * - The resource id for the card png from drawable.<br>
 * - A hidden value to determine whether that card should be face down or up.<br>
 * - A Bitmap, which will be the card image.
 * <p>
 * Hiding cards will be done here, as well as setting the card bitmap image. A method to draw a card to a Canvas is also
 * set here.
 * 
 * @author Edward McNealy
 * 
 */

public class Card {

	public String cardType;
	public int cardValue;
	private String cardSuit;
	public int tillFaceValue;
	private String imageName;
	private int resourceId;
	private boolean hidden = false, rotate = true;
	private Bitmap bitmap;
	private float x, y, width, height;

	private Context context;

	/**
	 * Empty constructor for a card.
	 */
	public Card(Context context) {
		resourceId = context.getResources().getIdentifier("jokerred", "drawable", context.getPackageName());
		bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
		this.height = bitmap.getHeight();
		this.width = bitmap.getWidth();
	}

	/**
	 * Create a new card.
	 * 
	 * @param type
	 *            The type of the card. This will be either the card number, or the face card type, or an ace.
	 * @param value
	 *            The value of the card. This will be the card number, or 10 for face cards and ace cards.
	 * @param suit
	 *            The suit of the card. This will be either Club, Spade, Diamond, or Heart.
	 * @param image
	 *            The image name for the card. This will relate to the file name of the card image that is stored in the
	 *            res folder.
	 * @param context
	 *            The context of the activity that is creating the card.
	 */
	public Card(String type, int value, String suit, String image, Context context) {
		this.cardType = type;
		this.cardValue = value;
		this.cardSuit = suit;
		this.imageName = image;
		this.context = context;

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
		bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
		this.height = bitmap.getHeight();
		this.width = bitmap.getWidth();
	}

	/**
	 * Returns the suit of the card. This will be either Club, Spade, Diamond, or Heart.
	 * 
	 * @return The suit of the card.
	 */
	public String getSuit() {
		return cardSuit;
	}

	/**
	 * Used to set the suit of the card. This will be either Club, Spade, Diamond, or Heart.
	 * 
	 * @param suit
	 *            The suit of the card.
	 */
	public void setSuit(String suit) {
		this.cardSuit = suit;
	}

	/**
	 * Returns the type of the card. This will be either the card number, or the face card type, or an ace.
	 * 
	 * @return The type of the card.
	 */
	public String getType() {
		return cardType;
	}

	/**
	 * Used to set the type of the card. This will be either the card number, or the face card type, or an ace.
	 * 
	 * @param type
	 *            The type of the card.
	 */
	public void setType(String type) {
		this.cardType = type;
	}

	/**
	 * Returns the value of the card. This will be the card number, or 10 for face cards and ace cards.
	 * 
	 * @return The face value of the card.
	 */
	public int getValue() {
		return cardValue;
	}

	/**
	 * Used to set the value of the card. This will be the card number, or 10 for face cards and ace cards.
	 * 
	 * @param value
	 *            The face value of the card.
	 */
	public void setValue(int value) {
		this.cardValue = value;
	}

	/**
	 * Returns the image name for the card. This will relate to the file name of the card image that is stored in the
	 * res folder.
	 * 
	 * @return The file name for the card image.
	 */
	public String getImageName() {
		return imageName;
	}

	/**
	 * Sets the image name for the card. This will relate to the file name of the card image that is stored in the res
	 * folder.
	 * 
	 * @param imageName
	 *            The file name for the card image.
	 */
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	/**
	 * Returns the x-coordinate for the card image.
	 * 
	 * @return The x-coordinate for the card image.
	 */
	public float getX() {
		return x;
	}

	/**
	 * Set the x-coordinate for card image.
	 * 
	 * @param x
	 *            The x-coordinate for the card image.
	 */
	public void setX(float x) {
		this.x = x;
	}

	/**
	 * Returns the y-coordinate for the card image.
	 * 
	 * @return The y-coordinate for the card image.
	 */
	public float getY() {
		return y;
	}

	/**
	 * Set the y-coordinate for card image.
	 * 
	 * @param y
	 *            The y-coordinate for the card image.
	 */
	public void setY(float y) {
		this.y = y;
	}

	/**
	 * Returns the width of the card image.
	 * 
	 * @return The width of the card image.
	 */
	public float getWidth() {
		return width;
	}

	/**
	 * Returns the height of the card image.
	 * 
	 * @return The height of the card image.
	 */
	public float getHeight() {
		return height;
	}

	/**
	 * Sets the card to be hidden. If the card is hidden, it will be removed from a player's hand or from the middle
	 * stack.
	 * 
	 * @param hidden
	 *            A boolean that that tells if the card should be hidden.
	 */
	public void setHiddden(boolean hidden) {
		this.hidden = hidden;
	}

	/**
	 * Used to determine whether the card should be displayed or not. Used after a card is played to tell if that card
	 * should be removed from a player's hand, or from the middle stack.
	 * 
	 * @return A boolean that tells if the card should be hidden.
	 */
	public boolean isHidden() {
		return hidden;
	}

	/**
	 * Returns the card bitmap image.
	 * 
	 * @return A bitmap for the card image.
	 */
	public Bitmap getCardBitmap() {
		return bitmap;
	}

	/**
	 * Used to set the card bitmap image.
	 * 
	 * @param bitmap
	 *            The bitmap used to display the card image.
	 */
	public void setCardBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	/**
	 * This is used to reset the card bitmap to the default position
	 */
	public void resetCardBitmap() {
		resourceId = context.getResources().getIdentifier(imageName, "drawable", context.getPackageName());
		bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId);
		height = bitmap.getHeight();
		width = bitmap.getWidth();
	}

	/**
	 * Used to determine whether the card should be rotated or not.
	 * 
	 * @return Boolean - rotate card if true, ignore rotation if false.
	 */
	public boolean rotateCard() {
		return rotate;
	}

	/**
	 * This will set the rotation of the card to true or false.
	 * 
	 * @param rotate
	 *            Boolean - rotate card if true, ignore rotation if false.
	 */
	public void setRotate(boolean rotate) {
		this.rotate = rotate;
	}

	/**
	 * Returns a description of the card telling you the type and suit. Example - Ace of Spades.
	 * 
	 * @return A description of the current card.
	 */
	public String cardDescription() {
		String cardDescription = String.format("%s of %s", cardType, cardSuit);
		return cardDescription;
	}

	/**
	 * This is used to draw the card on a canvas. Call this when ready to loop through each card in a player's hand and
	 * the cards in the middle deck.
	 * 
	 * @param canvas
	 *            The canvas area to draw the cards on.
	 */
	public void onDraw(Canvas canvas) {
		float x = getX();
		float y = getY();
		canvas.drawBitmap(getCardBitmap(), x, y, null);
	}

	/**
	 * Method used to rotate the card. This should be used to rotate every other card in the middle stack
	 * 
	 * @param degree
	 *            The degree amount to rotate the card.
	 */
	public void rotateCard(float degree) {
		if (rotate) { // If the card is set to be rotated, rotate it!!!
			float scaleWidth = ((float) 100) / bitmap.getWidth();
			float scaleHeight = ((float) 150) / bitmap.getHeight();
			Matrix matrix = new Matrix();
			matrix.postScale(scaleWidth, scaleHeight);
			matrix.postRotate(degree);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			width = bitmap.getWidth();		// Reset the bitmap width
			height = bitmap.getHeight();	// Reset the bitmap height
		}
	}
}

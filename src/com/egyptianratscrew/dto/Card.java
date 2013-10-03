package com.egyptianratscrew.dto;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

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
	
	public Card(String type, int value, String suit, String image, Context con) {
		cardType = type;
		cardValue = value;
		cardSuit = suit;
		imageName = image;
		context = con;
		
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

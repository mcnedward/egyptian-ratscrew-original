package com.egyptianratscrew.dto;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class CardDeck {

	public List<Card> cardDeck;
	protected Context context;

	public static int DeckSize() {
		return deck.length;
	}

	// TODO This is the real deck that should be used in the final app
	private static String[] deck = new String[] { "sa", "s2", "s3", "s4", "s7", "s6", "s7", "s8", "s9", "s10", "sj",
			"sq", "sk", "ca", "c2", "c3", "c4", "c5", "c6", "c7", "c8", "c9", "c10", "cj", "cq", "ck", "da", "d2",
			"d3", "d4", "d5", "d6", "d7", "d8", "d9", "d10", "dj", "dq", "dk", "ha", "h2", "h3", "h4", "h5", "h6",
			"h7", "h8", "h9", "h10", "hj", "hq", "hk" };

	private static String[] testdeck = new String[] { "c6", "c6", "c6", "c6", "ca", "ca" };

	public CardDeck() {

	}

	public CardDeck(Context context) {
		// Loop through all cards in the deck and assign values
		cardDeck = new ArrayList<Card>();
		for (int x = 0; x < deck.length; x++) {
			String suit = deck[x].substring(0, 1);
			String type = deck[x].substring(1);
			int value = 0;
			String imageName = deck[x];

			// Set the suit of the card
			if (suit.equals("s"))
				suit = "Spades";
			else if (suit.equals("c"))
				suit = "Clubs";
			else if (suit.equals("d"))
				suit = "Diamonds";
			else if (suit.equals("h"))
				suit = "Hearts";

			// Check the type of the card and set accordingly
			if (type.equals("j")) {
				type = "Jack";
				value = 10;
			} else if (type.equals("q")) {
				type = "Queen";
				value = 10;
			} else if (type.equals("k")) {
				type = "King";
				value = 10;
			} else if (type.equals("a")) {
				type = "Ace";
				value = 11;
			} else
				value = Integer.parseInt(type);

			Card card = new Card(type, value, suit, imageName, context);
			// adding a card to the deck
			cardDeck.add(card);
		}
	}

	public CardDeck(Context context, boolean onePlayergame) {
		// Loop through all cards in the deck and assign values
		cardDeck = new ArrayList<Card>();
		for (int x = 0; x < testdeck.length; x++) {
			String suit = testdeck[x].substring(0, 1);
			String type = testdeck[x].substring(1);
			int value = 0;
			String imageName = testdeck[x];

			// Set the suit of the card
			if (suit.equals("s"))
				suit = "Spades";
			else if (suit.equals("c"))
				suit = "Clubs";
			else if (suit.equals("d"))
				suit = "Diamonds";
			else if (suit.equals("h"))
				suit = "Hearts";

			// Check the type of the card and set accordingly
			if (type.equals("j")) {
				type = "Jack";
				value = 10;
			} else if (type.equals("q")) {
				type = "Queen";
				value = 10;
			} else if (type.equals("k")) {
				type = "King";
				value = 10;
			} else if (type.equals("a")) {
				type = "Ace";
				value = 11;
			} else
				value = Integer.parseInt(type);

			Card card = new Card(type, value, suit, imageName, context);
			// adding a card to the deck
			cardDeck.add(card);
		}
	}

	/**
	 * 
	 * @param position
	 *            of the card
	 * @return
	 */
	public Card getCardAt(int position) {
		return cardDeck.get(position);
	}

	/**
	 * 
	 * @param position
	 *            of the card type
	 * @return
	 */
	public String getCardType(int position) {
		return cardDeck.get(position).cardType;
	}

	/**
	 * 
	 * @param position
	 *            of the card value
	 * @return
	 */
	public int getCardValue(int position) {
		return cardDeck.get(position).cardValue;
	}

}

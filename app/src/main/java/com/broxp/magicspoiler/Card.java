package com.broxp.magicspoiler;

/**
 * Container for a card used when crawling.
 * 
 * @author broxp
 */
public class Card {
	public String pageUrl;
	public String imageUrl;

	public Card(String pageUrl, String imageUrl) {
		this.pageUrl = pageUrl;
		this.imageUrl = imageUrl;
	}
}
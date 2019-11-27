package com.broxp.magicspoiler;

import java.util.ArrayList;
import java.util.List;

/**
 * Container for the result of an image list.
 * 
 * @author broxp
 */
public class Result {
	public boolean error;
	public Exception exception;
	public List<Card> imgs;
	public String html;
	public String title;

	public Result(Exception ex) {
		html = "";
		imgs = new ArrayList<Card>();
		title = "";
		error = true;
		exception = ex;
	}

	public Result(String html, String title, List<Card> imgs) {
		this.html = html;
		this.imgs = imgs;
		this.title = title;
		error = false;
		exception = null;
	}
}
package broxp.magicspoiler;

/**
 * Container for the result of an image list.
 * 
 * @author broxp
 */
public class Result {
	public boolean error;
	public Exception exception;
	public String[] imgs;
	public String html;
	public String title;

	public Result(Exception ex) {
		html = "";
		imgs = new String[0];
		title = "";
		error = true;
		exception = ex;
	}

	public Result(String html, String title, String[] imgs) {
		this.html = html;
		this.imgs = imgs;
		this.title = title;
		error = false;
		exception = null;
	}
}
package broxp.magicspoiler;

class Result {
	public boolean error;
	public Exception exception;
	public String[] imgs;
	public String html;

	public int index;
	public String title;

	public Result(Exception ex) {
		html = "";
		imgs = new String[0];
		title = "";
		error = true;
		exception = ex;
		index = 0;
	}

	public Result(String html, String title, String[] imgs) {
		this.html = html;
		this.imgs = imgs;
		this.title = title;
		error = false;
		exception = null;
		index = 0;
	}
}
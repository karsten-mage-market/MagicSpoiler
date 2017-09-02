package broxp.magicspoiler;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;

/**
 * Downloads the image list.
 * 
 * @author broxp
 */
public class RetrieveMagicTask extends AsyncTask<Void, Void, Result> {
	static final String baseUrl = "http://mythicspoiler.com/";

	@Override
	protected Result doInBackground(Void... params) {
		try {
			String html = StringDownloadTask.downloadWebsite(baseUrl);

			String searchPage = "<a class=\"card\" href=\"";
			String searchImage = "<img width=\"200\" align=\"left\" hspace=\"0\" src=\"";

			int start = -1;
			int end = 0;
			List<Card> cards = new ArrayList<Card>();
			while ((start = html.indexOf(searchPage, start + 1)) >= 0) {
				start += searchPage.length();
				end = html.indexOf("\"", start);
				Card card = new Card("", "");
				if (end >= 0) {
					String pageHref = html.substring(start, end);
					start = end;
					card.pageUrl = baseUrl + pageHref;
				}
				start = html.indexOf(searchImage, start);
				if (start >= 0) {
					start += searchImage.length();
					end = html.indexOf("\"", start);
					if (end >= 0) {
						String imgSrc = html.substring(start, end);
						start = end;
						card.imageUrl = baseUrl + imgSrc;
					}
				}
				cards.add(card);
			}

			String searchTitle = "/index.html\"> ";
			start = html.indexOf(searchTitle);
			String title = "?";
			if (start >= 0) {
				start += searchTitle.length();
				end = html.indexOf("<", start);
				title = html.substring(start, end).trim();
			}
			return new Result(html, title, cards);

		} catch (Exception ex) {
			return new Result(ex);
		}
	}
}
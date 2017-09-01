package broxp.magicspoiler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
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
			String html = downloadWebsite(baseUrl);

			String search = "img width=\"200\" align=\"left\" hspace=\"0\" src=\"";
			int start = -1;
			int end = 0;
			List<String> list = new ArrayList<String>();
			while ((start = html.indexOf(search, start + 1)) >= 0) {
				int startIndex = start + search.length();
				end = html.indexOf("\"", startIndex);
				if (end >= 0) {
					String imgSrc = html.substring(startIndex, end);
					String imageUrl = baseUrl + imgSrc;
					start = end;
					list.add(imageUrl);
				}
			}

			String[] bmp = list.toArray(new String[0]);

			search = "/index.html\"> ";
			start = html.indexOf(search);
			String title = "?";
			if (start >= 0) {
				start += search.length();
				end = html.indexOf("<", start);
				title = html.substring(start, end).trim();
			}
			return new Result(html, title, bmp);

		} catch (Exception ex) {
			return new Result(ex);
		}
	}

	String downloadWebsite(String url) throws Exception {
		StringBuilder sb = new StringBuilder();
		InputStream stream = new URL(url).openStream();
		InputStreamReader reader = new InputStreamReader(stream);
		BufferedReader buf = null;
		try {
			buf = new BufferedReader(reader);
			String line;
			while ((line = buf.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} finally {
			if (buf != null) {
				buf.close();
			}
		}
		String html = sb.toString();
		return html;
	}
}
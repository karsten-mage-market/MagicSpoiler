package broxp.magicspoiler;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import android.os.AsyncTask;

class RetrieveMagicTask extends AsyncTask<Void, Void, Result> {
	String baseUrl = "http://mythicspoiler.com/";
	Exception ex;

	@Override
	protected Result doInBackground(Void... params) {
		try {
			StringBuilder sb = new StringBuilder();
			InputStreamReader reader = null;
			BufferedReader buf = null;
			try {
				reader = new InputStreamReader(new URL(baseUrl).openStream());
				try {
					buf = new BufferedReader(reader);
					String line;
					while ((line = buf.readLine()) != null) {
						sb.append(line).append("\n");
					}
				} finally {
					buf.close();
				}
			} finally {
				reader.close();
			}
			String html = sb.toString();

			String search = "img width=\"200\" align=\"left\" hspace=\"0\" src=\"";
			int start = -1;
			int end = 0;
			ArrayList<String> list = new ArrayList<String>();
			while ((start = html.indexOf(search, start + 1)) >= 0) {
				int startIndex = start + search.length();
				end = html.indexOf("\"", startIndex);
				String imgSrc = html.substring(startIndex, end);
				String b = baseUrl + imgSrc;
				start = end;
				list.add(b);
			}

			String[] bmp = list.toArray(new String[list.size()]);

			search = "/index.html\"> ";
			start = html.indexOf(search) + search.length();
			end = html.indexOf("<", start);
			String title = html.substring(start, end).trim();
			return new Result(html, title, bmp);

		} catch (Exception ex) {
			return new Result(ex);
		}
	}
}
package broxp.magicspoiler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import android.os.AsyncTask;

/**
 * Downloads an url and returns an {@link Exception} or a {@link String}.
 * 
 * @author broxp
 */
public class StringDownloadTask extends AsyncTask<String, Void, Object> {
	@Override
	protected Object doInBackground(String... url) {
		try {
			String html = downloadWebsite(url[0]);
			return html;

		} catch (Exception ex) {
			return ex;
		}
	}

	public static String downloadWebsite(String url) throws Exception {
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
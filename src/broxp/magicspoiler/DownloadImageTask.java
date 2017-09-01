package broxp.magicspoiler;

import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 * Downloads an image. Returns an {@link Exception} or {@link Bitmap}.
 * 
 * @author broxp
 */
public class DownloadImageTask extends AsyncTask<String, Void, Object> {
	@Override
	protected Object doInBackground(String... params) {
		String url = params[0];
		try {
			InputStream stream = new URL(url).openStream();
			return BitmapFactory.decodeStream(stream);
		} catch (Exception e) {
			return e;
		}
	}
}
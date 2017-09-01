package broxp.magicspoiler;

import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

/**
 * Downloads an image.
 * 
 * @author broxp
 */
public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	@Override
	protected Bitmap doInBackground(String... params) {
		String url = params[0];
		try {
			InputStream stream = new URL(url).openStream();
			return BitmapFactory.decodeStream(stream);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
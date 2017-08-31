package broxp.magicspoiler;

import java.io.InputStream;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
	final static String url = "http://maps.uesp.net/mwmap/zoom17/vvardenfell-%d-%d-17.jpg";

	@Override
	protected Bitmap doInBackground(String... params) {
		String r = params[0];
		try {
			InputStream is = new URL(r).openStream();
			return BitmapFactory.decodeStream(is);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
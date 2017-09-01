package broxp.magicspoiler;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TouchDelegate;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * The main activity where user input and displaying images and text happens.
 * 
 * @author broxp
 */
public class FullscreenActivity extends Activity {
	ImageView tile;
	ViewGroup grid;
	int x, y;
	int lastX, lastY;
	Result result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_fullscreen);
		setTitle("Loading spoilers...");

		grid = (ViewGroup) findViewById(R.id.grid);
		Log.d("grid", "" + grid);
		tile = (ImageView) findViewById(R.id.player);
		Log.d("tile", "" + tile);

		AsyncTask<Void, Void, Result> execute = new RetrieveMagicTask()
				.execute((Void) null);

		Exception exception;
		try {
			result = execute.get();
			exception = result.exception;
		} catch (Exception e) {
			exception = e;
		}
		if (result.exception != null) {
			Log.e("exception", exception + "");
			setTitle(exception.getClass().getSimpleName() + ": "
					+ exception.getLocalizedMessage());
		}
		if (exception == null) {
			showImage();
			Rect rect = new Rect(0, 0, grid.getWidth(), grid.getHeight());
			grid.setTouchDelegate(new TouchDelegate(rect, grid) {
				@Override
				public boolean onTouchEvent(MotionEvent event) {
					touch(event);
					return super.onTouchEvent(event);
				}
			});
		}
	}

	void touch(MotionEvent event) {
		float w = grid.getWidth();
		float h = grid.getHeight();
		float xMouse = event.getX();
		float yMouse = event.getY();
		float dx = xMouse - w / 2;
		float dy = yMouse - h / 2;
		x += Math.abs(dx) < w * 0.1 ? 0 : Math.signum(dx);
		y += Math.abs(dy) < h * 0.1 ? 0 : Math.signum(dy);
		int max = result.imgs.length;
		if (x >= max) {
			x = 0;
		} else if (x < 0) {
			x = max - 1;
		}
		if (y >= max) {
			y = 0;
		} else if (y < 0) {
			y = max - 1;
		}

		if (x != lastX || y != lastY) {
			showImage();
			lastX = x;
			lastY = y;
		}
	}

	void showImage() {
		setTitle("Loading # " + x + "...");
		String current = result.imgs[x];
		new DownloadImageTask() {
			@Override
			protected void onPostExecute(Bitmap result) {
				tile.setImageBitmap(result);
				status();
			}
		}.execute(current);
		status();
	}

	void status() {
		setTitle((x + 1) + "/" + result.imgs.length + " " + result.title);
	}
}

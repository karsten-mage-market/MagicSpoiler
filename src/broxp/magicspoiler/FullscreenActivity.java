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
				.execute(new Void[0]);
		try {
			result = execute.get();
			if (result.exception != null) {
				Log.e("result.exception", result.exception + "");
			}
		} catch (Exception e) {
			Log.e("e", e + "");
		}
		map();
		grid.setTouchDelegate(new TouchDelegate(rect(), grid) {
			@Override
			public boolean onTouchEvent(MotionEvent event) {
				touch(event);
				return super.onTouchEvent(event);
			}
		});
	}

	Rect rect() {
		return new Rect(0, 0, tile.getWidth(), tile.getHeight());
	}

	void touch(MotionEvent event) {
		// long dur = event.getEventTime() - event.getDownTime();
		// Toast.makeText(getApplication(), "Time: " + dur, Toast.LENGTH_SHORT).show();
		Rect r = rect();
		float w = r.width();
		float h = r.height();
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

		int newX = (int) x;
		int newY = (int) y;
		if (newX != lastX || newY != lastY) {
			map();
			lastX = newX;
			lastY = newY;
		}
	}

	void map() {
		setTitle("Loading # " + x + "...");
		new DownloadImageTask() {
			@Override
			protected void onPostExecute(Bitmap result) {
				tile.setImageBitmap(result);
				status();
			}
		}.execute(result.imgs[x]);
		status();
	}

	void status() {
		setTitle((x + 1) + "/" + result.imgs.length + " " + result.title);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
	}
}

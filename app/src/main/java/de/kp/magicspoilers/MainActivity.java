package de.kp.magicspoilers;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView imgView;
    TextView textView;
    Button buttonNext;
    Button buttonPrev;

    String title;
    List<String> images;
    int imageIndex = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void imageOnClick(View view) {
        Toast.makeText(this, "Test " + view, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart() {
        try {
            super.onStart();

            imgView = (ImageView) this.findViewById(R.id.imageView);
            textView = (TextView) this.findViewById(R.id.textView);
            buttonPrev = (Button) this.findViewById(R.id.button);
            buttonNext = (Button) this.findViewById(R.id.button3);
            //if ("".isEmpty()) return;
            String res = (String) new AsyncTask<Object, Object, Object>() {
                @Override
                protected Object doInBackground(Object... params) {
                    try {
                        URLConnection connection = new URL("http://mythicspoiler.com/newspoilers.html").openConnection();
                        StringBuilder builder = new StringBuilder();
                        /*Scanner scanner = new Scanner(new BufferedInputStream(connection.getInputStream()));
                        while (scanner.hasNext()) {
                            builder.append(scanner.next()).append("\n");
                        }*/
                        builder.append("<html><title>a</title><img src=\"img/1.png\" />");
                        return builder.toString();
                    } catch (Exception e) {
                        Log.e("e", "" + e);
                        throw new RuntimeException(e);
                    }
                }
            }.execute().get();

            title = substringBetween(res, "<title>", "</title>");
            String body = substringBetween(res, "<body", "</body>");
            images = new ArrayList<>();
            while (body.indexOf("<img") >= 0) {
                String img = substringBetween(body, "<img ", "</img>");
                if (!img.isEmpty()) {
                    images.add(substringBetween(img, "src=\"", "\""));
                }
                body = body.substring(body.indexOf(img));
            }
            textView.setText("OK");
            imgView.setClickable(true);
            getWindow().setTitle(title);
            getSupportActionBar().setTitle(title);
            final Bitmap bmp = Bitmap.createBitmap(800, 800, Bitmap.Config.ARGB_4444);
            View.OnTouchListener listener = new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (event == null) {
                        return false;
                    }
                    float x = event.getX();
                    int action = event.getAction();
                    if (action == MotionEvent.ACTION_DOWN) {
                        int mid = v.getWidth() / 2;
                        Log.e("event", event + ", " + mid + ",x:" + x);
                        if (x < mid) {
                            imageIndex = (imageIndex + 1) % images.size();
                            showText("left " + imageIndex);

                        } else if (x > mid) {
                            imageIndex = (imageIndex - 1 + images.size()) % images.size();

                            showText("right " + imageIndex);
                        }

                        String imgUrl = images.get(imageIndex);
                        InputStream stream = null;
                        try {
                            stream = new URL(imgUrl).openConnection().getInputStream();
                        } catch (IOException e) {
                            showText(e.toString());
                        }
                        imgView.setImageBitmap(Bitmap.createBitmap(stream));
                        stream.close();
                    }
                    for (int dx = 0; dx < bmp.getWidth(); dx++) {
                        for (int dy = 0; dy < bmp.getHeight(); dy++) {
                            int rgb = dx + dy;
                            bmp.setPixel(dx, dy, rgb);
                        }
                    }
                    imgView.setImageBitmap(bmp);
                    imgView.refreshDrawableState();
                    return true;
                }
            };
            listener.onTouch(imgView, null);
            imgView.setOnTouchListener(listener);
            bmp.setPixel(0, 1, 0xFFFFFF);
            imgView.setImageBitmap(bmp);
        } catch (Exception e) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void showText(String text) {
        Toast.makeText(MainActivity.this, text, Toast.LENGTH_LONG).show();
    }

    String substringBetween(String res, String start, String end) {
        int startInd = res.indexOf(start);
        if (startInd == -1) {
            return "";
        }
        startInd += start.length();
        int endInd = res.indexOf(end, startInd);
        if (endInd == -1) {
            return "";
        }
        return res.substring(startInd, endInd);
    }
}

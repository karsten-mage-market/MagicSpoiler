package com.broxp.magicspoiler;

import java.util.ArrayList;
import java.util.List;

import android.os.AsyncTask;
import android.util.Log;

/**
 * Downloads the image list.
 *
 * @author broxp
 */
public class RetrieveMagicTask extends AsyncTask<Void, Void, Result> {
    static final String baseUrl =
            //"https://cors-anywhere.herokuapp.com/http://mythicspoilers.com";
            "https://mythicspoiler.com/";
    static final String startUrl =
            "https://mythicspoiler.com/newspoilers.html";

    @Override
    protected Result doInBackground(Void... params) {
        try {
            Log.d("info", "RetrieveMagicTask doInBack");

            String html = StringDownloadTask.downloadWebsite(startUrl);
            Log.d("info", "RetrieveMagicTask res " + html.length());

            String searchPage = "<a href=\"mtg/cards/";
            String searchImage = "src=\"";

            int start = -1;
            int end = 0;
            List<Card> cards = new ArrayList<Card>();
            while ((start = html.indexOf(searchPage, start + 1)) >= 0) {
                start += searchPage.length();
                end = html.indexOf("\"", start);
                Card card = new Card("", "");
                if (end >= 0) {
                    String pageHref = html.substring(start - "mtg/cards/".length(), end);
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
            Log.d("info", "Res: " + title + ", " + cards.size());

            return new Result(html, title, cards);

        } catch (Exception ex) {
            Log.d("info", "Ex: " + ex);

            return new Result(ex);
        }
    }
}
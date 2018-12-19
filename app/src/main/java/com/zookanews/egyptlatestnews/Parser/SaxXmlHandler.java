package com.zookanews.egyptlatestnews.Parser;

import android.text.Html;
import android.util.Log;

import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SaxXmlHandler extends DefaultHandler {
    private static final String TAG = SaxXmlHandler.class.getSimpleName();
    private StringBuilder tempValue = new StringBuilder();
    private Article article;
    private List<Article> articles;
    private String link, title, description, imgSrc, guid, enclosure, thumb_url, mediaThumbnail, articleLink, articleThumbnailUrl, imageUrl;

    SaxXmlHandler() {
        articles = new ArrayList<>();
    }

    List<Article> getArticles() {
        return articles;
    }

    public void startElement(String uri, String localName, String qName, Attributes attributes) {
        tempValue.setLength(0);
        if (qName.equalsIgnoreCase("img")) {
            imgSrc = attributes.getValue(0);
            Log.d(TAG, "imgSrc = " + imgSrc);
        } else if (qName.equalsIgnoreCase("enclosure")) {
            enclosure = attributes.getValue(0);
            Log.d(TAG, "enclosure = " + enclosure);
        } else if (qName.equalsIgnoreCase("media:thumbnail")) {
            mediaThumbnail = attributes.getValue("url");
            Log.d(TAG, "media:thumbnail = " + mediaThumbnail);
        }
    }

    public void endElement(String uri, String localName, String qName) {
        if (qName.equalsIgnoreCase("item")) {
            if (link != null) {
                articleLink = link;
            } else if (guid != null) {
                articleLink = guid;
            }
            if (thumb_url != null) {
                articleThumbnailUrl = thumb_url;
            } else if (imgSrc != null) {
                articleThumbnailUrl = imgSrc;
            } else if (enclosure != null) {
                articleThumbnailUrl = enclosure;
            } else if (mediaThumbnail != null) {
                articleThumbnailUrl = mediaThumbnail;
            } else if (imageUrl != null) {
                articleThumbnailUrl = imageUrl;
            }
            article = new Article(title, articleLink, description, Calendar.getInstance().getTime(), articleThumbnailUrl, null, null, false);
            articles.add(article);
        } else if (qName.equalsIgnoreCase("title")) {
            title = tempValue.toString();
            Log.d(TAG, "title = " + title);
        } else if (qName.equalsIgnoreCase("link")) {
            link = tempValue.toString();
            Log.d(TAG, "link = " + link);
        } else if (qName.equalsIgnoreCase("description")) {
            description = String.valueOf(Html.fromHtml(tempValue.toString()));
            Log.d(TAG, "description = " + description);
        } else if (qName.equalsIgnoreCase("guid")) {
            guid = tempValue.toString();
            Log.d(TAG, "guid = " + guid);
        } else if (qName.equalsIgnoreCase("thumb_url")) {
            thumb_url = tempValue.toString();
            Log.d(TAG, "thumb_url = " + thumb_url);
        } else if (qName.equalsIgnoreCase("url")) {
            imageUrl = tempValue.toString();
        }
    }

    public void characters(char[] ch, int start, int length) {
        tempValue.append(ch, start, length);
    }
}
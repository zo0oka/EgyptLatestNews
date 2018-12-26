package com.zookanews.egyptlatestnews.Parser;

import android.annotation.SuppressLint;
import android.text.Html;

import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;

import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class SaxXmlHandler extends DefaultHandler {
    private StringBuilder tempValue = new StringBuilder();
    private List<Article> articles;
    private String link, title, description, imgSrc, guid, enclosure, thumb_url, mediaThumbnail, articleLink, articleThumbnailUrl, imageUrl;
    private Date pubDate;

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
        } else if (qName.equalsIgnoreCase("enclosure")) {
            enclosure = attributes.getValue(0);
        } else if (qName.equalsIgnoreCase("media:thumbnail")) {
            mediaThumbnail = attributes.getValue("url");
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
            Date articlePubDate;
            if (pubDate != null) {
                articlePubDate = pubDate;
            } else {
                articlePubDate = Calendar.getInstance().getTime();
            }

            Article article = new Article(title, articleLink, description, articlePubDate, articleThumbnailUrl, null, null, false, false);
            articles.add(article);
        } else if (qName.equalsIgnoreCase("title")) {
            title = tempValue.toString();
        } else if (qName.equalsIgnoreCase("link")) {
            link = tempValue.toString();
        } else if (qName.equalsIgnoreCase("description")) {
            description = String.valueOf(Html.fromHtml(tempValue.toString()));
        } else if (qName.equalsIgnoreCase("guid")) {
            guid = tempValue.toString();
        } else if (qName.equalsIgnoreCase("thumb_url")) {
            thumb_url = tempValue.toString();
        } else if (qName.equalsIgnoreCase("url")) {
            imageUrl = tempValue.toString();
        } else if (qName.equalsIgnoreCase("pubDate")) {
            String dateString = tempValue.toString();
            try {
                pubDate = getDateFromString(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("SimpleDateFormat")
    private Date getDateFromString(String dateString) throws ParseException {
        Date pubDate;
        int lettersCount = dateString.length();
        if (lettersCount == 29) {
            pubDate = (new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z")).parse(dateString.replace("GMT", "EET"));
        } else if (lettersCount == 19) {
            pubDate = (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).parse(dateString);
        } else if (lettersCount == 31) {
            pubDate = (new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z")).parse(dateString);
        } else if (lettersCount == 26) {
            pubDate = (new SimpleDateFormat("EEE , dd-MM-yyyy HH:mm:ss")).parse(dateString);
        } else {
            pubDate = (new SimpleDateFormat("EEE, MMM dd, yyyy - HH:mm")).parse(dateString);
        }
        return pubDate;
    }

    public void characters(char[] ch, int start, int length) {
        tempValue.append(ch, start, length);
    }
}

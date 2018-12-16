package com.zookanews.egyptlatestnews;

import android.util.Xml;

import com.zookanews.egyptlatestnews.Entities.Article;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

class XmlFeedParser {

    private InputStream downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();
        return conn.getInputStream();
    }

    private List<Article> parse(InputStream inputStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            inputStream.close();
        }
    }

    private List<Article> readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Article> articles = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, null, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("item")) {
                articles.add(readArticle(parser));
            } else {
                skip(parser);
            }
        }
        return articles;
    }

    private Article readArticle(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, null, "item");
        String title = null;
        String description = null;
        String link = null;
        String articleLink = null;
        String image = null;
        String enclosure = null;
        String guid = null;
        String thumb_url = null;
        String thumbnailUrl = null;
        Date date = Calendar.getInstance().getTime();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equalsIgnoreCase("title")) {
                title = readTitle(parser);
            } else if (name.equalsIgnoreCase("description")) {
                description = readDescription(parser);
            } else if (name.equalsIgnoreCase("link")) {
                link = readLink(parser);
            } else if (name.equalsIgnoreCase("image")) {
                image = readImage(parser);
            } else if (name.equalsIgnoreCase("enclosure")) {
                enclosure = readEnclosure(parser);
            } else if (name.equalsIgnoreCase("guid")) {
                guid = readGuid(parser);
            } else if (name.equalsIgnoreCase("thumb_url")) {
                thumb_url = readReadThumbUrl(parser);
            } else {
                skip(parser);
            }
        }
        if (image != null) {
            thumbnailUrl = image;
        } else if (enclosure != null) {
            thumbnailUrl = enclosure;
        } else if (thumb_url != null) {
            thumbnailUrl = thumb_url;
        }

        if (link != null) {
            articleLink = link;
        } else if (guid != null) {
            articleLink = guid;
        }
        return new Article(title, articleLink, description, date, thumbnailUrl, null, null, false);
    }

    private String readReadThumbUrl(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "thumb_url");
        String thumb_url = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "thumb_url");
        return thumb_url;
    }

    private String readGuid(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "guid");
        String guid = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "guid");
        return guid;
    }

    private String readEnclosure(XmlPullParser parser) throws IOException, XmlPullParserException {
        String enclosure = "";
        parser.require(XmlPullParser.START_TAG, null, "enclosure");
        String tag = parser.getName();
        if (tag.equals("enclosure")) {
            enclosure = parser.getAttributeValue(null, "url");
        }
        parser.require(XmlPullParser.END_TAG, null, "enclosure");
        return enclosure;
    }

    private String readImage(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "image");
        String img = readImageSrc(parser);
        parser.require(XmlPullParser.END_TAG, null, "image");
        return img;
    }

    private String readImageSrc(XmlPullParser parser) throws IOException, XmlPullParserException {
        String src = "";
        parser.require(XmlPullParser.START_TAG, null, "img");
        String tag = parser.getName();
        if (tag.equals("img")) {
            src = parser.getAttributeValue(null, "src");
        }
        parser.require(XmlPullParser.END_TAG, null, "img");
        return src;
    }

    private String readTitle(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "title");
        String title = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "title");
        return title;
    }

    private String readLink(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "link");
        String link = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "link");
        return link;
    }

    private String readDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null, "description");
        String description = readText(parser);
        parser.require(XmlPullParser.END_TAG, null, "description");
        return description;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    List<Article> loadXmlFromNetwork(String urlString) {
        InputStream inputStream = null;
        List<Article> articles = null;

        try {
            inputStream = downloadUrl(urlString);
            articles = parse(inputStream);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return articles;
    }
}

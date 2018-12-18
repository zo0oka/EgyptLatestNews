package com.zookanews.egyptlatestnews.Parser;

import com.zookanews.egyptlatestnews.RoomDB.Entities.Article;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

public class SaxXmlParser {

    public static List<Article> parse(String urlString) {

        List<Article> articles = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            InputStream inputStream = conn.getInputStream();
            // create a XMLReader from SAXParser
            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser()
                    .getXMLReader();
            // create a SAXXMLHandler
            SaxXmlHandler saxHandler = new SaxXmlHandler();
            // store handler in XMLReader
            xmlReader.setContentHandler(saxHandler);
            // the process starts
            xmlReader.parse(new InputSource(inputStream));
            // get the `Employee list`
            articles = saxHandler.getArticles();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return Employee list
        return articles;
    }
}

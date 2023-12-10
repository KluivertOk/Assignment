package com.example.assignment;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class CustomXmlParser {

    private static final String NAMESPACE = null;

    public static List<Rss> parse(InputStream inputStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readRssItems(parser);
        } finally {
            inputStream.close();
        }
    }

    private static List<Rss> readRssItems(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Rss> rssItems = new ArrayList<>();

        while (parser.next() != XmlPullParser.START_TAG) {
            // Loop until the start tag is found
        }

        if (parser.getName().equals("channel")) {
            parser.require(XmlPullParser.START_TAG, NAMESPACE, "channel");
        } else {
            throw new XmlPullParserException("Expected <channel> tag, found " + parser.getName());
        }

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String name = parser.getName();

            if (name.equals("item")) {
                rssItems.add(readRssItem(parser));
            } else {
                skipTag(parser);
            }
        }

        parser.require(XmlPullParser.START_TAG, NAMESPACE, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("item")) {
                rssItems.add(readRssItem(parser));
            } else {
                skipTag(parser);
            }
        }
        return rssItems;
    }

    private static List<Rss> readChannel(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<Rss> rssItems = new ArrayList<>();

        parser.require(XmlPullParser.START_TAG, NAMESPACE, "channel");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();

            if (name.equals("item")) {
                rssItems.add(readRssItem(parser));
            } else {
                skipTag(parser);
            }
        }
        return rssItems;
    }

    private static Rss readRssItem(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, NAMESPACE, "item");
        String title = null;
        String description = null;
        String link = null;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            switch (name) {
                case "title":
                    title = readText(parser);
                    break;
                case "description":
                    description = readText(parser);
                    break;
                case "link":
                    link = readText(parser);
                    break;
                default:
                    skipTag(parser);
                    break;
            }
        }
        return new Rss(title, description, link);
    }

    private static String readText(XmlPullParser parser) throws XmlPullParserException, IOException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private static void skipTag(XmlPullParser parser) throws XmlPullParserException, IOException {
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
}

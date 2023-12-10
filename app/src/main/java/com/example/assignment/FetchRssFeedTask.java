//package com.example.assignment;
//
//import android.os.AsyncTask;
//import android.widget.ListView;
//
//import org.xmlpull.v1.XmlPullParserException;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.util.List;
//
//public class FetchRssFeedTask extends AsyncTask<String, Void, List<Rss>> {
//
//    private ListView listView;
//
//    public FetchRssFeedTask(ListView listView) {
//        this.listView = listView;
//    }
//
//    @Override
//    protected List<Rss> doInBackground(String... params) {
//        String rssUrl = params[0];
//        try {
//            URL url = new URL(rssUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            InputStream inputStream = connection.getInputStream();
//
//            return Parser.parse(inputStream);
//        } catch (IOException | XmlPullParserException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    protected void onPostExecute(List<Rss> result) {
//        if (result != null) {
//            CustomAdapter adapter = (CustomAdapter) listView.getAdapter();
//            adapter.clear();
//            adapter.addAll(result);
//            adapter.notifyDataSetChanged();
//        }
//    }
//}

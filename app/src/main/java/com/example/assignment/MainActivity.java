package com.example.assignment;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText editTextRssUrl;
    private Button btnSubscribe;
    private Button btnLogout;
    private ListView listView;

    private WebView webView;
    DatabaseReference databaseReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextRssUrl = findViewById(R.id.editTextRssUrl);
        btnSubscribe = findViewById(R.id.btnSubscribe);
        btnLogout = findViewById(R.id.btnLogout);
        listView = findViewById(R.id.listView);
        webView = findViewById(R.id.webView);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("userHistory");

        List<Rss> rssItems = new ArrayList<>();
        CustomAdapter adapter = new CustomAdapter(this, R.layout.list_item, rssItems);
        listView.setAdapter(adapter);

        // Handle button click to subscribe and fetch RSS feed
        btnSubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rssUrl = editTextRssUrl.getText().toString();

                // Open WebView to display the subscribed RSS feed
                loadWebView(rssUrl);
            }
        });


        // Handle logout button click
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle logout logic
                signOut();

                // Redirect to the login activity
                Intent intent = new Intent(MainActivity.this, login.class);
                startActivity(intent);

                // Finish the current activity to prevent the user from coming back
                finish();
            }
        });

    }

    private void signOut() {
        // Sign out from Firebase
        FirebaseAuth.getInstance().signOut();

    }

    private void loadWebView(String url) {
        if (url != null && !url.isEmpty()) {
            // Show WebView and load the URL
            webView.setVisibility(View.VISIBLE);
            webView.loadUrl(url);
        }
    }

    public void showHistory(View view) {
        // Launch a new activity or show a dialog to display the history
        // You can customize this based on your UI design

        // For example, showing a Toast with the history for now
        showHistoryToast();
    }

    private void showHistoryToast() {
        // Retrieve the user's history from Firebase and display it in a Toast
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("userHistory").child(userId);

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Rss> historyList = new ArrayList<>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Rss rssItem = snapshot.getValue(Rss.class);
                    if (rssItem != null) {
                        historyList.add(rssItem);
                    }
                }

                // Display the history in a Toast
                showHistoryToastMessage(historyList);
            }

            private void showHistoryToastMessage(List<Rss> historyList) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle error
                Toast.makeText(MainActivity.this, "Failed to retrieve history", Toast.LENGTH_SHORT).show();
            }
        });


        class FetchRssFeedTask extends AsyncTask<String, Void, List<Rss>> {

            @Override
            protected List<Rss> doInBackground(String... params) {
                String rssUrl = params[0];
                // Implement the logic to fetch RSS feed in the background
                // For simplicity, this example uses a basic HTTP connection, consider using a library
                return fetchRssFeed(rssUrl);
            }

            @Override
            protected void onPostExecute(List<Rss> result) {
                // Update the UI with the fetched RSS feed
                if (result != null) {
                    CustomAdapter adapter = (CustomAdapter) listView.getAdapter();
                    adapter.clear();
                    adapter.addAll(result);
                    adapter.notifyDataSetChanged();
                }
            }

            private List<Rss> fetchRssFeed(String rssUrl) {
                // Implement the logic to fetch the RSS feed
                // This is a simplified example using HttpURLConnection, consider using a library (e.g., Retrofit)
                try {
                    URL url = new URL(rssUrl);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    InputStream inputStream = connection.getInputStream();

                    // Implement logic to parse the RSS feed
                    // This part depends on the structure of the RSS feed (e.g., XML parsing)
                    // ...

                    // For now, return dummy data
                    List<Rss> dummyData = new ArrayList<>();
                    dummyData.add(new Rss("Dummy Title", "Dummy Description", "Dummy Link"));
                    return dummyData;

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }
    }
}

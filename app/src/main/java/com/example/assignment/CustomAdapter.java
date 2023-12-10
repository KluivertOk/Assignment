package com.example.assignment;// CustomAdapter.java
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.assignment.R;
import com.example.assignment.Rss;
import com.example.assignment.RssActivity;

import java.util.List;

public class CustomAdapter extends ArrayAdapter<Rss> {

    private int layoutResource;
    private Context context;

    public CustomAdapter(Context context, int resource, List<Rss> objects) {
        super(context, resource, objects);
        this.context = context;
        layoutResource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;

        if (listItem == null) {
            listItem = LayoutInflater.from(getContext()).inflate(layoutResource, parent, false);
        }

        Rss currentRss = getItem(position);

        if (currentRss != null) {
            TextView titleTextView = listItem.findViewById(R.id.textViewTitle);
            TextView descriptionTextView = listItem.findViewById(R.id.textViewDescription);
            Button viewStoryButton = listItem.findViewById(R.id.btnViewStory);

            titleTextView.setText(currentRss.getTitle());
            descriptionTextView.setText(currentRss.getDescription());

            // Handle button click to view full story
            viewStoryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Launch RssActivity with the selected RSS link
                    String rssLink = currentRss.getLink();
                    Intent intent = new Intent(context, RssActivity.class);
                    intent.putExtra("rssLink", rssLink);
                    context.startActivity(intent);
                }
            });
        }

        return listItem;
    }

    private void openWebPage(String url) {
        // Open the web page in the device's browser
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }
}

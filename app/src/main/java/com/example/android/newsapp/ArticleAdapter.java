package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.newsapp.image.DownloadImageTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Nate on 10/20/2017.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    private long timeInMilliseconds;

    public ArticleAdapter(Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        Article currentArticle = getItem(position);

        TextView titleTextView = (TextView) convertView.findViewById(R.id.title_text_view);
        titleTextView.setText(currentArticle.getTitle());

        TextView subjectTextView = (TextView) convertView.findViewById(R.id.subject_text_view);
        subjectTextView.setText(currentArticle.getSubject());

        TextView authorTextView = (TextView) convertView.findViewById(R.id.author_text_view);
        authorTextView.setText(currentArticle.getAuthor());

        String date = (currentArticle.getDate());
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        try {
            Date dateObject = dateFormatter.parse(date);
            timeInMilliseconds = dateObject.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long currentTime = System.currentTimeMillis();
        long timeElapsed = currentTime - timeInMilliseconds;
        long timeSeconds = timeElapsed / 1000;
        String time = getTimeAsString(timeSeconds);

        TextView dateTextView = (TextView) convertView.findViewById(R.id.date_text_view);
        dateTextView.setText(time);

        String thumbnail = currentArticle.getThumbnail();
        ImageView thumbnailImageView = (ImageView) convertView
                .findViewById(R.id.thumbnail_image_view);
        if (thumbnail == "No Image") {
            thumbnailImageView.setImageResource(R.drawable.no_image);
        } else {
            new DownloadImageTask(thumbnailImageView)
                    .execute(currentArticle.getThumbnail());
        }

        return convertView;
    }

    // Method to to display time elapsed in seconds, minutes, hours, days, etc.
    private String getTimeAsString(long seconds) {
        if (seconds < 60) {     // rule 1
            return String.format("%s seconds ago", seconds);
        } else if (seconds < 120) {
            return String.format("%s minute ago", seconds / 60);
        } else if (seconds < 3600) {  // rule 2
            return String.format("%s minutes ago", seconds / 60);
        } else if (seconds < 7200) {
            return String.format("%s hour ago", seconds / 3600);
        } else if (seconds < 86400) {
            return String.format("%s hours ago", seconds / 3600);
        } else if (seconds < 172800) {
            return String.format("%s day ago", seconds / 86400);
        } else if (seconds < 604800) {
            return String.format("%s days ago", seconds / 86400);
        } else if (seconds < 1209600) {
            return String.format("%s week ago", seconds / 604800);
        } else if (seconds < 2629800) {
            return String.format("%s weeks ago", seconds / 604800);
        } else if (seconds < 5259600) {
            return String.format("%s month ago", seconds / 2629800);
        } else if (seconds < 31557600) {
            return String.format("%s months ago", seconds / 2629800);
        } else if (seconds < 63115200) {
            return String.format("%s year ago", seconds / 31557600);
        } else {
            return String.format("%s years ago", seconds / 31557600);
        }
    }
}

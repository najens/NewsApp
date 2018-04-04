package com.example.android.newsapp.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.newsapp.image.DownloadImageTask;
import com.example.android.newsapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.android.newsapp.activities.ArticleActivity.KEY_AUTHOR;
import static com.example.android.newsapp.activities.ArticleActivity.KEY_BODY;
import static com.example.android.newsapp.activities.ArticleActivity.KEY_DATE;
import static com.example.android.newsapp.activities.ArticleActivity.KEY_SUBJECT;
import static com.example.android.newsapp.activities.ArticleActivity.KEY_THUMBNAIL;
import static com.example.android.newsapp.activities.ArticleActivity.KEY_TITLE;
import static com.example.android.newsapp.activities.ArticleActivity.KEY_URL;
import static com.example.android.newsapp.R.id.article_subject_text_view;
import static com.example.android.newsapp.R.id.article_title_text_view;

public class ArticlePage extends AppCompatActivity {

    private Date dateObject;
    private String url = "";

    // Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    // Return the formatted date string (i.e. "4:30 PM") from a Date object.
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_page);

        String title = "";
        String subject = "";
        String author = "";
        String date = "";
        String thumbnail = "";
        String body = "";

        Intent intent = getIntent();
        if (null != intent) {
            title = intent.getStringExtra(KEY_TITLE);
            subject = intent.getStringExtra(KEY_SUBJECT);
            author = intent.getStringExtra(KEY_AUTHOR);
            date = intent.getStringExtra(KEY_DATE);
            thumbnail = intent.getStringExtra(KEY_THUMBNAIL);
            url = intent.getStringExtra(KEY_URL);
            body = intent.getStringExtra(KEY_BODY);
        }

        Button viewInBrowserButton = (Button) findViewById(R.id.view_in_browser_button);
        viewInBrowserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri articleUri = Uri.parse(url);
                android.util.Log.i("Time Class ", " Url " + articleUri);

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        TextView articleTitleTextView = (TextView) findViewById(article_title_text_view);
        articleTitleTextView.setText(title);

        TextView articleSubjectTextView = (TextView) findViewById(article_subject_text_view);
        articleSubjectTextView.setText(subject);

        TextView articleAuthorTextView = (TextView) findViewById(R.id.article_author_text_view);
        articleAuthorTextView.setText(author);

        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX");
        try {
            dateObject = dateFormatter.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String formattedDate = formatDate(dateObject);

        String formattedTime = formatTime(dateObject);

        TextView articleDateTextView = (TextView) findViewById(R.id.article_date_text_view);
        articleDateTextView.setText(formattedDate + " @ " + formattedTime);

        TextView articleBodyTextView = (TextView) findViewById(R.id.article_body_text_view);
        articleBodyTextView.setText(Html.fromHtml(body));

        ImageView articleImageView = (ImageView) findViewById(R.id.article_image_view);
        if (thumbnail == "No Image") {
            articleImageView.setVisibility(View.GONE);

        } else {
            new DownloadImageTask(articleImageView)
                    .execute(thumbnail);
        }
    }
}

package com.example.android.newsapp;

/**
 * Created by Nate on 10/20/2017.
 */

public class Article {
    //Define variables
    private String mTitle;
    private String mSubject;
    private String mAuthor;
    private String mDate;
    private String mThumbnail;
    private String mUrl;
    private String mBody;

    public Article(String title, String subject, String author, String date, String thumbnail, String url, String body) {
        mTitle = title;
        mSubject = subject;
        mAuthor = author;
        mDate = date;
        mThumbnail = thumbnail;
        mUrl = url;
        mBody = body;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getSubject() {
        return mSubject;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getDate() {
        return mDate;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public String getUrl() {
        return mUrl;
    }

    public String getBody() {
        return mBody;
    }
}

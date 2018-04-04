package com.example.android.newsapp.activities;

import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.android.newsapp.Article;
import com.example.android.newsapp.ArticleAdapter;
import com.example.android.newsapp.R;
import com.example.android.newsapp.loader.ArticleLoader;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class ArticleActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Article>> {

    private ArticleAdapter mAdapter;
    private Article currentArticle;
    private TextView mEmptyStateTextView;
    private SearchView searchView;
    private static String mQuery = "";
    private static final int ARTICLE_LOADER_ID = 1;
    private static final String LOG_TAG = ArticleActivity.class.getName();

    public static final String KEY_TITLE = "article_title";
    public static final String KEY_SUBJECT = "article_subject";
    public static final String KEY_AUTHOR = "article_author";
    public static final String KEY_DATE = "article_date";
    public static final String KEY_THUMBNAIL = "article_thumbnail";
    public static final String KEY_URL = "article_url";
    public static final String KEY_BODY = "article_body";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        final ListView articleListView = (ListView) findViewById(R.id.list);
        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        articleListView.setEmptyView(mEmptyStateTextView);

        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());

        articleListView.setAdapter(mAdapter);

        // If there is a network connection, fetch data
        if (isConnected()) {

            Log.i(LOG_TAG, "TEST: connected to internet ...");
            LoaderManager loaderManager = getLoaderManager();

            Log.i(LOG_TAG, "TEST: calling initLoader() ...");
            loaderManager.initLoader(ARTICLE_LOADER_ID, null, this);

        } else {

            Log.i(LOG_TAG, "TEST: not connected to internet ...");

            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            mEmptyStateTextView.setText(R.string.no_internet);
        }

        // Set an item click listener on the ListView, which sends an intent to open the
        // article in a new activity.
        articleListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                currentArticle = mAdapter.getItem(position);

                // Create an intent to open the web article in browser when item is clicked
                //Uri articleUri = Uri.parse(currentArticle.getUrl());
                //Intent websiteIntent = new Intent(Intent.ACTION_VIEW, articleUri);
                //startActivity(websiteIntent);

                // Create an intent to open the web article in the Article Page activity
                Intent articlePageIntent = new Intent(ArticleActivity.this, ArticlePage.class);
                articlePageIntent.putExtra(KEY_TITLE, currentArticle.getTitle());
                articlePageIntent.putExtra(KEY_SUBJECT, currentArticle.getSubject());
                articlePageIntent.putExtra(KEY_AUTHOR, currentArticle.getAuthor());
                articlePageIntent.putExtra(KEY_DATE, currentArticle.getDate());
                articlePageIntent.putExtra(KEY_THUMBNAIL, currentArticle.getThumbnail());
                articlePageIntent.putExtra(KEY_URL, currentArticle.getUrl());
                articlePageIntent.putExtra(KEY_BODY, currentArticle.getBody());

                startActivity(articlePageIntent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView =
                (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSubmitButtonEnabled(true);
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mQuery = query;
                Log.i(LOG_TAG, "TEST: query value = " + mQuery);

                if (isConnected()) {
                    Log.i(LOG_TAG, "TEST: connected to internet ...");
                    getLoaderManager().restartLoader(ARTICLE_LOADER_ID, null, ArticleActivity.this);
                    return true;
                } else {
                    mEmptyStateTextView.setText(R.string.no_internet);
                    mEmptyStateTextView.setVisibility(View.GONE);
                    Log.i(LOG_TAG, "TEST: not connected to internet ...");
                    return false;
                }
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    final private boolean isConnected() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public Loader<List<Article>> onCreateLoader(int i, Bundle bundle) {
        Log.i(LOG_TAG, "TEST: onCreateLoader() called ...");

        Uri.Builder builder = new Uri.Builder();
        builder.scheme("http")
                .authority("content.guardianapis.com")
                .appendPath("search")
                .appendQueryParameter("q", mQuery);
        String ARTICLE_REQUEST_URL = URLDecoder.decode(builder.build().toString());
        ARTICLE_REQUEST_URL = ARTICLE_REQUEST_URL + "&section=sport&show-fields=all&from-date=" +
                "2015-01-01&order-by=newest&api-key=7533b0a5-500c-4e17-a475-6368e778724b";

        return new ArticleLoader(this, ARTICLE_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> articles) {
        Log.i(LOG_TAG, "TEST: onLoadFinished() called ...");
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_articles);

        mAdapter.clear();

        if (articles != null && !articles.isEmpty()) {
            mAdapter.addAll(articles);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        mAdapter.clear();
    }
}

package com.example.android.newsappone;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks <List <Article>> {

    public static final String LOG_TAG = MainActivity.class.getName ();

    //Value for articleLoader ID.

    private static final int ARTICLE_LOADER_ID = 1;

    //URL searched

    private static final String GUARDIAN_REQUEST_URL ="https://content.guardianapis.com/search?q=JohnMcCain&show-tags=contributor&api-key=4f195ab0-9a93-42ea-9f4f-55fd41f55d97";

    //*TextView that is displayed with list is empty

    private TextView mEmptyStateTextView;

    //*Adapter for list of articles

    private ArticleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        //*Initialize ListView
        ListView articleListView = (ListView) findViewById ( R.id.list );

        //*Initialize empty state
        mEmptyStateTextView = (TextView) findViewById ( R.id.empty_view );

        articleListView.setEmptyView ( mEmptyStateTextView );

        //*create global adapter with custome adapter

        mAdapter = new ArticleAdapter ( this, new ArrayList <Article> () );
        //*set adapter on listview

        articleListView.setAdapter ( mAdapter );

        //*set item click listener on listview to send intent to browser

        articleListView.setOnItemClickListener ( new AdapterView.OnItemClickListener () {
            @Override

            public void onItemClick(AdapterView <?> adapterView, View view, int position, long l) {

                //*find the current article that was clicked on

                Article currentArticle = mAdapter.getItem ( position );

                //*convert string url into uri object

                Uri articleUri = Uri.parse ( currentArticle.getUrl () );

                //*create a new intent to view article uri

                Intent websiteIntent = new Intent ( Intent.ACTION_VIEW, articleUri );

                startActivity ( websiteIntent );

            }
        } );

//* check for network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService ( Context.CONNECTIVITY_SERVICE );

        //*get details on active default network

        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo ();

        //*if network connection, get data

        if (networkInfo != null && networkInfo.isConnected ()) {
            LoaderManager loaderManager = getLoaderManager ();

            //*initalize loader. this activity implements loader callback interface

            loaderManager.initLoader ( ARTICLE_LOADER_ID, null, this );

        } else {

            //*otherwise display error message

            View loadingIndicator = findViewById ( R.id.loading_indicator );

            loadingIndicator.setVisibility ( View.GONE );

            //*Update empty state with no connection error message

            mEmptyStateTextView.setText ( R.string.no_internet_connection );

        }
    }

    @Override

    public Loader <List <Article>> onCreateLoader(int i, Bundle bundle) {
        //*create a new loader for the given uRL

        return new ArticleLoader ( this, GUARDIAN_REQUEST_URL );
    }

    @Override

    public void onLoadFinished(Loader <List <Article>> loader, List <Article> articles) {

        View loadingIndicator = findViewById ( R.id.loading_indicator );
        loadingIndicator.setVisibility ( View.GONE );

        //*SET empty state text message

        mEmptyStateTextView.setText ( R.string.no_articles );

        //*clear the adapter of previous data
        mAdapter.clear ();

        //*if there is a valid list of articles then add them

        if (articles != null && !articles.isEmpty ()) {
            mAdapter.addAll ( articles );
        }
    }

    @Override

    public void onLoaderReset(Loader <List <Article>> loader) {
        //*loader reset
        mAdapter.clear ();

    }

    @Override

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater ().inflate ( R.menu.main, menu );

        return true;

    }


    @Override

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId ();

        if (id == R.id.action_settings) {

            Intent settingsIntent = new Intent ( this, SettingsActivity.class );

            startActivity ( settingsIntent );

            return true;

        }

        return super.onOptionsItemSelected ( item );

    }



}

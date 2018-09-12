package com.example.android.newsappone;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getName ();

    private QueryUtils() {

    }

    private static URL createUrl(String stringUrl) {
        URL url = null;

        try {

            url = new URL ( stringUrl );
        } catch (MalformedURLException e) {

            Log.e ( LOG_TAG, "Error retrieving URL", e );
        }
        return url;
    }

    //* make http request, return a string as response

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        //* if URL is null, return early

        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {

            urlConnection = (HttpURLConnection) url.openConnection ();
            urlConnection.setReadTimeout ( 10000/*milliseconds*/ );
            urlConnection.setConnectTimeout ( 15000/*milliseconds*/ );
            urlConnection.setRequestMethod ( "GET" );
            urlConnection.connect ();

            //* if request successful read input stream and parse response*/

            if (urlConnection.getResponseCode () == 200) {
                inputStream = urlConnection.getInputStream ();

                jsonResponse = readFromStream ( inputStream );
            } else {

                Log.e ( LOG_TAG, "Error response code:" + urlConnection.getResponseCode () );
            }
        } catch (IOException e) {

            Log.e ( LOG_TAG, "Problem retrieving JSON results", e );
        } finally {

            if (urlConnection != null) {

                urlConnection.disconnect ();
            }

            if (inputStream != null) {
                //* Closing input stream could throw IOException
                inputStream.close ();

            }
        }

        return jsonResponse;

    }

    //*convert Inputstream into string which contains JSON response*/
    private static String readFromStream(InputStream inputStream) throws IOException {

        StringBuilder output = new StringBuilder ();

        if (inputStream != null) {

            InputStreamReader inputStreamReader = new InputStreamReader ( inputStream,
                    Charset.forName ( "UTF-8" ) );

            BufferedReader reader = new BufferedReader ( inputStreamReader );

            String line = reader.readLine ();

            while (line != null) {

                output.append ( line );

                line = reader.readLine ();
            }
        }

        return output.toString ();
    }

    //*Query Guardian API and return list of article objects*/

    public static List <Article> fetchArticleData(String requestUrl) {

        URL url = createUrl ( requestUrl );

        //*Perform Http request to Url and receive json response

        String jsonResponse = null;

        try {

            jsonResponse = makeHttpRequest ( url );

        } catch (IOException e) {

            Log.e ( LOG_TAG, "Problem making HTTP request", e );

        }

        //* extract fields from JSON response and create list of articles*/

        List <Article> articles = extractArticleFromJson ( jsonResponse );

        return articles;
    }

    //*return a list of objects*//

    public static List <Article> extractArticleFromJson(String articleJSON) {

        //* if the JSON string is empty return early*//

        if (TextUtils.isEmpty ( articleJSON )) {

            return null;

        }

        //*Create an empty array list

        List <Article> articles = new ArrayList <> ();

        //*Parse the Guardian API if problem occurs, exception will be thrown.*/

        try {

            JSONObject baseJsonResponse = new JSONObject ( articleJSON );

            JSONObject responseObject = baseJsonResponse.getJSONObject ( "response" );

            JSONArray articlesArray = responseObject.getJSONArray ( "results" );

            for (int i = 0; i < articlesArray.length (); i++) {

                //* build up a list of articles objects

                JSONObject currentArticle = articlesArray.getJSONObject ( i );

                String title = currentArticle.getString ( "web" +
                        "Title" );

                String section = currentArticle.getString ( "sectionName" );

                String date = currentArticle.getString ( "webPublicationDate" );

                String url = currentArticle.getString("webUrl");

                //*Extract tags array to obtain contributor when noted
                JSONArray ArticlesTagsArray = currentArticle.getJSONArray ( "tags" );
                String contributor = null;

                if (ArticlesTagsArray != null) {
                    JSONObject tagsObject = ArticlesTagsArray.getJSONObject ( 0 );

                    //*extract name of contributor
                    if (tagsObject != null) {
                        contributor = tagsObject.getString ( "webTitle" );
                    } else {
                        contributor = "Unknown Contributor";
                    }
                }

                Article article = new Article ( title,section,contributor,date,url);

                articles.add ( article );
            }

        } catch (JSONException e) {

            //* if error is thrown when statements in try block is executed,catch exceptions here*//

            Log.e ( "QueryUtils", "Problem parsing JSON results", e );
        }
        //* return the list of articles*//

        return articles;

    }
}

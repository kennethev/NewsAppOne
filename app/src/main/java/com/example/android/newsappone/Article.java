package com.example.android.newsappone;

public class Article {

    //*Article title
    private String mTitle;

    //* Article section
    private String mSection;

    //*Article contributor
    private String mContributor;

    //*Article publication date
    private String mDate;

    //*Article web url
    private String mUrl;

    public Article(String title, String section, String contributor,String date, String url) {

        mTitle = title;
        mSection = section;
        mContributor = contributor;
        mDate = date;
        mUrl = url;

    }//get the title of the news article

    public String getTitle() {
        return mTitle;
    }

    public String getSection() {
        return mSection;
    }

    public String getContributor(){
        return mContributor;
    }
    public String getDate() {
        return mDate;
    }

    public String getUrl() {
        return mUrl;
    }

}

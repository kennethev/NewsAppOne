package com.example.android.newsappone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class ArticleAdapter extends ArrayAdapter <Article> {
    private String title;

    private String section;
    private String contributor;
    private String date;
    private String url;
    //*constructor

    public ArticleAdapter(Context context, List <Article> articles) {

        super ( context, 0, articles );
    }

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        //*Check if there is an existing list item view that we can reuse.
        //Otherwise, if view is null, then inflate a new list item layout.

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from ( getContext () ).inflate ( R.layout.list_item_view, parent, false );
        }
        //*find article at given position from article list

        Article currentArticle = getItem ( position );

        //*find Textview with the label "Title"

        TextView titleView = listItemView.findViewById ( R.id.title );
        titleView.setText ( currentArticle.getTitle () );

        //*find TextView with id "Section"
        TextView sectionView = listItemView.findViewById ( R.id.section );
        sectionView.setText ( currentArticle.getSection () );


        //*find TextView with id "Contributor"
        TextView contributorView = listItemView.findViewById ( R.id.contributor );
        contributorView.setText ( currentArticle.getContributor ());


        //*get the publication date of article
        TextView dateView = listItemView.findViewById ( R.id.date );
        dateView.setText ( currentArticle.getDate () );

        //*get the URL article


        TextView urlView = listItemView.findViewById ( R.id.url );
        urlView.setText ( currentArticle.getUrl () );

        return listItemView;
    }


}
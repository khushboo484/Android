package com.example.englishapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class WordAdaptor extends ArrayAdapter<Word>  {

    /**
     * Resource Id to store the background color
     */
    private int mColorResource;

    public WordAdaptor(Activity context, ArrayList<Word> TransaltionWords, int Resourceid) {
        super(context, 0, TransaltionWords);
        mColorResource = Resourceid;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listViewItem = convertView;
        // Check if the existing view is reused, otherwise inflate the view
        if(listViewItem == null) {
            listViewItem = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        // Get the {@link Word} object located at this position in the list
        Word currentWord = getItem(position);

        // Find the textView in the list_item.xml layout with the ID english text
        TextView englishTextView = (TextView) listViewItem.findViewById(R.id.English_text);
        // Get the name from the current Word object and set the text on the
        englishTextView.setText(currentWord.getEnglishTranslation());

        // Find the ImageView in the list_item.xml layout with the ID image.
        ImageView iconView = (ImageView) listViewItem.findViewById(R.id.photo);
        if(currentWord.hasImage()) {
            // Set the ImageView to the image resource specified in the current Word
            iconView.setImageResource(currentWord.getReasourceId());

            //Make sure the view is visible
            iconView.setVisibility(View.VISIBLE);
        }
        else {
            // Otherwise hide the ImageView (set the visibility to GONE)
            iconView.setVisibility(View.GONE);
        }

        // Set the theme color for the list item
        View textContainer = listViewItem.findViewById(R.id.English_text);
        // Find the color that the resource ID maps to
        int color = ContextCompat.getColor(getContext(), mColorResource);
        textContainer.setBackgroundColor(color);
        return listViewItem;
    }
}
package com.example.creatinguser;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

// custom view holder adapter for recyclerview to show headlines
public class CustomViewHolder extends RecyclerView.ViewHolder {

    // objects for our view items created in headline_list_items.xml
    TextView text_title, text_source;
    ImageView img_headline;
    CardView cardView;

    // constructor
    public CustomViewHolder(@NonNull View itemView) {
        super(itemView);

        // connect to .xml ids
        text_title = itemView.findViewById(R.id.text_title);
        text_source = itemView.findViewById(R.id.text_source);
        img_headline = itemView.findViewById(R.id.img_headline);
        cardView = itemView.findViewById(R.id.main_container);
    }
}
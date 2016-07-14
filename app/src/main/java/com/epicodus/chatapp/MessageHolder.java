package com.epicodus.chatapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Guest on 7/14/16.
 */
public class MessageHolder extends RecyclerView.ViewHolder {
    View mView;

    public MessageHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setText(String text) {
        TextView field = (TextView) mView.findViewById(android.R.id.text1);
        field.setText(text);
    }

    public void setName(String name) {
        TextView field = (TextView) mView.findViewById(android.R.id.text2);
        field.setText(name);
    }
}

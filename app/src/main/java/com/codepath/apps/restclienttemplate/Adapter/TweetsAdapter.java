package com.codepath.apps.restclienttemplate.Adapter;

import android.content.Context;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import com.codepath.apps.restclienttemplate.Activity.ProfileActivity;
import com.codepath.apps.restclienttemplate.PatternEditableBuilder;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;


import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.regex.Pattern;


public class TweetsAdapter extends ArrayAdapter<Tweet> {
    public TweetsAdapter(Context context, ArrayList<Tweet> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Tweet tweet = getItem(position);
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_tweet, parent, false);
            viewHolder.ivProfileImage = (ImageView) convertView.findViewById(R.id.ivProfileImage);
            viewHolder.tvUserName = (TextView) convertView.findViewById(R.id.tvUserName);
            viewHolder.tvBody = (TextView) convertView.findViewById(R.id.tvBody);
            viewHolder.tvTimeStamp = (TextView) convertView.findViewById(R.id.tvTimeStamp);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tvUserName.setText(tweet.getUser().getName());
        viewHolder.tvBody.setText(tweet.getBody());
        new PatternEditableBuilder().addPattern(Pattern.compile("\\@(\\w+)"), Color.BLUE,
                new PatternEditableBuilder.SpannableClickedListener(){
                    @Override
                    public void onSpanClicked(String text) {
                        //  Click Action
                        Toast.makeText(getContext(), "Clicked username: " + text,
                                Toast.LENGTH_SHORT).show();
                    }
                }).into(viewHolder.tvBody);
        viewHolder.tvTimeStamp.setText(tweet.getCreateAt());
        Glide.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(viewHolder.ivProfileImage);
        viewHolder.ivProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("DEBUG", tweet.getUser().toString());
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("user", Parcels.wrap(tweet.getUser()));
                v.getContext().startActivity(intent);
            }
        });
        return convertView;
    }

    public static class ViewHolder {
        ImageView ivProfileImage;
        TextView tvUserName;
        TextView tvBody;
        TextView tvTimeStamp;
    }
}

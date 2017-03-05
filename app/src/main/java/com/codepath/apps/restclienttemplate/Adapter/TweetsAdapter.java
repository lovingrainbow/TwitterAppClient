package com.codepath.apps.restclienttemplate.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.resource;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Quietus on 2017/3/4.
 */

public class TweetsAdapter extends ArrayAdapter<Tweet> {
    public TweetsAdapter(Context context, ArrayList<Tweet> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Tweet tweet = getItem(position);
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
        viewHolder.tvTimeStamp.setText(tweet.getCreateAt());
        Glide.with(getContext()).load(tweet.getUser().getProfileImageUrl()).into(viewHolder.ivProfileImage);

        return convertView;
    }

    public static class ViewHolder {
        ImageView ivProfileImage;
        TextView tvUserName;
        TextView tvBody;
        TextView tvTimeStamp;
    }


}

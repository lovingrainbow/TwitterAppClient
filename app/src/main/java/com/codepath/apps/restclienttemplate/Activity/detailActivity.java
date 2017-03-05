package com.codepath.apps.restclienttemplate.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;
import org.w3c.dom.Text;

public class detailActivity extends AppCompatActivity {
    private ImageView ivDetialImg;
    private TextView tvDetialName;
    private TextView tvDetialTimeStamp;
    private TextView tvDetialBody;
    private Tweet tweets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setView();
    }

    private void setView() {
        tweets = (Tweet) Parcels.unwrap(getIntent().getParcelableExtra("tweet"));
        ivDetialImg = (ImageView)findViewById(R.id.ivDetialImg);
        Glide.with(this).load(tweets.getUser().getProfileImageUrl()).into(ivDetialImg);
        tvDetialName = (TextView)findViewById(R.id.tvDetialName);
        tvDetialName.setText(tweets.getUser().getName());
        tvDetialTimeStamp = (TextView)findViewById((R.id.tvDetialTimeStamp));
        tvDetialTimeStamp.setText(tweets.getCreateAt());
        tvDetialBody = (TextView)findViewById(R.id.tvDetialBody);
        tvDetialBody.setText(tweets.getBody());
    }
}

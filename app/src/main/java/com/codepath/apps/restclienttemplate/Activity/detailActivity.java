package com.codepath.apps.restclienttemplate.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

public class detailActivity extends AppCompatActivity {
    private ImageView ivDetialImg;
    private TextView tvDetialName;
    private TextView tvDetialTimeStamp;
    private TextView tvDetialBody;
    private Tweet tweets;
    private Button btnLike;
    private Button btnReply;
    private Button btnRetweet;
    private TextView tvFavorateCount;
    private TextView tvRetweetCount;
    private TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        //  init client
        client = TwitterApplication.getRestClient();

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
        btnLike = (Button)findViewById(R.id.btnLike);
        if (tweets.isFavorated()){
            btnLike.setBackgroundResource(R.drawable.ic_liked);
        }else{
            btnLike.setBackgroundResource(R.drawable.ic_like);
        }
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tweets.isFavorated()){
                    // like action
                    client.unlike(tweets.getUid(), new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            tweets = Tweet.fromJSON(response);
                            btnLike.setBackgroundResource(R.drawable.ic_like);
                            tvFavorateCount.setText(String.valueOf(tweets.getFavoratecount()));
                        }
                    });
                }else{
                    //  unlike action
                    client.like(tweets.getUid(), new JsonHttpResponseHandler(){
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            tweets = Tweet.fromJSON(response);
                            btnLike.setBackgroundResource(R.drawable.ic_liked);
                            tvFavorateCount.setText(String.valueOf(tweets.getFavoratecount()));
                        }
                    });
                }
            }
        });
        btnReply = (Button)findViewById(R.id.btnReply);
        btnRetweet = (Button)findViewById(R.id.btnRetweet);
        btnRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                client.retweet(tweets.getUid(), new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                        tweets = Tweet.fromJSON(response);
                        tvRetweetCount.setText(String.valueOf(tweets.getRetweetcount()));
                    }
                });
            }
        });
        tvFavorateCount = (TextView)findViewById(R.id.tvFavorateCount);
        tvFavorateCount.setText(String.valueOf(tweets.getFavoratecount()));
        tvRetweetCount = (TextView)findViewById(R.id.tvRetweetCount);
        tvRetweetCount.setText(String.valueOf(tweets.getRetweetcount()));
    }
}

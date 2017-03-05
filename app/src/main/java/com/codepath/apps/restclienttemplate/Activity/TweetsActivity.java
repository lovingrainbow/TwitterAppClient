package com.codepath.apps.restclienttemplate.Activity;

import android.content.Intent;
import android.graphics.ComposePathEffect;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.Adapter.TweetsAdapter;
import com.codepath.apps.restclienttemplate.EndlessScrollListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class TweetsActivity extends AppCompatActivity {
    private final int REQUEST_CODE = 20;
    private TwitterClient client;
    private Tweet tweets;
    private ArrayList<Tweet> aTweets;
    private TweetsAdapter tweetsAdapter;
    private ListView tweetsListView;
    private SwipeRefreshLayout swipeContainer;
    private User myProfile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);
        setView();








    }

    private void setView(){
        // init client
        client = TwitterApplication.getRestClient();    //singleton client
        // get first pop
        client.getHomeTimeline(null, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                aTweets.addAll(Tweet.froJSONArray(response));
                tweetsListView.setAdapter(tweetsAdapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e("ERROR", errorResponse.toString());
            }
        });
        // get myprofile
        client.getAccountVerifyCredentials(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                myProfile = User.fromJSON(response);
            }
        });


        // init ArrayList, ArrayAdapter, listview
        aTweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(TweetsActivity.this, aTweets);
        tweetsListView = (ListView)findViewById(R.id.lvTweets);
        // set listview onscrolllistener
        tweetsListView.setOnScrollListener(new EndlessScrollListener() {

            private long getMaxId(int totalItemsCount){

                try{
                    long max_id = aTweets.get(totalItemsCount - 1).getUid() - 1;
                    return max_id;
                }catch (NullPointerException e){
                    e.printStackTrace();
                    return 0;
                }

            }
            @Override
            public boolean onLoadMore(int page, int totalItemCount) {
                client.getHomeTimeline(getMaxId(totalItemCount), new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        tweetsAdapter.addAll(Tweet.froJSONArray(response));
                        tweetsAdapter.notifyDataSetChanged();
                    }

                });
                return true;
            }
        });
        // listview select action
        tweetsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(TweetsActivity.this, detailActivity.class);
                tweets = tweetsAdapter.getItem(position);
                intent.putExtra("tweet", Parcels.wrap(tweets));
                startActivity(intent);
            }
        });

        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.activity_tweets);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // refresh action
                client.getRefreshTimeline(getSinceId(), new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                        for(int i = response.length() - 1; i >= 0; i--){
                            try{
                                tweetsAdapter.insert(Tweet.fromJSON(response.getJSONObject(i)), 0);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        tweetsAdapter.notifyDataSetChanged();
                        swipeContainer.setRefreshing(false);
                    }
                });
                Toast.makeText(TweetsActivity.this, "Refresh Finish", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private long getSinceId(){
        if (aTweets == null){
            return 0;
        }else {
            return aTweets.get(0).getUid();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tweets, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menuCompose:
                // open new activity
                Intent intent = new Intent(this, ComposeActivity.class);
                intent.putExtra("user", Parcels.wrap(myProfile));
                startActivityForResult(intent, REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE){
            String body = data.getExtras().getString("body");
            client.postTweet(body, new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    tweets = Tweet.fromJSON(response);
                    tweetsAdapter.insert(tweets, 0);
                    tweetsAdapter.notifyDataSetChanged();
                }
            });
        }
    }
}


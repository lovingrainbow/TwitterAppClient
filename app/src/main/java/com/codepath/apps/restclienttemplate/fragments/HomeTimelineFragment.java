package com.codepath.apps.restclienttemplate.fragments;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;


public class HomeTimelineFragment extends TweetsListFragment {
    private TwitterClient client;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // init client
        client = TwitterApplication.getRestClient();    //singleton client
        // get first pop
        client.getHomeTimeline(null , new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                addAll(Tweet.froJSONArray(response));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e("ERROR", errorResponse.toString());
            }
        });
    }

    @Override
    protected void populateTimeline(long maxId) {
        // init client
        client = TwitterApplication.getRestClient();    //singleton client
        // get first pop
        client.getHomeTimeline(maxId, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                addAll(Tweet.froJSONArray(response));

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                Log.e("ERROR", errorResponse.toString());
            }
        });
    }
}

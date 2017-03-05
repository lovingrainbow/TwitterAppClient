package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scribe.builder.api.TwitterApi;

import java.util.ArrayList;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

/**
 * Created by Quietus on 2017/3/3.
 */

public class Tweet {
    private String body;
    private long uid;
    private String createAt;
    private User user;

    public String getBody() {
        return body;
    }

    public String getCreateAt() {
        return createAt;
    }

    public long getUid() {
        return uid;
    }

    public User getUser() {
        return user;
    }

    public static Tweet fromJSON(JSONObject jsonObject){
        Tweet tweet = new Tweet();
        try{
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            tweet.createAt = ParseRelativeDate.getRelativeTimeAgo(jsonObject.getString("created_at"));
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        }catch (JSONException e){
            e.printStackTrace();
        }
        return tweet;
    }

    public static ArrayList<Tweet> froJSONArray(JSONArray jsonArray){
        ArrayList<Tweet> tweetArrayList = new ArrayList<>();
        for(int i = 0;i < jsonArray.length();i++){
            try{
                Tweet tweet = Tweet.fromJSON(jsonArray.getJSONObject(i));
                if (tweet != null){
                    tweetArrayList.add(tweet);
                }
            }catch (JSONException e){
                e.printStackTrace();
                continue;
            }
        }
        return tweetArrayList;
    }
}

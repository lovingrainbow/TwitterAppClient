package com.codepath.apps.restclienttemplate.models;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

/**
 * Created by Quietus on 2017/3/3.
 */
@Parcel
public class User {
    //list attributes
    private String name;
    private long uid;
    private String screenName;
    private String profileImageUrl;
    private int followersCount;
    private int friendsCount;
    private int statusesCount;
    private String description;

    public String getName() {
        return name;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public String getScreenName() {
        return screenName;
    }

    public long getUid() {
        return uid;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFriendsCount() {
        return friendsCount;
    }

    public int getStatusesCount() {
        return statusesCount;
    }

    public String getDescription() {
        return description;
    }

    public User() {

    }

    public static  User fromJSON(JSONObject jsonObject){
        User user = new User();
        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
            user.friendsCount = jsonObject.getInt("friends_count");
            user.followersCount = jsonObject.getInt("followers_count");
            user.statusesCount = jsonObject.getInt("statuses_count");
            user.description = jsonObject.getString("description");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return user;
    }
}

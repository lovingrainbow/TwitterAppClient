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

    public User() {

    }

    public static  User fromJSON(JSONObject jsonObject){
        User user = new User();
        try {
            user.name = jsonObject.getString("name");
            user.uid = jsonObject.getLong("id");
            user.screenName = jsonObject.getString("screen_name");
            user.profileImageUrl = jsonObject.getString("profile_image_url");
        }catch (JSONException e){
            e.printStackTrace();
        }
        return user;
    }
}

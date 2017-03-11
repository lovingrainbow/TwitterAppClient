package com.codepath.apps.restclienttemplate.Activity;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.fragments.UserTimelineFragment;
import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;
import org.w3c.dom.Text;

public class ProfileActivity extends AppCompatActivity {
    private User myProfile;
    private ImageView ivProfileImage;
    private TextView tvName;
    private TextView tvTagline;
    private TextView tvFollowers;
    private TextView tvFollowing;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //  get the screen name
        myProfile = (User) Parcels.unwrap(getIntent().getParcelableExtra("user"));

        //  Actionbar change title
        getSupportActionBar().setTitle("@" + myProfile.getScreenName());
        //  create usertimeline fragment
        if (savedInstanceState == null){
            UserTimelineFragment fragment = UserTimelineFragment.newInstance(myProfile);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, fragment);
            ft.commit();
        }
        //  display user time line fragment within this activity

        setView();


    }

    private void setView() {
        ivProfileImage = (ImageView)findViewById(R.id.ivProfileImage);
        tvName = (TextView)findViewById(R.id.tvName);
        tvTagline = (TextView)findViewById(R.id.tvTagline);
        tvFollowers = (TextView)findViewById(R.id.tvFollowers);
        tvFollowing = (TextView)findViewById(R.id.tvFollowing);
        Glide.with(this).load(myProfile.getProfileImageUrl()).into(ivProfileImage);
        tvName.setText(myProfile.getName());
        tvTagline.setText(myProfile.getDescription());
        tvFollowers.setText(myProfile.getFollowersCount() + " Followers");
        tvFollowing.setText(myProfile.getFriendsCount() + " Following");
    }
}

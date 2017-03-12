package com.codepath.apps.restclienttemplate.Activity;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.restclienttemplate.Adapter.SmartFragmentStatePagerAdapter;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.fragments.HomeTimelineFragment;
import com.codepath.apps.restclienttemplate.fragments.MentionsTimelineFragment;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;
import org.parceler.Parcels;

import java.io.IOException;

import cz.msebera.android.httpclient.Header;


public class TweetsActivity extends AppCompatActivity {

    private final int REQUEST_CODE = 20;
    private TwitterClient client;
    private ViewPager viewPager;
    private Tweet tweets;

//    private SwipeRefreshLayout swipeContainer;
    private User myProfile;
//    private TweetsListFragment fragmentTweetsList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets);
        setView();


        //  Get the viewpager
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        //  set the viewpager adapter for the pager
        viewPager.setAdapter(new TweetsPagerAdapter(getSupportFragmentManager()));
        //  find the sliding tabstrip
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        //  attach the tabstrip to the viewpager
        tabStrip.setViewPager(viewPager);

    }

    private void setView(){
        if (!isNetworkAvailable() || !isOnline()){
            Toast.makeText(this, "Network not available", Toast.LENGTH_SHORT).show();
        }else{
            // init client
            client = TwitterApplication.getRestClient();    //singleton client
            // get myprofile
            client.getAccountVerifyCredentials(new JsonHttpResponseHandler(){
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    myProfile = User.fromJSON(response);
                }
            });
        }




//        // listview select action
//        tweetsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//            }
//        });
//
//        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.activity_tweets);
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // refresh action
//                client.getRefreshTimeline(getSinceId(), new JsonHttpResponseHandler(){
//                    @Override
//                    public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
//                        for(int i = response.length() - 1; i >= 0; i--){
//                            try{
//                                tweetsAdapter.insert(Tweet.fromJSON(response.getJSONObject(i)), 0);
//                            }catch (JSONException e){
//                                e.printStackTrace();
//                            }
//                        }
//                        tweetsAdapter.notifyDataSetChanged();
//                        swipeContainer.setRefreshing(false);
//                    }
//                });
//                Toast.makeText(TweetsActivity.this, "Refresh Finish", Toast.LENGTH_SHORT).show();
//            }
//        });

    }

//    private long getSinceId(){
//        if (aTweets == null){
//            return 0;
//        }else {
//            return aTweets.get(0).getUid();
//        }
//    }

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
                Intent i = new Intent(this, ComposeActivity.class);
                i.putExtra("myfile", Parcels.wrap(myProfile));
                startActivityForResult(i, REQUEST_CODE);
                return true;
            case R.id.menuProfile:
                Intent intent = new Intent(this, ProfileActivity.class);
                intent.putExtra("user", Parcels.wrap(myProfile));
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
    //  return the order of the fragments in the view paper
    public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter{
        private String tabTitles[] = {"Home", "Mentions"};
        //  Adapter gets the manager insert or remove fragment from activity
        public TweetsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0){
                return new HomeTimelineFragment();
            }else if (position == 1){
                return new MentionsTimelineFragment();
            }else{
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
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
                    HomeTimelineFragment fragment = (HomeTimelineFragment)
                            ((TweetsPagerAdapter)viewPager.getAdapter()).getRegisteredFragment(0);
                    fragment.insert(tweets, 0);
                }
            });
        }
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }
        return false;
    }
}


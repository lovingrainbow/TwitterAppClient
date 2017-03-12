package com.codepath.apps.restclienttemplate.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;


import com.codepath.apps.restclienttemplate.Activity.TweetsActivity;
import com.codepath.apps.restclienttemplate.Activity.detailActivity;
import com.codepath.apps.restclienttemplate.Adapter.TweetsAdapter;
import com.codepath.apps.restclienttemplate.EndlessScrollListener;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import static com.codepath.apps.restclienttemplate.R.menu.tweets;


public abstract class TweetsListFragment extends android.support.v4.app.Fragment {
    private ArrayList<Tweet> aTweets;
    private TweetsAdapter tweetsAdapter;
    private ListView tweetsListView;

    //  inflation logic
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list, container, false);
        //  find the listview
        tweetsListView = (ListView)v.findViewById(R.id.lvTweets);
        tweetsListView.setAdapter(tweetsAdapter);

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
                populateTimeline(getMaxId(totalItemCount));
                return true;
            }
        });

        //  item select listener
        tweetsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), detailActivity.class);
                intent.putExtra("tweet", Parcels.wrap(tweetsAdapter.getItem(position)));
                startActivity(intent);
            }
        });


        return v;
    }

    protected abstract void populateTimeline(long maxId);


    //  creation lifecycle event

    @Override
    public void onCreate(Bundle savedInstanceState) {
        aTweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(getActivity(), aTweets);
        super.onCreate(savedInstanceState);
    }

    public void addAll(List<Tweet> tweets){
        aTweets.addAll(tweets);
        tweetsAdapter.notifyDataSetChanged();
    }

    public void insert(Tweet tweet, int position){
        tweetsAdapter.insert(tweet, position);
        tweetsAdapter.notifyDataSetChanged();
    }
}

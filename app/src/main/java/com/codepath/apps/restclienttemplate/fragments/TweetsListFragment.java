package com.codepath.apps.restclienttemplate.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


import com.codepath.apps.restclienttemplate.Adapter.TweetsAdapter;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.models.Tweet;

import java.util.ArrayList;
import java.util.List;


public class TweetsListFragment extends android.support.v4.app.Fragment {
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
        return v;
    }

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
}

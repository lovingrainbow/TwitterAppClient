package com.codepath.apps.restclienttemplate.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.R;
import com.codepath.apps.restclienttemplate.TwitterApplication;
import com.codepath.apps.restclienttemplate.TwitterClient;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.models.User;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;
import org.parceler.Parcels;
import org.w3c.dom.Text;

import cz.msebera.android.httpclient.Header;

public class ComposeActivity extends AppCompatActivity {
    private ImageView ivUserImage;
    private TextView tvName;
    private EditText etPost;
    private TextView tvPostCount;
    private Button btnOK;
    private Button btnCancel;
    private TwitterClient twitterClient;
    private User myProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        setView();


    }



    private void setView() {
        ivUserImage = (ImageView) findViewById(R.id.ivUserImage);
        tvName = (TextView) findViewById(R.id.tvName);
        tvPostCount = (TextView)findViewById(R.id.tvPostCount);
        btnOK = (Button)findViewById(R.id.btnEditOK);
        btnCancel = (Button)findViewById(R.id.btnEditCancel);
        etPost = (EditText) findViewById(R.id.etPost);
        // editTextChange
        etPost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tvPostCount.setText(String.valueOf(140 - start));
                if (start > 140){
                    btnOK.setEnabled(false);
                }else{
                    btnOK.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        twitterClient = TwitterApplication.getRestClient();
        myProfile = (User) Parcels.unwrap(getIntent().getParcelableExtra("myfile"));
        // set myprofile data
        tvName.setText(myProfile.getName());
        Glide.with(this).load(myProfile.getProfileImageUrl()).into(ivUserImage);
    }

    public void EditOK(View view) {
        Intent intent = new Intent(this, TweetsActivity.class);
        intent.putExtra("body", etPost.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }

    public void EditCancel(View view) {
        // leave this page
        Intent intent = new Intent(this, TweetsActivity.class);
        setResult(RESULT_CANCELED, intent);
        finish();
    }
}

package com.example.creatinguser;


import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YoutubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    private static final String TAG = "YoutubeActivity";
    static final String GOOGLE_API_KEY = "AIzaSyAO9CdtiQeD5l7pmEnrYueVxvkaR-52gpw";
    static final String YOUTUBE_VIDEO_ID = "T0nFoJFn2gA";
    static final String NBA_HIGHLIGHTS = "PLlVlyGVtvuVm4_E_faSFuu3nU0F9O6XbR";
    static final String NFL_PLAYLIST = "PL1H2IyN18L0Sig6TkkAqLaoJFVBnukx9X";
    static final String MMA_PLAYLIST = "PLqYXv_L7NiEzgd0-1z7RmyZtirWffCZKb";
    static final String MLB_PLAYLIST = "PLL-lmlkrmJalROhW3PQjrTD6pHX1R0Ub8";
    static final String MLS_PLAYLIST = "PLcj4z4KsbIoXqdWKX405un2PyALNClE8C";
    static final String NHL_PLAYLIST = "PL1NbHSfosBuG_HB8WTgFaCVsDWCcnOsUW";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_youtube);
//        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.activity_youtube);

        // alternative way than on top
        ConstraintLayout layout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_youtube, null);
        setContentView(layout);


        YouTubePlayerView playerView = new YouTubePlayerView(this);
        playerView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)); // match parent will take all available space in layout
        layout.addView(playerView);
        playerView.initialize(GOOGLE_API_KEY,this);

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        Log.d(TAG, "onInitializationSuccess: provider is " + provider.getClass().toString());
        //Toast.makeText(this, "Initialized Youtube Player successfully", Toast.LENGTH_LONG).show();

        if(!wasRestored){
            youTubePlayer.cueVideo(YOUTUBE_VIDEO_ID);
        }

    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        final int REQUEST_CODE = 1;

        if(youTubeInitializationResult.isUserRecoverableError()){
            youTubeInitializationResult.getErrorDialog(this, REQUEST_CODE).show();
        }else{
            String errorMessage = String.format("There was an error initializing the YoutubePlaer (%1$s)", youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }

    }

}
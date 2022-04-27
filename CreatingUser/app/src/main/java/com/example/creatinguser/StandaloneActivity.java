package com.example.creatinguser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.youtube.player.YouTubeStandalonePlayer;

public class StandaloneActivity extends AppCompatActivity implements View.OnClickListener {

    CardView cvNba, cvNfl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standalone);

        cvNba = findViewById(R.id.cvNba);
        cvNfl = findViewById(R.id.cvNfl);

        Button btnPlayVideo = (Button) findViewById(R.id.btnPlayVideo);
        Button btnPlaylist = (Button) findViewById(R.id.btnPlaylist);

        btnPlayVideo.setOnClickListener(this);
        btnPlaylist.setOnClickListener(this);

        cvNba.setOnClickListener(this);
        cvNfl.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent intent = null;
        switch (view.getId()){
            case R.id.btnPlayVideo:
                intent = YouTubeStandalonePlayer.createVideoIntent(this, YoutubeActivity.GOOGLE_API_KEY, YoutubeActivity.YOUTUBE_VIDEO_ID);
                break;
            case R.id.btnPlaylist:
                intent = YouTubeStandalonePlayer.createPlaylistIntent(this, YoutubeActivity.GOOGLE_API_KEY, YoutubeActivity.YOUTUBE_PLAYLIST);
                break;
            case R.id.cvNba:
                intent = YouTubeStandalonePlayer.createPlaylistIntent(this, YoutubeActivity.GOOGLE_API_KEY, YoutubeActivity.YOUTUBE_PLAYLIST);
                break;
            case R.id.cvNfl:
                intent = YouTubeStandalonePlayer.createPlaylistIntent(this, YoutubeActivity.GOOGLE_API_KEY, YoutubeActivity.YOUTUBE_PLAYLIST);
                break;
            default:
        }

        if(intent != null){
            startActivity(intent);
        }
    }
}

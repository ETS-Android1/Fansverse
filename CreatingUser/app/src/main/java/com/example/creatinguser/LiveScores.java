package com.example.creatinguser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class LiveScores extends AppCompatActivity {
    private TextView Title;
    private Button homePage;
    private Button nbaScores;
    private Button nflScores;
    //private TextView currScoresText;
    //private Button stats;
    //private Button myProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_livescores);

        Title = findViewById(R.id.sportsTeamsMainPageFansverse);
        homePage = findViewById(R.id.buttonHomePage);
        nbaScores = findViewById(R.id.buttonNBAScores);
        nflScores = findViewById(R.id.buttonNFLScores);
        //currScoresText = findViewById(R.id.currScoresText);
        //stats = findViewById(R.id.buttonStats);
        //myProfile = findViewById(R.id.buttonProfile);

        homePage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
            }
        });

        nbaScores.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CurrentScoresNBA.class);
                startActivity(intent);
            }
        });

        nflScores.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), CurrentScoresNFL.class);
                startActivity(intent);
            }
        });
    }
}

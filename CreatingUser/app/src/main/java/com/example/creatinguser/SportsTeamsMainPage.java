package com.example.creatinguser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SportsTeamsMainPage extends AppCompatActivity {
    private TextView Title;
    private Button homePage;
    private Button nbaTeams;
    private Button nflTeams;
    //private TextView currScoresText;
    //private Button stats;
    //private Button myProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sportsteams);

        Title = findViewById(R.id.sportsTeamsMainPageFansverse);
        homePage = findViewById(R.id.buttonHomePage);
        nbaTeams = findViewById(R.id.buttonNBATeams);
        nflTeams = findViewById(R.id.buttonNFLTeams);
        //currScoresText = findViewById(R.id.currScoresText);
        //stats = findViewById(R.id.buttonStats);
        //myProfile = findViewById(R.id.buttonProfile);

        homePage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intent);
            }
        });

        nbaTeams.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NBATeamsMainPage.class);
                startActivity(intent);
            }
        });

        nflTeams.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NFLTeamsMainPage.class);
                startActivity(intent);
            }
        });
    }
}

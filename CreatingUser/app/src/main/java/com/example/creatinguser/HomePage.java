package com.example.creatinguser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {

    private TextView Title;
    private Button map;
    private Button liveScores;
    private Button stats;
    private Button myProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        Title = findViewById(R.id.homePageFansverse);
        map = findViewById(R.id.buttonMap);
        liveScores = findViewById(R.id.buttonLScores);
        stats = findViewById(R.id.buttonStats);
        myProfile = findViewById(R.id.buttonProfile);

        liveScores.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LiveScoresPage.class);
                startActivity(intent);
            }
        });
    }
}

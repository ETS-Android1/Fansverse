package com.example.creatinguser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.creatinguser.adapters.RecentConversationsAdapter;
import com.example.creatinguser.models.ChatMessage;
import com.example.creatinguser.utilities.PreferenceManager;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class HomePage extends AppCompatActivity {
    //
    private TextView Title;
    private Button map;
    private Button liveScores;
    private Button stats;
    private Button myProfile;
    Button btnNewsfeed;
    Button btnmessages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        Title = findViewById(R.id.homePageFansverse);
        map = findViewById(R.id.buttonMap);
        liveScores = findViewById(R.id.buttonLScores);
        stats = findViewById(R.id.buttonStats);
        myProfile = findViewById(R.id.buttonProfile);
        myProfile = findViewById(R.id.buttonProfile);
        btnNewsfeed = findViewById(R.id.btnNewsfeed);
        btnmessages = findViewById(R.id.messages);

        btnmessages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //intent.putExtra("message", current_username);
                startActivity(intent);
            }
        });

        btnNewsfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Newsfeed.class);
                //intent.putExtra("message", current_username);
                startActivity(intent);
            }
        });

    }
}


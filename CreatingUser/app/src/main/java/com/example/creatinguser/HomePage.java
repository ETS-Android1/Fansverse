package com.example.creatinguser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.creatinguser.activities.FanPageActivity;
import com.example.creatinguser.activities.ProfileActivity;

public class HomePage extends AppCompatActivity {
    //
    private TextView Title;
    private Button map;
    private Button liveScores;
    private Button stats;
    private Button myProfile;
    Button btnNewsfeed;
    Button btnmessages;

    private Button logout_button;
    CardView cvMessage, cvMap, cvScore, cvFanPage, cvProfilePage, cvNews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);


        //logout_button = findViewById(R.id.logout_button);
        cvMessage = findViewById(R.id.cvMessage);
        cvMap = findViewById(R.id.cvMap);
        cvScore = findViewById(R.id.cvScore);
        cvNews = findViewById(R.id.cvNews);
        cvFanPage = findViewById(R.id.cvFan);
        cvProfilePage = findViewById(R.id.cvProfile);


        cvFanPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), FanPageActivity.class);
                //intent.putExtra("message", current_username);
                startActivity(intent);
            }
        });

        cvProfilePage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                //intent.putExtra("message", current_username);
                startActivity(intent);
            }
        });

        cvNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Newsfeed.class);
                //intent.putExtra("message", current_username);
                startActivity(intent);
            }
        });

        cvMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), GoogleMaps.class);
                //intent.putExtra("message", current_username);
                startActivity(intent);
            }
        });

        cvMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                //intent.putExtra("message", current_username);
                startActivity(intent);
            }
        });

//        logout_button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                FirebaseAuth.getInstance().getCurrentUser();
//                FirebaseAuth.getInstance().signOut();
//                Toast toast = Toast.makeText(HomePage.this, "Signout Complete", Toast.LENGTH_LONG);
//                toast.show();
//                Intent intent = new Intent(getApplicationContext(), LoginPage.class);
//                startActivity(intent);
//            }
//        });

        

    }
}


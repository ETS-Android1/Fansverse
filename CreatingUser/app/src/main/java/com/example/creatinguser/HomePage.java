package com.example.creatinguser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class HomePage extends AppCompatActivity {

    private TextView Title;
    private Button map;
    private Button liveScores;
    private Button stats;
    private Button myProfile;
    Button btnNewsfeed;
    private Button logout_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        Title = findViewById(R.id.homePageFansverse);
        map = findViewById(R.id.buttonMap);
        liveScores = findViewById(R.id.buttonLScores);
        stats = findViewById(R.id.buttonStats);
        myProfile = findViewById(R.id.buttonProfile);
        btnNewsfeed = findViewById(R.id.btnNewsfeed);
        logout_button = findViewById(R.id.logout_button);

        btnNewsfeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Newsfeed.class);
                //intent.putExtra("message", current_username);
                startActivity(intent);
            }
        });

        logout_button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FirebaseAuth.getInstance().getCurrentUser();
                FirebaseAuth.getInstance().signOut();
                Toast toast = Toast.makeText(HomePage.this, "Signout Complete", Toast.LENGTH_LONG);
                toast.show();
                Intent intent = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(intent);
            }
        });

    }
}

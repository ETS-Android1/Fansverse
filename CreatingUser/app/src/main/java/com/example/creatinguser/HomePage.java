package com.example.creatinguser;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.creatinguser.utilities.Constants;
import com.example.creatinguser.utilities.PreferenceManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class HomePage extends AppCompatActivity {

    private TextView Title;
    private Button map;
    private Button liveScores;
    private Button stats;
    private Button myProfile;
    Button btnNewsfeed;
    Button btnmessages;
    BottomNavigationView bottomNavigationView;
    PreferenceManager preferenceManager;

    private Button logout_button;
    CardView cvMessage, cvMap, cvScore, cvStats, cvProfile, cvNews;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);


        //logout_button = findViewById(R.id.logout_button);
        cvMessage = findViewById(R.id.cvMessage);
        cvMap = findViewById(R.id.cvMap);
        cvScore = findViewById(R.id.cvScore);
        cvNews = findViewById(R.id.cvNews);

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

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // set home
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.logout:
                        startActivity(new Intent(getApplicationContext(), LoginPage.class));
                        overridePendingTransition(0, 0);
                        return true;


//                        FirebaseAuth.getInstance().getCurrentUser();
//                        FirebaseAuth.getInstance().signOut();
//                        Toast toast = Toast.makeText(HomePage.this, "Signout Complete", Toast.LENGTH_LONG);
//                        toast.show();
//                        Intent intent = new Intent(getApplicationContext(), LoginPage.class);
//                        startActivity(intent);
//                        overridePendingTransition(0, 0);
//                        return true;


//                        showToast("Signing out.....");
//                        FirebaseFirestore database = FirebaseFirestore.getInstance();
//                        DocumentReference documentReference =
//                                database.collection(Constants.KEY_COLLECTION_USERS).document(
//                                        preferenceManager.getString(Constants.KEY_USER_ID)
//                                );
//                        HashMap<String,Object> updates = new HashMap<>();
//                        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
//                        documentReference.update(updates)
//                                .addOnSuccessListener(unused -> {
//                                    preferenceManager.clear();
//                                    startActivity(new Intent(getApplicationContext(), LoginPage.class));
//                                    finish();
//                                })
//                               .addOnFailureListener(e -> showToast("Unable to sign out"));

                    case R.id.home:
                        return true;

                    // right now it directs to news and it works
                    case R.id.info:
                        startActivity(new Intent(getApplicationContext(), Newsfeed.class));
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
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


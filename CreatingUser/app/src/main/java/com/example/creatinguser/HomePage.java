package com.example.creatinguser;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class HomePage extends AppCompatActivity {

    private TextView Title;
    private Button map;
    private Button liveScores;
    private Button stats;
    private Button myProfile;
    FirebaseAuth firebaseAuth;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Title = findViewById(R.id.homePageFansverse);
        map = findViewById(R.id.buttonMap);
        liveScores = findViewById(R.id.buttonLScores);
        stats = findViewById(R.id.buttonStats);
        myProfile = findViewById(R.id.buttonProfile);
        toolbar = findViewById(R.id.home_toolbar);
        toolbar.setTitle("Homepage");


        myProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(HomePage.this, ProfileActivity.class);
                loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(loginIntent);
            }
        });

        toolbar.inflateMenu(R.menu.main_menu);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.logout) {
                    firebaseAuth.signOut();
                    Intent loginIntent = new Intent(HomePage.this, LoginPage.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                    finish();
                }


                return false;
            }
        });


        firebaseAuth = FirebaseAuth.getInstance();

        loadAndCheckUser();
        retrieveUserProfile();

    }

    public void loadAndCheckUser(){
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if(firebaseUser == null){
            Intent loginIntent = new Intent(HomePage.this, LoginPage.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(loginIntent);
            finish();
        }
    }

    private void retrieveUserProfile(){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentUserID = firebaseAuth.getCurrentUser().getUid().toString();
        final DocumentReference docRef = db.collection("Profile").document(currentUserID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if(documentSnapshot.exists()){
                    if(documentSnapshot.getData().get("firstname") == null){
                        Intent loginIntent = new Intent(HomePage.this, SetUpProfileActivity.class);
                        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(loginIntent);
                        finish();
                    }
                }
            }
        });
    }
}
package com.example.creatinguser.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.creatinguser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ReviewActivity extends AppCompatActivity {
    EditText reviewText;
    Button addReview;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    String key, currentDate, currentTime;
    String currentUserID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        getSupportActionBar().setTitle("Add Review");

        firebaseAuth = FirebaseAuth.getInstance();

        reviewText = findViewById(R.id.add_review_edit_text);
        addReview = findViewById(R.id.add_review_button);


        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        currentUserID = firebaseAuth.getCurrentUser().getUid().toString();

        progressDialog = new ProgressDialog(this);

        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addBarReview();
            }
        });



        Intent intent = getIntent();
        key = intent.getStringExtra("KEY");
    }

    public void addBarReview(){
        String revText = reviewText.getText().toString();

        if(revText.isEmpty()){
            Toast.makeText(getApplicationContext(), "Please enter a review", Toast.LENGTH_LONG).show();
        }else{
            progressDialog.setTitle("Adding your review...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            Map<String, Object> map = new HashMap<>();
            map.put("review", revText);
            map.put("userId", currentUserID);
            map.put("bar_id", key);
            map.put("currentDate", currentDate);
            map.put("currentTime",currentTime);

            db.collection("BarReviews").add(map).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Review Added Successful", Toast.LENGTH_LONG).show();
                        Intent mainIntent = new Intent(getApplicationContext(), BarPageActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(mainIntent);
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Error adding the review", Toast.LENGTH_LONG).show();
                    }
                }
            });


        }
    }
}
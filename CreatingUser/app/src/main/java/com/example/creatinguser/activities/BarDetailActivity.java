package com.example.creatinguser.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.creatinguser.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class BarDetailActivity extends AppCompatActivity {
    String key;
    TextView textTitle, textDes, textTime;
    String currentUserID, chat_image_url, userfromDb, cur_user_id, current_time, current_date;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    Button checkin, review;
    boolean checker;
    String doc_key, chkintime, chkindate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_detail);

        getSupportActionBar().setTitle("Bar Page Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        progressDialog = new ProgressDialog(this);

        textTitle = findViewById(R.id.detail_bar_page_title);
        textDes = findViewById(R.id.detail_bar_page_description);
        checkin = findViewById(R.id.detail_bar_page_checkin);
        textTime = findViewById(R.id.detail_bar_page_time);
        review = findViewById(R.id.detail_bar_page_review);

        firebaseAuth = FirebaseAuth.getInstance();

        current_date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        current_time = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());


        Intent intent = getIntent();
        key = intent.getStringExtra("KEY");

        checkin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkIn();
            }
        });

        review.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ReviewActivity.class);
                intent.putExtra("KEY", key);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        getCurrentUserDets();
        retrieveBar();
        retrievePage();

    }

    private void getCurrentUserDets(){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        cur_user_id = firebaseAuth.getCurrentUser().getUid().toString();
        final DocumentReference docRef = db.collection("Profile").document(cur_user_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                DocumentSnapshot documentSnapshot = task.getResult();
                if(documentSnapshot.exists()){
                    userfromDb = documentSnapshot.getData().get("username").toString();
                    if(documentSnapshot.getData().get("imageUrl") != null){
                        chat_image_url = documentSnapshot.getData().get("imageUrl").toString();
                    }else{
                        chat_image_url = "https://firebasestorage.googleapis.com/v0/b/fanverse-7e61e.appspot.com/o/ProfileImages%2Fprofile.png?alt=media&token=dd60c0a2-b023-4f93-805f-0574a5ab6dd4";
                    }
                }else{
                    userfromDb = "Unknown";
                }

            }
        });
    }

    public void checkIn(){
       if(!checker){
           progressDialog.setTitle("Checking You In");
           progressDialog.setMessage("Please wait...");
           progressDialog.setCanceledOnTouchOutside(false);
           progressDialog.show();
           final FirebaseFirestore db = FirebaseFirestore.getInstance();
           Map<String, Object> data = new HashMap<>();
           data.put("userID", currentUserID);
           data.put("userImage", chat_image_url);
           data.put("username", userfromDb);
           data.put("bar_id", key);
           data.put("checked_in", true);
           data.put("checkout_time", "not yet");
           data.put("checkout_date", "not yet");
           data.put("current_time", current_time);
           data.put("current_date", current_date);

           db.collection("Bar CheckIn").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
               @Override
               public void onComplete(@NonNull Task<DocumentReference> task) {
                   if(task.isSuccessful()){
                       progressDialog.dismiss();
                       Toast.makeText(getApplicationContext(), "You have checked in successfully", Toast.LENGTH_LONG).show();
                   }else{
                       progressDialog.dismiss();
                       Toast.makeText(getApplicationContext(), "There was an error checking you in", Toast.LENGTH_LONG).show();

                   }

               }
           });
       }else{
           progressDialog.setTitle("Checking You Out");
           progressDialog.setMessage("Please wait...");
           progressDialog.setCanceledOnTouchOutside(false);
           progressDialog.show();
           final FirebaseFirestore db = FirebaseFirestore.getInstance();
           Map<String, Object> data = new HashMap<>();
           data.put("userID", currentUserID);
           data.put("userImage", chat_image_url);
           data.put("username", userfromDb);
           data.put("bar_id", key);
           data.put("current_time", current_time);
           data.put("current_date", current_date);

           db.collection("Bar CheckOut").add(data).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
               @Override
               public void onComplete(@NonNull Task<DocumentReference> task) {
                   if(task.isSuccessful()){
                       final FirebaseFirestore db = FirebaseFirestore.getInstance();
                       Map<String, Object> map = new HashMap<>();
                       map.put("current_date", current_date);
                       map.put("current_time", current_time);
                       map.put("checked_in", false);

                       db.collection("Bar CheckIn").document(doc_key).update(map);
                       progressDialog.dismiss();
                       Toast.makeText(getApplicationContext(), "You have checked our successfully", Toast.LENGTH_LONG).show();
                   }else{
                       progressDialog.dismiss();
                       Toast.makeText(getApplicationContext(), "There was an error checking you out", Toast.LENGTH_LONG).show();

                   }

               }
           });

       }

    }

    public void retrievePage() {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid().toString();
        db.collection("Bar CheckIn")
                .whereEqualTo("bar_id", key)
                .whereEqualTo("userID", currentUserID)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        checker = false;
                        assert value != null;
                        if(value.size() > 0){
                            for(DocumentSnapshot snapshot: value.getDocuments()){
                                if(snapshot.exists()){
                                    doc_key = snapshot.getId().toString();
                                    if(snapshot.getData().get("checked_in").toString().equals("true")){
                                        checker = true;
                                        chkintime = snapshot.getData().get("current_time").toString();
                                        chkindate = snapshot.getData().get("current_date").toString();
                                        break;
                                    }
                                }
                            }

                            if(checker){
                                checkin.setText("Check Out");
                                textTime.setText("Last check in was "+chkindate+" at "+chkintime);

                            }else{
                                checkin.setText("Check In");
                            }
                        }

                    }
                });

    }

    public void retrieveBar(){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid().toString();
        final DocumentReference docRef = db.collection("BarPages").document(key);

        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value.exists()){
                    String title = value.getData().get("title").toString();
                    String desc = value.getData().get("description").toString();



                    textTitle.setText(title);
                    textDes.setText(desc);

                }

            }
        });


    }
}
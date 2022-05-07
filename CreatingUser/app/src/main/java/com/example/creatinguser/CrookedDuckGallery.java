package com.example.creatinguser;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class CrookedDuckGallery extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crooked_duck_gallery);

        String nameOfPic = getIntent().getStringExtra("FirstPic");
        String nameOfPic2 = getIntent().getStringExtra("SecondPic");

        // getting ImageView by its id
        ImageView rImage = findViewById(R.id.crookedDuck1);
        ImageView secondImage = findViewById(R.id.crookedDuck2);

        // we will get the default FirebaseDatabase instance
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        // we will get a DatabaseReference for the database root node
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        // Here "image" is the child node value we are getting
        // child node data in the getImage variable
        DatabaseReference getImage = databaseReference.child(nameOfPic);
        DatabaseReference getImage2 = databaseReference.child(nameOfPic2);


        // Adding listener for a single change
        // in the data at this location.
        // this listener will triggered once
        // with the value of the data at the location
        getImage.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // getting a DataSnapshot for the location at the specified
                // relative path and getting in the link variable
                String link = dataSnapshot.getValue(String.class);

                // loading that data into rImage
                // variable which is ImageView
                Picasso.get().load(link).into(rImage);
                //Picasso.get().load(link).into(secondImage);
            }



            // this will called when any problem
            // occurs in getting data
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // we are showing that error message in toast
                Toast.makeText(CrookedDuckGallery.this, "Error Loading Image", Toast.LENGTH_SHORT).show();
            }
        });

        getImage2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // getting a DataSnapshot for the location at the specified
                // relative path and getting in the link variable
                String link2 = dataSnapshot.getValue(String.class);

                // loading that data into rImage
                // variable which is ImageView
                //Picasso.get().load(link).into(rImage);
                Picasso.get().load(link2).into(secondImage);
            }



            // this will called when any problem
            // occurs in getting data
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // we are showing that error message in toast
                Toast.makeText(CrookedDuckGallery.this, "Error Loading Image", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
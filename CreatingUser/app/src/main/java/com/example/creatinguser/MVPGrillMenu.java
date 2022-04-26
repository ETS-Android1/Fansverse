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

public class MVPGrillMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crooked_duck_menu);

        String nameOfPicture = getIntent().getStringExtra("NameOfPic");
        //String idOfPic = getIntent().getStringExtra("PicID");

        //Toast.makeText(MenuDisplay.this, nameOfPicture, Toast.LENGTH_SHORT).show();
        //Toast.makeText(MenuDisplay.this, idOfPic, Toast.LENGTH_SHORT).show();
        //System.out.println("\n \n  Name of the picture: " + nameOfPicture);
        //System.out.println("ID of imagiview: " + idOfPic);


        //int resID = getResources().getIdentifier(idOfPic, "id", getPackageName());


        //need help with!

        //int resID = getResources().getIdentifier(idOfPic, "drawable", getPackageName());

        //System.out.println("\n \n This is the resId:"+ resID);
        // getting ImageView by its id
        ImageView viewMenuPic = findViewById(R.id.mvpGrillMenuPic);
        //ImageView interludeMenuPic = findViewById(resID);

        // we will get the default FirebaseDatabase instance
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        // we will get a DatabaseReference for the database root node
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        // Here "image" is the child node value we are getting
        // child node data in the getImage variable
        DatabaseReference getImage = databaseReference.child(nameOfPicture);



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
                Picasso.get().load(link).into(viewMenuPic);
            }

            // this will called when any problem
            // occurs in getting data
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // we are showing that error message in toast
                Toast.makeText(MVPGrillMenu.this, "Error Loading Image", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
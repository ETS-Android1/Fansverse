package com.example.creatinguser.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.creatinguser.Models.Fans;
import com.example.creatinguser.Models.Post;
import com.example.creatinguser.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.SetOptions;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class FanMembers extends AppCompatActivity {

    private RecyclerView recyclerView;
    Query query;
    FirebaseFirestore db;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fan_members);
        getSupportActionBar().setTitle("Members");


        recyclerView = findViewById(R.id.fansRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(getApplicationContext()).getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        key = intent.getStringExtra("PAGE_ID");

        CollectionReference pageRef= db.collection("Page Members");
        query =  pageRef.whereEqualTo("page_id", key);

    }

    @Override
    protected void onStart() {
        super.onStart();
        FirestoreRecyclerOptions<Fans> options = new FirestoreRecyclerOptions.Builder<Fans>()
                .setQuery(query, Fans.class)
                .build();

        FirestoreRecyclerAdapter<Fans, FanViewHolder> firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<Fans, FanViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull FanViewHolder holder, int position, @NonNull @NotNull Fans model) {
                final String  key = getSnapshots().getSnapshot(position).getId();
                holder.setUsername(model.getUsername());
                holder.setImage(model.getUserImage());
            }

            @NonNull
            @Override
            public FanViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.single_fan_members, viewGroup, false);
                return new FanViewHolder(view);
            }
        };

        firestoreRecyclerAdapter.startListening();

        recyclerView.setAdapter(firestoreRecyclerAdapter);


    }

    private static class FanViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public FanViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }


        public void setUsername(String name){
            TextView username = mView.findViewById(R.id.fan_username);
            username.setText(name);
        }

        public void setImage(String image){
            CircleImageView circleImageView = mView.findViewById(R.id.fan_image);
            Picasso.get().load(image).into(circleImageView);
        }

    }
}
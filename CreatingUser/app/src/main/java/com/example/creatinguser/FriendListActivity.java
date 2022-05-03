package com.example.creatinguser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.creatinguser.Models.User;
import com.example.creatinguser.activities.ChatActivity;
import com.example.creatinguser.activities.GroupChatActivity;
import com.example.creatinguser.adapters.UsersAdapter;
import com.example.creatinguser.databinding.ActivityFriendListBinding;
import com.example.creatinguser.listeners.UserListener;
import com.example.creatinguser.utilities.Constants;
import com.example.creatinguser.utilities.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendListActivity extends AppCompatActivity implements UserListener {

    private ActivityFriendListBinding binding;
    private PreferenceManager preferenceManager;
    boolean found;
    List<DocumentSnapshot> friendsLists;
    BottomNavigationView bottomNavigationView;
    FirebaseFirestore db;
    String currentUser,userBeingAdded;
    ArrayList<String> listOfFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFriendListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        currentUser = intent.getStringExtra(Constants.KEY_USER_ID);
        userBeingAdded = intent.getStringExtra("Friend");
        getUsers();
        bottomNavigationView = findViewById(R.id.bottom_navigation);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.logout:
                        Toast toast = Toast.makeText(getApplicationContext(),"Signing out...",Toast.LENGTH_LONG);
                        toast.show();
                        FirebaseFirestore database = FirebaseFirestore.getInstance();
                        DocumentReference documentReference = database.collection(Constants.KEY_COLLECTION_USERS).document(preferenceManager.getString(Constants.KEY_USER_ID));
                        HashMap<String,Object> updates = new HashMap<>();
                        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
                        documentReference.update(updates)
                                .addOnSuccessListener(unused -> {
                                    preferenceManager.clear();
                                    startActivity(new Intent(getApplicationContext(), LoginPage.class));
                                    finish();
                                });
                        return true;
                    case R.id.sportsTeams:
                        startActivity(new Intent(getApplicationContext(), SportsTeamsMainPage.class));
                        overridePendingTransition(0, 0);
                        return true;

                    // right now it directs to news and it works
                    case R.id.home:
                        Intent intent1 = new Intent(getApplicationContext(), HomePage.class);
                        intent1.putExtra(Constants.KEY_USER_ID,currentUser);
                        overridePendingTransition(0, 0);
                        startActivity(intent1);
                        return true;
                }
                return false;
            }
        });
    }

    private void getUsers(){
        System.out.println("\n\n in getUsers function");
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection("Friends Lists").document(currentUser).collection("Friends").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                friendsLists = task.getResult().getDocuments();
                for (DocumentSnapshot data: friendsLists
                     ) {
                    System.out.println(data.getData().get("userId"));
//                    listOfFriends.add(data.getData().get("userId").toString());
                }
            }
        });
        database.collection(Constants.KEY_COLLECTION_USERS)
            .get()
            .addOnCompleteListener(task -> {
                loading(false);
                String currentUserId = preferenceManager.getString(Constants.KEY_USER_ID);
                if(task.isSuccessful() && task.getResult() != null){
                    List<User> users = new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                        if (currentUserId.equals(queryDocumentSnapshot.getId())){
                            continue;
                        }
//                      else if (!friendsLists.isEmpty()){
//                            for (DocumentSnapshot data:friendsLists
//                                 ) {
//                                System.out.println(data.getData().get("userId"));
//                                System.out.println(queryDocumentSnapshot.getString(Constants.KEY_NAME));
//                                if (data.getData().get("userId").equals(queryDocumentSnapshot.getString(Constants.KEY_NAME))){
//                                    continue;
//                                }
//                            }
//                        }
                        User user = new User();
                        user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                        user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                        user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
                        user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                        user.id = queryDocumentSnapshot.getId();
                        users.add(user);
                    }
                    if (users.size() > 0){
                        UsersAdapter usersAdapter = new UsersAdapter(users,this);
                        binding.usersRecyclerView.setAdapter(usersAdapter);
                        binding.usersRecyclerView.setVisibility(View.VISIBLE);
                    }else {
                        showErrorMessage();
                    }
                }else{
                    showErrorMessage();
                }
            });
}

    private void showErrorMessage(){
        binding.textErrorMessage.setText(String.format("%s","No user available"));
        binding.textErrorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading){
        if (isLoading){
            binding.progressBar.setVisibility(View.VISIBLE);
        }else{
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onUserClicked(User user) {
        System.out.println("\n \n Clicking on user"+user+"\n \n id"+user.id+"\n \n name"+user.name);
        found=false;
        db.collection("Friends Lists")
                .document(currentUser)
                .collection("Friends")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            System.out.println("\n\nTASK.ISSUCCESSFUL");
                            System.out.println(task.getResult().toString());
                            System.out.println(task.getResult().getDocuments());
                            List<DocumentSnapshot> docs = task.getResult().getDocuments();
                            for (DocumentSnapshot data: docs
                                 ) {
                                System.out.println(data);
                                System.out.println(data.getData().get("userId"));
                            }
                            if (task.getResult().isEmpty()) {
                                System.out.println("\n\nIs Empty");
                                Map<String, Object> map = new HashMap<>();
//                                user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
//                                user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
//                                user.image = queryDocumentSnapshot.getString(Constants.KEY_IMAGE);
//                                user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
//                                user.id = queryDocumentSnapshot.getId();
                                map.put(Constants.KEY_USER_ID, user.id);
                                map.put(Constants.KEY_NAME,user.name);
                                map.put(Constants.KEY_EMAIL,user.email);
                                map.put(Constants.KEY_IMAGE,user.image);
                                map.put(Constants.KEY_FCM_TOKEN,user.token);
                                DocumentReference ref = db.collection("Friends Lists").document(currentUser);
                                ref.collection("Friends").add(map);
                                Intent mainIntent = new Intent(getApplicationContext(), FriendListActivity.class);
                                mainIntent.putExtra(Constants.KEY_USER_ID, currentUser);
                                mainIntent.putExtra("Friend", user.id);
                                startActivity(mainIntent);
                                finish();
                            }else{
                                System.out.println("\n\n Else");
                                for (DocumentSnapshot data: docs
                                ) {
                                    System.out.println(data);
                                    System.out.println(data.getData().get("userId"));
                                    if (data.getData().get("userId").equals(user.id)){
                                        found=true;
                                        break;
                                    }
                                }
                                if (found){
                                    System.out.println("\\nFriend is already in your friends lists");
                                    Intent mainIntent = new Intent(getApplicationContext(),FriendListActivity.class);
                                    mainIntent.putExtra(Constants.KEY_USER_ID,currentUser);
                                    mainIntent.putExtra("Friend",user.id);
                                    startActivity(mainIntent);
                                    finish();
                                }else{
                                    Map<String, Object> map = new HashMap<>();
                                    map.put(Constants.KEY_USER_ID, user.id);
                                    map.put(Constants.KEY_NAME,user.name);
                                    map.put(Constants.KEY_EMAIL,user.email);
                                    map.put(Constants.KEY_IMAGE,user.image);
                                    map.put(Constants.KEY_FCM_TOKEN,user.token);
                                    DocumentReference ref = db.collection("Friends Lists").document(currentUser);
                                    ref.collection("Friends").add(map);
                                    Intent mainIntent = new Intent(getApplicationContext(), FriendListActivity.class);
                                    mainIntent.putExtra(Constants.KEY_USER_ID, currentUser);
                                    mainIntent.putExtra("Friend", user.id);
                                    startActivity(mainIntent);
                                    finish();

                                }
                            }
                        }
                    }
                });
    }
}
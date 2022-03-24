package com.example.creatinguser.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.creatinguser.Models.GroupChatMessage;
import com.example.creatinguser.R;
import com.example.creatinguser.utilities.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateGroupChatActivity extends AppCompatActivity {
    EditText titleET;
    Button createBtn;
    private ProgressDialog progressDialog;
    private String currentUserID;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group_chat);

        getSupportActionBar().setTitle("Create Group Chat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        progressDialog = new ProgressDialog(this);
        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid().toString();
        initializeViews();



        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createGroupChat();
            }
        });
    }

    private void initializeViews(){
        titleET = findViewById(R.id.name_group_chat);
        createBtn = findViewById(R.id.create_group_chat_button);
    }

    private void createGroupChat(){
        String title = titleET.getText().toString();

        if(TextUtils.isEmpty(title)){
            titleET.setError("PLease enter a Name for your group chat");
            return;
        }

        progressDialog.setTitle("Creating your Group Chat");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        Map<String, Object> map = new HashMap<>();
        map.put(Constants.KEY_GROUP_CHAT_NAME, title);
        map.put(Constants.KEY_USER_ID, currentUserID);


        DocumentReference ref = db.collection(Constants.KEY_COLLECTION_GROUPCHAT).document(currentUserID);
        ref.collection("GroupMessage").add(map);
        Intent mainIntent = new Intent(getApplicationContext(),GroupChatActivity.class);
        mainIntent.putExtra("KEY",title);
        startActivity(mainIntent);

//                .add(map)
//                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                    @Override
//                    public void onSuccess(DocumentReference documentReference) {
//                        progressDialog.dismiss();
//                        Toast toast = Toast.makeText(getApplicationContext(), "Group Chat Created successfully", Toast.LENGTH_LONG);
//                        toast.show();
//                        Intent mainIntent = new Intent(getApplicationContext(), GroupChatActivity.class);
//                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(mainIntent);
//                        finish();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                String message = e.getMessage();
//                Toast toast = Toast.makeText(getApplicationContext(),"Error: "+ message, Toast.LENGTH_LONG);
//                toast.show();
//                progressDialog.dismiss();
//            }
//        });

    }
}

package com.example.creatinguser;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creatinguser.Models.ChatMessage;
import com.example.creatinguser.Models.FanPage;
import com.example.creatinguser.Models.GroupChatMessage;
import com.example.creatinguser.Models.User;
import com.example.creatinguser.activities.ChatActivity;
import com.example.creatinguser.activities.CreateFanPageActivity;
import com.example.creatinguser.activities.CreateGroupChatActivity;
import com.example.creatinguser.activities.GroupChatActivity;
import com.example.creatinguser.activities.GroupUsersActivity;
import com.example.creatinguser.activities.UsersActivity;
import com.example.creatinguser.adapters.RecentConversationsAdapter;
import com.example.creatinguser.databinding.ActivityDirectmessageScreenBinding;
import com.example.creatinguser.databinding.ActivityGroupchatBinding;
import com.example.creatinguser.databinding.ActivityGroupmessageScreenBinding;
import com.example.creatinguser.listeners.ConversationListener;
import com.example.creatinguser.utilities.Constants;
import com.example.creatinguser.utilities.PreferenceManager;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
//
public class GroupMessageScreen extends AppCompatActivity implements ConversationListener {
    private RecyclerView recyclerView;
    Query query;
    FirebaseFirestore db;
    FloatingActionButton switchToDirectMessage, createGroupMessage;
    String currentUserID;
    FirebaseAuth firebaseAuth;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupmessage_screen);

        switchToDirectMessage = findViewById(R.id.switchToDirectMessage);
        createGroupMessage = findViewById(R.id.fabNewGroupChat);

        getSupportActionBar().setTitle("Group Messages");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid().toString();

        createGroupMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainIntent = new Intent(com.example.creatinguser.GroupMessageScreen.this, CreateGroupChatActivity.class);
                startActivity(mainIntent);

            }
        });

        switchToDirectMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mainIntent = new Intent(GroupMessageScreen.this,DirectMessageScreen.class);
                startActivity(mainIntent);
            }
        });

        recyclerView = findViewById(R.id.conversationsRecyclerViewforGroupChat);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                new LinearLayoutManager(getApplicationContext()).getOrientation());
        recyclerView.addItemDecoration(mDividerItemDecoration);

        db = FirebaseFirestore.getInstance();

        query = db.collection(Constants.KEY_COLLECTION_GROUPCHAT).document(currentUserID).collection("GroupMessage");
//        Intent intent = getIntent();
//        key = intent.getStringExtra("KEY");
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirestoreRecyclerOptions<GroupChatMessage> options = new FirestoreRecyclerOptions.Builder<GroupChatMessage>()
                .setQuery(query, GroupChatMessage.class)
                .build();

        FirestoreRecyclerAdapter<GroupChatMessage, GroupMessageScreen.FanViewHolder> firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<GroupChatMessage, GroupMessageScreen.FanViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull FanViewHolder holder, int position, @NonNull @NotNull GroupChatMessage model) {
//                final String key = getSnapshots().getSnapshot(position).getId();
                final String key = getSnapshots().getSnapshot(position).getString(Constants.KEY_GROUP_CHAT_NAME);
                holder.setTitle(key);
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getApplicationContext(), GroupChatActivity.class);
                        intent.putExtra("KEY", key);
                        startActivity(intent);
                    }
                });
            }
            @NonNull
            @Override
            public GroupMessageScreen.FanViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.single_fan_page, viewGroup, false);
                return new GroupMessageScreen.FanViewHolder(view);
            }
        };
        firestoreRecyclerAdapter.startListening();
        recyclerView.setAdapter(firestoreRecyclerAdapter);
    }
    private static class FanViewHolder extends RecyclerView.ViewHolder {
        View mView;
        public FanViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setTitle(String title) {
            TextView titleTV = mView.findViewById(R.id.single_page_title);
            titleTV.setText(title);
        }
    }


//
//    private ActivityGroupmessageScreenBinding binding;
//    private PreferenceManager preferenceManager;
//    private List<GroupChatMessage> conversations;
//    private RecentConversationsAdapter conversationsAdapter;
//    private FirebaseFirestore database;
//    private RecyclerView recyclerView;
//    FirebaseAuth firebaseAuth;
//    String key;
//    Query query;
//    String currentUserID;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityGroupmessageScreenBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        preferenceManager = new PreferenceManager(getApplicationContext());
//        firebaseAuth = FirebaseAuth.getInstance();
//
//        Intent intent = getIntent();
//        key = intent.getStringExtra("KEY");
//
//        init();
////        loadUserDetails();
//        setListeners();
////        listenConversations();
////        retrievePage();
//
//        recyclerView = findViewById(R.id.fan_page_recyclerview);
////        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
//                new LinearLayoutManager(getApplicationContext()).getOrientation());
//        recyclerView.addItemDecoration(mDividerItemDecoration);
//
//        query = database.collection(Constants.KEY_COLLECTION_GROUPCHAT);
//    }
//
////    @Override
////    protected void onStart() {
////        super.onStart();
////        FirestoreRecyclerOptions<GroupChatMessage> options = new FirestoreRecyclerOptions.Builder<GroupChatMessage>()
////                .setQuery(query, GroupChatMessage.class)
////                .build();
////
////        FirestoreRecyclerAdapter<GroupChatMessage, GroupChatActivity.Messages> firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<FanPage, FanPageActivity.FanViewHolder>(options) {
////            @Override
////            protected void onBindViewHolder(@NonNull @NotNull FanPageActivity.FanViewHolder holder, int position, @NonNull @NotNull FanPage model) {
////                final String  key = getSnapshots().getSnapshot(position).getId();
////
////                holder.setTitle(model.getTitle());
////                holder.setTotalMembers(model.getTotal_members());
////
////
////
////
////                holder.mView.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        Intent intent = new Intent(getApplicationContext(), DetailPages.class);
////                        intent.putExtra("KEY", key);
////                        startActivity(intent);
////                    }
////                });
////
////
////
////
////            }
////
////
////
////            @NonNull
////            @Override
////            public FanPageActivity.FanViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
////                View view = LayoutInflater.from(viewGroup.getContext())
////                        .inflate(R.layout.single_fan_page, viewGroup, false);
////                return new FanPageActivity.FanViewHolder(view);
////            }
////        };
////
////        firestoreRecyclerAdapter.startListening();
////
////        recyclerView.setAdapter(firestoreRecyclerAdapter);
////
////
////    }
////
////    private static class FanViewHolder extends RecyclerView.ViewHolder {
////        View mView;
////        FirebaseAuth firebaseAuth;
////        String currentUser;
////
////        public FanViewHolder(@NonNull View itemView) {
////            super(itemView);
////            mView = itemView;
////
////
////            firebaseAuth = FirebaseAuth.getInstance();
////
////            currentUser = firebaseAuth.getCurrentUser().getUid().toString();
////
////
////        }
////
////
////        public void setTitle(String title) {
////            TextView titleTV = mView.findViewById(R.id.single_page_title);
////            titleTV.setText(title);
////        }
////    }
//
//    private void init() {
//        conversations = new ArrayList<>();
////        conversationsAdapter = new RecentConversationsAdapter(conversations, this);
//        binding.conversationsRecyclerViewforGroupChat.setAdapter(conversationsAdapter);
//        database = FirebaseFirestore.getInstance();
//    }
//
//    private void setListeners() {
//        binding.switchToDirectMessage.setOnClickListener(v ->
//                startActivity(new Intent(getApplicationContext(), DirectMessageScreen.class)));
//        binding.fabNewGroupChat.setOnClickListener(v ->
//                startActivity(new Intent(getApplicationContext(), CreateGroupChatActivity.class)));
//    }
//
////    private void loadUserDetails(){
////        binding.textName.setText(preferenceManager.getString(Constants.KEY_NAME));
////        byte[] bytes = Base64.decode(preferenceManager.getString(Constants.KEY_IMAGE),Base64.DEFAULT);
////        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
////        binding.imageProfile.setImageBitmap(bitmap);
////    }
//
//    private void showToast(String message) {
//        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//    }
//
////    private void listenConversations() {
////        database.collection(Constants.KEY_COLLECTION_GROUPCHAT)
////                .whereEqualTo(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
////                .addSnapshotListener(eventListener);
////        currentUserID = firebaseAuth.getCurrentUser().getUid().toString();
////        database.collection(Constants.KEY_COLLECTION_GROUPCHAT)
////                .whereEqualTo(Constants.KEY_USER_ID, currentUserID)
////                .
////        database.collection(Constants.KEY_COLLECTION_GROUPCHAT)
////                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
////                .addSnapshotListener(eventListener);
////    }
////    public void retrievePage() {
////        final FirebaseFirestore db = FirebaseFirestore.getInstance();
////        currentUserID = firebaseAuth.getCurrentUser().getUid().toString();
////        final DocumentReference docRef = db.collection(Constants.KEY_COLLECTION_GROUPCHAT).document();
////
////        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
////            @Override
////            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
////                if (value.exists()) {
////                    String user = value.getData().get("userID").toString();
////
////                    if (user.equals(currentUserID)) {
////                        database.collection(Constants.KEY_COLLECTION_GROUPCHAT)
////                                .whereEqualTo(Constants.KEY_USER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
////                                .addSnapshotListener(eventListener);
////                    }
////                }
////
////            }
////        });
////    }
//
////    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
////        if (error != null) {
////            return;
////        }
////        if (value != null) {
////            for (DocumentChange documentChange : value.getDocumentChanges()) {
////                if (documentChange.getType() == DocumentChange.Type.ADDED) {
////                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
////                    String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
////                    ChatMessage chatMessage = new ChatMessage();
////                    chatMessage.senderId = senderId;
////                    chatMessage.receiverId = receiverId;
////                    if (preferenceManager.getString(Constants.KEY_USER_ID).equals(senderId)) {
//////                        chatMessage.conversionImage = documentChange.getDocument().getString(Constants.KEY_RECEIVER_IMAGE);
////                        chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
////                        chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
////                    } else {
//////                        chatMessage.conversionImage = documentChange.getDocument().getString(Constants.KEY_SENDER_IMAGE);
////                        chatMessage.conversionName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
////                        chatMessage.conversionId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
////                    }
////                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
////                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
////                    conversations.add(chatMessage);
////                }
//////                else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
////                    for (int i = 0; i < conversations.size(); i++) {
////                        String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
////                        String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
////                        if (conversations.get(i).senderId.equals(senderId) && conversations.get(i).receiverId.equals(receiverId)) {
////                            conversations.get(i).message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
////                            conversations.get(i).dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
////                            break;
////                        }
////                    }
////                }
////            }
////            Collections.sort(conversations, (obj1, obj2) -> obj2.dateObject.compareTo(obj1.dateObject));
////            conversationsAdapter.notifyDataSetChanged();
////            binding.conversationsRecyclerViewforGroupChat.smoothScrollToPosition(0);
////            binding.conversationsRecyclerViewforGroupChat.setVisibility(View.VISIBLE);
////            binding.progressBar.setVisibility(View.GONE);
////        }
////    };
//
    @Override
    public void onConversionClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), GroupChatActivity.class);
        intent.putExtra(Constants.KEY_USER,user);
        startActivity(intent);
    }
}

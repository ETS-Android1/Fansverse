package com.example.creatinguser.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.creatinguser.HomePage;
import com.example.creatinguser.LoginPage;
import com.example.creatinguser.Models.GroupChatMessage;
import com.example.creatinguser.R;
import com.example.creatinguser.utilities.Constants;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupChatActivity extends AppCompatActivity {//} implements ConversationListener {
    private EditText userMessage;
    private ImageButton sendMessageBtn;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private String currentUserID;
    BottomNavigationView bottomNavigationView;
    FirebaseAuth firebaseAuth;
    String userfromDb;
    String chat_image_url;
    String key;
    Query query;
    FirebaseFirestore db;
    String currentDate;
    String currentTime;

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groupchat);




        getSupportActionBar().setTitle("GroupChat");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        currentTime = new SimpleDateFormat("HH:mm aa", Locale.getDefault()).format(new Date());

        userMessage = findViewById(R.id.group_activity_message_input);
        sendMessageBtn = findViewById(R.id.group_activity_send_button);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUserID = firebaseAuth.getCurrentUser().getUid().toString();

        Intent intent = getIntent();
        key = intent.getStringExtra("KEY");

        db = FirebaseFirestore.getInstance();

        CollectionReference chatsRef= db.collection("GroupChatMessageCont");
        query =  chatsRef.whereEqualTo(Constants.KEY_GROUP_CHAT_NAME, key).orderBy("timestamp", Query.Direction.ASCENDING);


        bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
       @Override
       public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
           startActivity(new Intent(getApplicationContext(), HomePage.class));
           return true;
       }
   });
        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });


        recyclerView = findViewById(R.id.group_activity_recyclerView);
//        recyclerView.hasFixedSize();
        linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        userMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString().trim().length()==0){
                    sendMessageBtn.setEnabled(false);
                    sendMessageBtn.setVisibility(View.GONE);

                } else {
                    sendMessageBtn.setEnabled(true);
                    sendMessageBtn.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        FirestoreRecyclerOptions<GroupChatMessage> options = new FirestoreRecyclerOptions.Builder<GroupChatMessage>()
                .setQuery(query, GroupChatMessage.class)
                .build();

        FirestoreRecyclerAdapter<GroupChatMessage, com.example.creatinguser.activities.GroupChatActivity.MessagesViewHolder> firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<GroupChatMessage, com.example.creatinguser.activities.GroupChatActivity.MessagesViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull @NotNull com.example.creatinguser.activities.GroupChatActivity.MessagesViewHolder holder, int position, @NonNull @NotNull GroupChatMessage model) {
                final String  key = getSnapshots().getSnapshot(position).getId();

                holder.setMessage(model.getChat_message());
                holder.setMessageTime(model.getChat_time());
                holder.setSenderImage(model.getChat_image());
                holder.setSenderUsername(model.getChat_username());

            }

            @NonNull
            @Override
            public com.example.creatinguser.activities.GroupChatActivity.MessagesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view;
                if(i == MSG_TYPE_RIGHT){
                    view  = LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.message_layout_sender, viewGroup, false);
                }else{
                    view = LayoutInflater.from(viewGroup.getContext())
                            .inflate(R.layout.message_layout_receiver, viewGroup, false);
                }
                return new com.example.creatinguser.activities.GroupChatActivity.MessagesViewHolder(view);
            }

            @Override
            public int getItemViewType(int position) {
                if(getItem(position).getUser_id().equals(currentUserID)){
                    return MSG_TYPE_RIGHT;
                }else{
                    return  MSG_TYPE_LEFT;
                }
            }
        };

        firestoreRecyclerAdapter.startListening();

        firestoreRecyclerAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.scrollToPosition(firestoreRecyclerAdapter.getItemCount() - 1);
            }
        });
        recyclerView.setAdapter(firestoreRecyclerAdapter);
    }

    private static class MessagesViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public MessagesViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }

        private void setSenderImage(String image){
            CircleImageView circleImageView = mView.findViewById(R.id.message_sender_image);
            Picasso.get().load(image).into(circleImageView);
        }

        private void setMessage(String text){
            TextView senderText = mView.findViewById(R.id.message_sender_text);
            senderText.setText(text);
        }


        private void setMessageTime(String time){
            TextView senderTime = mView.findViewById(R.id.message_sender_time);
            senderTime.setText(time);
        }

        private void setSenderUsername(String name){
            TextView senderName = mView.findViewById(R.id.message_sender_username);
            senderName.setText(name);
        }


    }

    private void getCurrentUsername(){
        currentUserID = firebaseAuth.getCurrentUser().getUid().toString();
        final FirebaseFirestore db = FirebaseFirestore.getInstance();

        final DocumentReference docRef = db.collection("Profile").document(currentUserID);
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

    public void sendMessage(){
        String message = userMessage.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String currentUserId = firebaseAuth.getCurrentUser().getUid().toString();
        Map<String, Object> map = new HashMap<>();
        map.put("chat_image", chat_image_url);
        map.put("chat_message", message);
        map.put("chat_time", currentTime);
        map.put("chat_date", currentDate);
        map.put("chat_username",userfromDb);
        map.put(Constants.KEY_GROUP_CHAT_NAME,key);
        map.put("timestamp", FieldValue.serverTimestamp());
        map.put("user_id", currentUserId);
        db.collection("GroupChatMessageCont").add(map);
        userMessage.setText("");
    }

}
//    private ActivityGroupchatBinding binding;
//    private User receiverUser;
//    private List<User> groupUsers;
//    private List<GroupChatMessage> chatMessages;
//    private GroupChatAdapter groupChatAdapter;
//    private PreferenceManager preferenceManager;
//    private FirebaseFirestore database;
//    private String conversionId = null;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityGroupchatBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        setListeners();
//        loadReceiverDetails();
//        init();
//        listenMessages();
//    }
//
//    private void init(){
//        preferenceManager = new PreferenceManager(getApplicationContext());
//        chatMessages = new ArrayList<>();
//        groupChatAdapter = new GroupChatAdapter(
//                chatMessages,
//                preferenceManager.getString(Constants.KEY_GROUP_SENDER_ID)
//        );
//        binding.chatRecyclerView.setAdapter(groupChatAdapter);
//        database = FirebaseFirestore.getInstance();
//    }
//
//    private void sendMessage(){
//        HashMap<String,Object> message = new HashMap<>();
//        message.put(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USER_ID));
//        message.put(Constants.KEY_RECEIVER_ID,receiverUser.id);
//        message.put(Constants.KEY_MESSAGE,binding.inputMessage.getText().toString());
//        message.put(Constants.KEY_TIMESTAMP, new Date());
//        message.put(Constants.KEY_TYPE_OF_CHAT,"GroupChat");
//        database.collection(Constants.KEY_COLLECTION_GROUPCHAT).add(message);
//        if (conversionId != null){
//            updateConversion(binding.inputMessage.getText().toString());
//        }else{
//            HashMap<String, Object> conversion = new HashMap<>();
//            conversion.put(Constants.KEY_SENDER_ID,preferenceManager.getString(Constants.KEY_USER_ID));
//            conversion.put(Constants.KEY_SENDER_NAME,preferenceManager.getString(Constants.KEY_NAME));
//            conversion.put(Constants.KEY_SENDER_IMAGE,preferenceManager.getString(Constants.KEY_IMAGE));
//            conversion.put(Constants.KEY_RECEIVER_ID,receiverUser.id);
//            conversion.put(Constants.KEY_RECEIVER_NAME,receiverUser.name);
//            conversion.put(Constants.KEY_RECEIVER_IMAGE, receiverUser.image);
//            conversion.put(Constants.KEY_LAST_MESSAGE, binding.inputMessage.getText().toString());
//            conversion.put(Constants.KEY_TIMESTAMP, new Date());
//            conversion.put(Constants.KEY_TYPE_OF_CHAT,"GroupChat");
//            addConversion(conversion);
//        }
//        binding.inputMessage.setText(null);
//    }
//
//    private void listenMessages(){
//        database.collection(Constants.KEY_COLLECTION_GROUPCHAT)
//                .whereEqualTo(Constants.KEY_SENDER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
//                .whereEqualTo(Constants.KEY_RECEIVER_ID,receiverUser.id)
//                .addSnapshotListener(eventListener);
//        database.collection(Constants.KEY_COLLECTION_GROUPCHAT)
//                .whereEqualTo(Constants.KEY_SENDER_ID,receiverUser.id)
//                .whereEqualTo(Constants.KEY_RECEIVER_ID, preferenceManager.getString(Constants.KEY_USER_ID))
//                .addSnapshotListener(eventListener);
//    }
//
//    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
//        if(error != null){
//            return;
//        }
//        if (value != null){
//            int count = chatMessages.size();
//            for (DocumentChange documentChange : value.getDocumentChanges()) {
//                if (documentChange.getType() == DocumentChange.Type.ADDED) {
//                    GroupChatMessage chatMessage = new GroupChatMessage();
//                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
//                    chatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
//                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
//                    chatMessage.dateTime = getReadableDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
//                    chatMessage.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
//                    chatMessages.add(chatMessage);
//                }
//            }
//            Collections.sort(chatMessages,(obj1, obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
//            if (count == 0){
//                groupChatAdapter.notifyDataSetChanged();
//            }else{
//                groupChatAdapter.notifyItemRangeInserted(chatMessages.size(),chatMessages.size());
//                binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size() -1);
//            }
//            binding.chatRecyclerView.setVisibility(View.VISIBLE);
//        }
//        binding.progressBar.setVisibility(View.GONE);
//        if (conversionId == null){
//            checkForConversation();
//        }
//    };
//
//    private Bitmap getBitmapFromEncodedString(String encodedImage){
//        byte[] bytes = Base64.decode(encodedImage, Base64.DEFAULT);
//        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//    }
//
//    private void loadReceiverDetails() {
//        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
//        binding.textName.setText("GroupChat");
////        groupUsers.add(0,receiverUser);
//    }
//
//    private void setListeners(){
//        binding.imageBack.setOnClickListener(v -> onBackPressed());
//        binding.layoutSend.setOnClickListener(v -> sendMessage());
//        binding.addUser.setOnClickListener(v -> addUser());
//    }
//
//    private void addUser(){
//        startActivity(new Intent(getApplicationContext(), GroupChatActivity.class));
//
//    }
//
//    private String getReadableDateTime(Date date){
//        return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
//    }
//
//    private void addConversion(HashMap<String,Object> conversion){
//        database.collection(Constants.KEY_COLLECTION_GROUPCONVERSATIONS)
//                .add(conversion)
//                .addOnSuccessListener(documentReference -> conversionId = documentReference.getId());
//    }
//
//    private void updateConversion(String message){
//        DocumentReference documentReference =
//                database.collection(Constants.KEY_COLLECTION_GROUPCONVERSATIONS).document(conversionId);
//        documentReference.update(
//                Constants.KEY_LAST_MESSAGE, message,
//                Constants.KEY_TIMESTAMP, new Date()
//        );
//    }
//
//    private void checkForConversation(){
//        if (chatMessages.size() != 0){
//            checkForConversionRemotely(
//                    preferenceManager.getString(Constants.KEY_USER_ID),
//                    receiverUser.id
//            );
//            checkForConversionRemotely(
//                    receiverUser.id,
//                    preferenceManager.getString(Constants.KEY_USER_ID)
//            );
//            checkForConversionRemotely(
//                    "GroupChat",
//                    preferenceManager.getString(Constants.KEY_TYPE_OF_CHAT)
//            );
//        }
//    }
//    private void checkForConversionRemotely(String senderId, String receiverId){
//        database.collection(Constants.KEY_COLLECTION_GROUPCONVERSATIONS)
//                .whereEqualTo(Constants.KEY_SENDER_ID,senderId)
//                .whereEqualTo(Constants.KEY_RECEIVER_ID,receiverId)
//                .get()
//                .addOnCompleteListener(conversionOnCompleteListener);
//    }
//
//    private final OnCompleteListener<QuerySnapshot> conversionOnCompleteListener = task -> {
//        if (task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0){
//            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
//            conversionId = documentSnapshot.getId();
//        }
//    };
//
////    @Override
////    public void onConversionClicked(User user) {
////        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
////        Intent intent = new Intent(getApplicationContext(), GroupChatActivity.class);
////        intent.putExtra(Constants.KEY_USER,user);
////        startActivity(intent);
////        groupUsers.add(user);
////        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
////        binding.textName.setText(receiverUser.name);
////    }
//}

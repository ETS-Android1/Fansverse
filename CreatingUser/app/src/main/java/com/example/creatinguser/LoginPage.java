package com.example.creatinguser;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.creatinguser.activities.SignUpActivity;
import com.example.creatinguser.databinding.LoginPageBinding;
import com.example.creatinguser.utilities.Constants;
import com.example.creatinguser.utilities.PreferenceManager;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginPage extends AppCompatActivity {

    private LoginPageBinding binding;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(getApplicationContext(), HomePage.class);
            startActivity(intent);
            finish();
        }
        binding = LoginPageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {
        binding.tvRegister.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class)));
        binding.loginButton.setOnClickListener(v ->
        {
            if (isValidSignInDetails()) {
                signIn();
            }
        });
    }

    private void signIn(){
        loading(true);
        FirebaseFirestore database = FirebaseFirestore.getInstance();
        database.collection(Constants.KEY_COLLECTION_USERS)
                .whereEqualTo(Constants.KEY_EMAIL, binding.inputEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, binding.loginPass.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null
                            && task.getResult().getDocuments().size() > 0) {
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferenceManager.putString(Constants.KEY_USER_ID, documentSnapshot.getId());
                        preferenceManager.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        preferenceManager.putString(Constants.KEY_IMAGE, documentSnapshot.getString(Constants.KEY_IMAGE));
                        Intent intent = new Intent(getApplicationContext(), HomePage.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } else {
                        loading(false);
                        showToast("Unable to sign in");
                    }
                });
    }

    private void loading(Boolean isLoading) {
        if (isLoading) {
            binding.loginButton.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.loginButton.setVisibility(View.VISIBLE);
        }
    }

    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidSignInDetails() {
        if (binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter email");
            return false;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(binding.inputEmail.getText().toString()).matches()) {
            showToast("Enter valid email");
            return false;
        } else if (binding.loginPass.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;
        } else {
            return true;
        }
    }
}
//
//
//        mAuth = FirebaseAuth.getInstance();
//
//        //getApplicationContext() renders the current context of the Application which can be used in various ways.
//        context = getApplicationContext();
//
//        // Mapping the actual widget objects in the activity_registration.xml to the corresponding mentioned references
//        login_username = findViewById(R.id.login_uname);
//        login_password = findViewById(R.id.login_pass);
//        login_button = findViewById(R.id.login_button);
//        signup_button = findViewById(R.id.singup_btn);
//
//        //Creating an HashMap object and assigning to the above mentioned 'credentials' hashmap reference.
//        credentials = new HashMap<String, String>();
//
//        // Setting an onClick listener to Login button
//        login_button.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Toast toast;
//
//                // If the 'credentials' hashmap has no data in it and login button is clicked,then a toast message saying 'please sign up' will be displayed.
//                if (credentials == null) {
//                    toast = Toast.makeText(context, "Please Sign up ", Toast.LENGTH_LONG);
//                    toast.show();
//                } else {
//                    // Retrieving the data associated with the login and password fields
//                    String current_username = login_username.getText().toString();
//                    String current_password = login_password.getText().toString();
//                    mAuth.signInWithEmailAndPassword(current_username, current_password)
//                            .addOnCompleteListener(LoginPage.this, new OnCompleteListener<AuthResult>() {
//                                @Override
//                                public void onComplete(@NonNull Task<AuthResult> task) {
//                                    if (task.isSuccessful()) {
////                                         Sign in success, update UI with the signed-in user's information
//                                        Log.d(TAG, "signInWithEmail:success");
//                                        FirebaseUser user = mAuth.getCurrentUser();
//                                        Intent intent = new Intent(getApplicationContext(), HomePage.class);
//                                        //intent.putExtra("message", current_username);
//                                        startActivity(intent);
////                                        updateUI(user);
//                                    }
//                                    else {
//                                        // If sign in fails, display a message to the user.
//                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
//                                        Toast.makeText(LoginPage.this, "Authentication failed. Check Email or Password",
//                                                Toast.LENGTH_SHORT).show();
////                                        updateUI(null);
//                                    }
//                                }
//                            });
//                }
//
//            }
//        });


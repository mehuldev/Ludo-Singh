package com.example.ludo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ludo.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SignIn extends AppCompatActivity {

    ActivitySignInBinding binding;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    FirebaseDatabase database;
    User user;
    ActivityResultLauncher<Intent> signUpActivityLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    Log.d("activity.SignIn",
                            "activity.SignUp result received in activity.SignIn");
                    if(result.getResultCode() == Activity.RESULT_CANCELED)
                        return;
                    setResult(Activity.RESULT_OK, result.getData());
                    finish();
                }
            }
    );;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("activity.SignIn", "activity.SignIn created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.buttonSignInSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("activity.SignIn", "SignIn button clicked");
                if(binding.textSignInUserName.getText().toString().isEmpty()) {
                    binding.textSignInUserName.setError("Enter your Username");
                    return;
                }
                if(binding.textSignInPassword.getText().toString().isEmpty()) {
                    binding.textSignInPassword.setError("Password Required");
                    return;
                }
                progressDialog = new ProgressDialog(SignIn.this);
                progressDialog.setTitle("Login");
                progressDialog.setMessage("Logging in");
                progressDialog.show();
                firebaseAuth(binding.textSignInUserName.getText().toString(),
                                binding.textSignInPassword.getText().toString());
            }
        });

        binding.buttonSignInSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("activity.SignIn", "SignUp button clicked");
                Log.d("activity.SignIn", "Launching activity.SignUp");
                signUpActivityLauncher.launch(new Intent(SignIn.this,SignUp.class));
            }
        });
    }
    protected void firebaseAuth(String username, String password){

        //Get uid corresponding to the username
        database.getReference().child("UIDs").child(username).get().
                addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task1) {
                progressDialog.dismiss();
                if(task1.getResult().exists()){
                    Log.d("activity.SignIn", "Username found in database");
                    //Get user class object corresponding to the uid
                    database.getReference().child("Users").child(task1.getResult().getValue().toString())
                            .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            user = task.getResult().getValue(User.class);
                            auth.signInWithEmailAndPassword(user.email, password)
                                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task1) {
                                            if(task1.isSuccessful()) {
                                                Log.d("activity.SignIn", "Email and password match");
                                                Toast.makeText(SignIn.this,
                                                        (CharSequence) ("Login Successful"),
                                                        Toast.LENGTH_SHORT);
                                                Intent intent = new Intent();
                                                intent.putExtra("User",user);
                                                setResult(Activity.RESULT_OK,intent);
                                                Log.d("activity.SignIn",
                                                        "activity result set, finishing the activity");
                                                finish();
                                            }
                                            else {
                                                Log.d("activity.SignIn",
                                                        "Email and password do no match");
                                                user = null;
                                                Toast.makeText(SignIn.this,
                                                        (CharSequence) "Wrong username or password",
                                                        Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }
                    });

                }
                else{
                    Log.d("activity.SignIn", "User not found in database");
                    user = null;
                    Toast.makeText(SignIn.this,
                            "User does not exist",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
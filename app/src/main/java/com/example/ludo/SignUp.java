package com.example.ludo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.IInterface;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.ludo.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {

    ActivitySignUpBinding binding;
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    FirebaseAuth auth;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("activity.SignUp", "activity.SignUp created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(SignUp.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're creating your account");
        binding.buttonSignUpSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("activity.SignUp", "SignUp button clicked");
                progressDialog.show();
                auth.createUserWithEmailAndPassword(binding.textSignUpEmail.getText().toString(),
                        binding.textSignUpPassword.getText().toString()).
                        addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d("activity.SignUp",
                                        "createUserWithEmailAndPassword complete");
                                progressDialog.dismiss();
                                if(task.isSuccessful()){
                                    Log.d("activity.SignUp", "Created user in FirebaseAuth");
                                    user = new User(binding.textSignUpName.getText().toString(),
                                            binding.textSignUpUserName.getText().toString(),
                                            binding.textSignUpEmail.getText().toString(),
                                            task.getResult().getUser().getUid());
                                    database.getReference().child("Users").child(user.uid).setValue(user);
                                    database.getReference().child("UIDs").child(user.username).setValue(user.uid);
                                    Toast.makeText(SignUp.this,"User Created Successfully",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent();
                                    intent.putExtra("User",user);
                                    setResult(Activity.RESULT_OK, intent);
                                    Log.d("activity.SignUp",
                                            "activity result set, finishing the activity");
                                    finish();
                                }
                                else{
                                    Log.d("activity.SignUp", task.getException().getMessage());
                                    Toast.makeText(SignUp.this,task.getException().getMessage()
                                            ,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        binding.buttonSignUpSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("activity.SignUp",
                        "SignIn button clicked, " +
                                "finishing activity.SignIn and going back to activity.SignIn");
                Intent intent = new Intent();
                user = null;
                intent.putExtra("User",user);
                setResult(Activity.RESULT_CANCELED,intent);
                Log.d("activity.SignUp", "activity result set, finishing the activity");
                finish();
            }
        });

    }
}
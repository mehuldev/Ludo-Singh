package com.example.ludo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;

//Activity to check login status
public class Authenticate extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseDatabase database;
    ActivityResultLauncher<Intent> signInActivityLauncher;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("activity.Authenticate", "Authenticate activity created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authenticate);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        Log.d("activity.Authenticate", "Authenticating from FirebaseAuth");
        FirebaseUser firebaseUser = auth.getCurrentUser();
        if(firebaseUser == null){
            //If no user is logged in, start sign in activity

            Log.d("activity.Authenticate", "Not logged in, launching signIn activity");
            Intent signInIntent = new Intent(Authenticate.this,SignIn.class);
            signInActivityLauncher = registerForActivityResult(
                    new ActivityResultContracts.StartActivityForResult(),
                    new ActivityResultCallback<ActivityResult>() {
                        @Override
                        public void onActivityResult(ActivityResult result) {
                            Log.d("activity.Authenticate",
                                    "Received Result from activity.SignIn");
                            assert(result.getResultCode() == Activity.RESULT_OK);
                            setResult(Activity.RESULT_OK,result.getData());
                            Log.d("activity.Authenticate",
                                    "Activity result set, finishing this activity");
                            finish();
                        }
                    }
            );
            Log.d("activity.Authenticate", "Launching activity.SignIn");
            signInActivityLauncher.launch(signInIntent);
        }
        else{
            Log.d("activity.Authenticate", "User already logged in");
            database.getReference().child("Users").child(firebaseUser.getUid())
                    .get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    assert (task.isSuccessful());
                    Log.d("activity.Authenticate", "Received user object from database");
                    Intent intent = new Intent();
                    user = task.getResult().getValue(User.class);
                    intent.putExtra("User",user);
                    setResult(Activity.RESULT_OK, intent);
                    Log.d("activity.Authenticate",
                            "Activity result set, finishing this activity");
                    finish();
                }
            });
        }
    }
}
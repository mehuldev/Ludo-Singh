package com.example.ludo;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

//string facing
public class MainActivity extends AppCompatActivity {
    User user;

    ActivityResultLauncher<Intent> authActivityLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("activity.MainActivity", "Main activity created");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent = new Intent(MainActivity.this,Authenticate.class);
        authActivityLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        Log.d("activity.MainActivity",
                                "Received result from activity.Authenticate");
                        assert (result.getResultCode() == Activity.RESULT_OK);
                        user = (User) result.getData().getSerializableExtra("User");
                    }
                }
        );
        Log.d("activity.MainActivity", "Launching activity.Authenticate");
        authActivityLauncher.launch(intent);
    }
}